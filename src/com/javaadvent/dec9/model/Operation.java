package com.javaadvent.dec9.model;

/**
 * Models an operation
 * 
 * @author florin.bunau
 */
public abstract class Operation {

    public enum OperationType {
        Q, // Query 
        U  // Update
    }

    private OperationType operationType;
    
    public Operation(OperationType intervalType) {
        this.operationType = intervalType;
    }

    public OperationType getIntervalType() {
        return operationType;
    }

    /**
     * Query an interval
     */
    public static class QueryIntervalOperation extends Operation {
        
        private int left;
        private int right;
        
        public QueryIntervalOperation(int left, int right) {
            super(OperationType.Q);
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "" + super.operationType.name() + "[" + left + ", " + right + "]";
        }

        public int getLeft() {
            return left;
        }

        public int getRight() {
            return right;
        }
    }

    /**
     * Update at an index
     * 
     * TODO: Not used in this example
     */
    public static class UpdateIntervalOperation extends Operation {

        private int index;
        private int val;

        public UpdateIntervalOperation(int index, int val) {
            super(OperationType.U);
            this.index = index;
            this.val = val;
        }

        public int getVal() {
            return val;
        }

        @Override
        public String toString() {
            return "" + super.operationType.name() + "[@" + this.index + ", " + this.val + "]";
        }

        public int getIndex() {
            return index;
        }
        
    }
    
}
