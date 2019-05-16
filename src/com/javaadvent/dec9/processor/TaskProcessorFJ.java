package com.javaadvent.dec9.processor;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import com.javaadvent.dec9.model.*;
import com.javaadvent.dec9.solver.TaskSolver;

/**
 * This is a Fork / Join processor for tasks, using a ForkJoinPool    test
 * 
 * At the processing step it will split the task into subtasks until it reaches a threshold
 * Once the threshold is reached it will solve the subtask
 * 
 * @author florin.bunau
 */
public class TaskProcessorFJ extends AbstractTaskProcessor {
    
    /**
     * Work stealing thread pool implementation
     */
    private ForkJoinPool forkJoinPool;
    
    public TaskProcessorFJ(TaskSolver taskSolver) {
        super(taskSolver);
        forkJoinPool = new ForkJoinPool();
    }

    @Override
    public void process(Task task) {
        // Give the pool a subtask to solve. At the beginning task == subtask 
        forkJoinPool.invoke(new Subtask(task, 0, task.getOperations().size(), true));
    }

    /**
     * Wraps a task to be solved, and restricts it's extend to a subtask.
     * 
     * Also implements a FJ recursive action, that will split the subtask into two smaller substasks
     * if it's above a specified threshold
     */
    private class Subtask extends RecursiveAction  {

        private static final long serialVersionUID = 1L;
        
        /**
         * Task to be solved
         */
        final Task task;
        
        /**
         * Limit the task from operation having index 'from'
         */
        final int from;
        /**
         * to operation having index 'to'
         */
        final int to;
        
        /**
         * Is this subtask == the initial task
         */
        final boolean rootTask;

        public Subtask(Task task, int from, int to, boolean rootTask) {
            this.task = task;
            this.from = from;
            this.to = to;
            this.rootTask = rootTask;
        }

        @Override
        protected void compute() {
            // If the subtask size is smaller than an L task. ( XS, S, M ), then go ahead and solve it
            if (to - from < Task.TaskType.L.getRange()) {
                taskSolver.solve(task, from, to);
            }
            // If it's same or larger ( L, XL, XXL ), we will break it into two smaller substasks and solve it later
            else {
                int mid = (from + to) / 2;
                invokeAll(new Subtask(this.task, from, mid, false), 
                          new Subtask(this.task, mid + 1, to, false));
            }
            // We're done solving the subtask, if it's the initial task, then signal that we are done solving the task
            if (rootTask) {
                taskSolver.getTaskResultHandler().taskDone(task);
            }
        }
    }
    
}
