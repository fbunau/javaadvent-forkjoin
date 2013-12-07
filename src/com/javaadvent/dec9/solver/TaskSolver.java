package com.javaadvent.dec9.solver;

import com.javaadvent.dec9.model.Operation;
import com.javaadvent.dec9.model.Task;
import com.javaadvent.dec9.result.TaskResultHandler;
import com.javaadvent.dec9.rmq.RMQSegmentTree;

/**
 * Logic used to handle the solving of a task 
 * 
 * @author florin.bunau
 */
public class TaskSolver {
    
    /**
     * Segment tree data structure used for the query and update operations
     */
    private RMQSegmentTree segmentTree;
    /**
     * We report the results of the task after it has been solved partially or completely here
     */
    private TaskResultHandler taskResultHandler;
    
    public TaskSolver(RMQSegmentTree segmentTree, TaskResultHandler taskResultHandler) {
        this.segmentTree = segmentTree;
        this.taskResultHandler = taskResultHandler;
    }
    
    /**
     * Solve a task
     * @param t Task to be solved 
     */
    public void solve(Task t) {
        solve(t, 0, t.getOperations().size());
    }
    
    /**
     * Solve a range of a task
     * 
     * @param t Task to be solved
     * @param from Starting range of task to solve
     * @param to Ending range of task to solve
     */
    public void solve(Task t, int from, int to) {
        for (int k = from; k < to; ++k) {
            solve(t, k);
        }
    }
    
    private void solve(Task t, int k) {
        Operation operation = t.getOperations().get(k);
        switch (operation.getIntervalType()) {
            case Q : solveQuery(t, k, operation);
            case U : solveUpdate(t, k, operation);
        }
    }
    
    /**
     * Solve a query
     * 
     * @param t Task to solve
     * @param k Index of query operation within the task 
     * @param operation Query operation to solve
     */
    private void solveQuery(Task t, int k, Operation operation) {
        Operation.QueryIntervalOperation intervalQ = (Operation.QueryIntervalOperation)operation;
        int result = segmentTree.query(intervalQ.getLeft(), intervalQ.getRight());
        taskResultHandler.reportQueryResult(t, k, result);
    }
    
    /**
     * Solve an update
     * 
     * @param t Task to solve
     * @param k Index of query operation within the task 
     * @param operation Update operation to solve
     */
    private void solveUpdate(Task t, int k, Operation operation) {
        // TODO
    }

    /**
     * @return The task result handler
     */
    public TaskResultHandler getTaskResultHandler() {
        return taskResultHandler;
    }

}
