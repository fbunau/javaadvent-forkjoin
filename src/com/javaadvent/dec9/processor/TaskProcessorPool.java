package com.javaadvent.dec9.processor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.javaadvent.dec9.model.Task;
import com.javaadvent.dec9.solver.TaskSolver;

/**
 * This is a thread pool processor for tasks, using a fixed thread pool ExecutorService
 * 
 * We spawn a pool of threads, and as soon as we have a job we submit it to the executor to process it
 * This should run 4 threads solving tasks in parallel. This is not as efficient as a workstealing thread pool, 
 * because we might have one thread busy with a large task, while the others have nothing to do
 * 
 * @author florin.bunau
 */
public class TaskProcessorPool extends AbstractTaskProcessor  {

    /**
     * 4 threads for a 4 core / 4 threads system
     */
    public final int POOL_SIZE = 4;
    
    /**
     * Thread pool
     */
    private ExecutorService threadPool;
    
    public TaskProcessorPool(TaskSolver taskSolver) {
        super(taskSolver);
        threadPool = Executors.newFixedThreadPool(POOL_SIZE);
    }

    @Override
    public void process(Task task) {
        // As soon as we have a task we submit it to the threadpool
        threadPool.submit(new TaskRunnable(task));
    }
    
    @Override
    public void shutdown() {
        threadPool.shutdown();
    }
    
    /**
     * Runnable solver for a task
     */
    class TaskRunnable implements Runnable {

        /**
         * Task to be solved
         */
        private Task task;
        
        public TaskRunnable(Task task) {
            this.task = task;
        }
        
        @Override
        public void run() {
            taskSolver.solve(task);
            taskSolver.getTaskResultHandler().taskDone(task);
        }
    }

}
