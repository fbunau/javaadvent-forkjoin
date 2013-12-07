package com.javaadvent.dec9;

import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.javaadvent.dec9.model.Task;
import com.javaadvent.dec9.processor.AbstractTaskProcessor;
import com.javaadvent.dec9.producer.TaskProducer;
import com.javaadvent.dec9.result.TaskResultHandler;
import com.javaadvent.dec9.rmq.RMQSegmentTree;
import com.javaadvent.dec9.solver.TaskSolver;

/**
 * 
 * This is the main runner.
 * It generates test data tasks and runs to completion different implementation of processors that solve these tasks
 * 
 *  - TaskProcessorSimple : simple sequential solving of tasks
 *  - TaskProcessorPool   : thread pool solving of tasks
 *  - TaskProcessorFJ     : fork/join (work stealing thread pool) solving of tasks
 *  
 *  This runner runs multiple times the implementations and measures their performance
 *  
 *  A task consists of a batch of Range Minimum Queries on a large integer array
 * 
 * @author florin.bunau
 */
public class TestRunner {

    private static final int RAND_SEED = 100;
    private static Random randomGenerator = new Random(RAND_SEED);
    
    /**
     * Package where the task processors implementations reside 
     */
    private static String PROCESSOR_PACKAGE = "com.javaadvent.dec9.processor.";

    /**
     * Size of the array we operate the RMQ queries on
     */
    private static final int LARGE_ARRAY_SIZE = 10000000;
    
    /**
     * How many tasks are in tasks.txt file
     * We need this to know when we are done processing tasks
     */
    private static final int TASKS_IN_TEST_FILE = 60;
    /**
     * How many test runs each processor should get
     */
    private static final int TEST_RUNS = 4;
    /**
     * Sleep this much millis between runs to give the JVM a chance to catch it's breath
     */
    private static final int SLEEP_BETWEEN_RUNS = 2000;
    
    /**
     * Processors to run in the test
     */
    private static String[] processorNameList = new String[] { "TaskProcessorSimple", "TaskProcessorPool", "TaskProcessorFJ" };
    
    /**
     * Array of integers on which we do RMQ queries
     */
    private int[] values = new int[LARGE_ARRAY_SIZE];
    /**
     * Segment tree data structure used for efficient RMQ queries
     */
    private RMQSegmentTree segmentTree;

    
    public static void main(String[] args) {
        TestRunner env = new TestRunner();

        System.out.println("Doing " + TEST_RUNS + " runs for each of the " + processorNameList.length + " processors. Pls wait ...");
        for (String processorName : processorNameList) {
            for (int testNb = 0; testNb < TEST_RUNS; ++testNb) {
                env.run(processorName);
            }
        }
        System.out.println("Test completed.");
    }
    
    public TestRunner() {
        init();
    }
    
    /**
     * Initialize the test runner
     */
    private void init() {
        initValuesArray();
        segmentTree = new RMQSegmentTree(values);
    }
    
    /**
     * Fill the array we are working on with random values
     */
    private void initValuesArray() {
        for (int i = 0; i < LARGE_ARRAY_SIZE; ++i) {
            values[i] = randomGenerator.nextInt(Integer.MAX_VALUE);
        }
    }
    
    /**
     * Solve random query tasks on the array for a given processor
     * 
     * @param processorName Name of the processor to benchmark
     */
    private void run(String processorName) {
        /**
         * Build Processor
         */
        CountDownLatch allTasksDoneLatch = new CountDownLatch(TASKS_IN_TEST_FILE);
        TaskResultHandler resultHandler = new TaskResultHandler(allTasksDoneLatch);
        TaskSolver taskSolver = new TaskSolver(segmentTree, resultHandler);
        AbstractTaskProcessor processor = null;
        try {
            Class processorClass = Class.forName(PROCESSOR_PACKAGE + processorName);
            Constructor processorConstructor = processorClass.getConstructor(TaskSolver.class);
            processor = (AbstractTaskProcessor)processorConstructor.newInstance(taskSolver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * Build task producer
         */
        TaskProducer taskProducer;
        try {
            taskProducer = new TaskProducer();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        
        /**
         * RUN test
         */
        System.out.print(processorName + ": ");
        Task task;
        
        long time1 = System.currentTimeMillis();
        while (true) {
            task = taskProducer.getNext();
            if (task != null) {
                processor.process(task);
            }
            else {
                break;
            }
        }
        
        /**
         * WAIT test completion. Measure performance
         */
        try {
            allTasksDoneLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long time2 = System.currentTimeMillis();
        processor.shutdown();
        
        // Time it took to solve the tasks
        System.out.println(time2-time1);
        
        // Sleep between runs to give the JVM a chance to catch it's breath
        try {
            Thread.sleep(SLEEP_BETWEEN_RUNS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    
    
}
