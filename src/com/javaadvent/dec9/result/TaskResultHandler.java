package com.javaadvent.dec9.result;

import java.util.concurrent.CountDownLatch;

import com.javaadvent.dec9.model.Task;

/**
 * Handles updates on results being obtained by solving tasks
 * This is used to measure the performance of different implementations
 *  
 * @author florin.bunau
 */
public class TaskResultHandler {

    /**
     * Counts down tasks being solved completely
     */
    private CountDownLatch allTasksDoneLatch;
    
    /**
     * Initialize this handler with a latch we will use to count down tasks solved
     * @param allTasksDoneLatch Latch on which we will countdown everytime we solve a task completly
     */
    public TaskResultHandler(CountDownLatch allTasksDoneLatch) {
        this.allTasksDoneLatch = allTasksDoneLatch;
    }
    
	public void reportQueryResult(Task task, int index, int val) {
	    // Nowhere to report for this example
	}
	
	public void reportUpdateResult(Task task, int index) {
	    // Nowhere to report for this example
	}
	
	/**
	 * Task is done, count it down.
	 * @param task Task that has been completed
	 */
	public void taskDone(Task task) {
	    allTasksDoneLatch.countDown();
	}
}
