package com.javaadvent.dec9.processor;

import com.javaadvent.dec9.model.Task;
import com.javaadvent.dec9.solver.TaskSolver;

/**
 * Naive implementation of a processor.
 * We just solve the task in the same thread, sequentially
 * 
 * @author florin.bunau
 */
public class TaskProcessorSimple extends AbstractTaskProcessor {

    public TaskProcessorSimple(TaskSolver taskSolver) {
        super(taskSolver);
    }

    @Override
    public void process(Task task) {
        taskSolver.solve(task);
        taskSolver.getTaskResultHandler().taskDone(task);
    }

}
