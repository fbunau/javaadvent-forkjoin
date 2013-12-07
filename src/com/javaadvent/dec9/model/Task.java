package com.javaadvent.dec9.model;

import java.util.List;

/**
 * Models a task composed of multiple operations
 * For this example a task is a list of queries.
 * 
 * The task size is how many queries does the task contain. This could be in a real world example a 
 * bulk query list coming from some client
 *  
 * @author florin.bunau
 */
public class Task {
    
    /**
     * Operations to be done in this task
     */
    private final List<Operation> operations;
    /**
     * Type of task. How large it is.
     */
    private final TaskType taskType;
    
    public Task(List<Operation> operations, TaskType taskType) {
        this.operations = operations;
        this.taskType = taskType;
    }
    
    @Override
    public String toString() {
        return taskType.name() + " (" + operations.size() + ")";
    }
    
    public List<Operation> getOperations() {
        return operations;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    /**
     * Possible task sizes
     */
    public static enum TaskType {
        XS  (10), 
        S   (100), 
        M   (1000), 
        L   (10000), 
        XL  (100000),   
        XXL (1000000);
        
        private final int range;
        
        TaskType(int range) {
            this.range = range;
        }

        public int getRange() {
            return range;
        }
        
    }

}
