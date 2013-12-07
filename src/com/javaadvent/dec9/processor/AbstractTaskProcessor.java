package com.javaadvent.dec9.processor;

import com.javaadvent.dec9.model.Task;
import com.javaadvent.dec9.solver.TaskSolver;

/**
 * Base for a task processor.
 * 
 * Different ways of processing tasks will extend from this.
 * Has the same logic for solving a task in the form of a TaskSolver
 *  
 * @author florin.bunau
 */
public abstract class AbstractTaskProcessor {

    /**
     * Logic that can solve a task
     */
    protected TaskSolver taskSolver; 

    public AbstractTaskProcessor(TaskSolver taskSolver) {
        this.taskSolver = taskSolver;
    }

    /**
     * Process a task
     * @param task Task to be processed
     */
    public abstract void process(Task task);

    /**
     * Calling this will notify the processor that it can shutdown it's operations 
     */
    public void shutdown() {
    }
}
