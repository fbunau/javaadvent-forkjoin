package com.javaadvent.dec9.rmq;

/**
 * Implements a Segment Tree data structure
 * 
 * This data structure supports Query and Update operations on an integer array
 * Query(i, j) - What is the index of the minimum value in the array between indexes i and j
 * Update(i, v) - Index i in the array, now has value v
 * 
 * Init   : O(N)
 * Query  : O(log N)
 * Update : O(log N)
 * 
 * @author florin.bunau
 * See : http://www.topcoder.com/tc?d1=tutorials&d2=lowestCommonAncestor&module=Static
 * See : http://en.wikipedia.org/wiki/Segment_tree
 */
public class RMQSegmentTree {
    
    /**
     * tree[k] Holds the index of the smallest element from values[k_start] .. values[k_end]
     * where [k_start, k_end] are the range values of node 'k'.
     * Node 1 is defined as having [0 .. N] as range values
     * Node 2 is defined as having [0 .. N/2] as range values
     * Node 3 is defined as having [N/2+1 .. N] as range values
     * ...
     * Node k defined recursively using above logic 
     */
    public int[] tree;
    
    /**
     * An array of values to be queried and updated
     */
    public int[] values;

    public RMQSegmentTree(int[] values) {
        int log2n = (int) (Math.log(values.length) / Math.log(2));

        this.values = values;

        // Binary tree of intervals
        tree = new int[1 << (log2n + 2)];

        init(1, 0, values.length-1);
    }

    /**
     * Computes the values in the tree, which we will use to compute the miniumum for a given range.
     * this is O(n)
     * 
     * @param node Id of the node
     * @param left Left interval index
     * @param right Right interval index
     */
    public void init(int node, int left, int right) {
        if (left == right) {
            tree[node] = left;
        }
        else {
            int mid = (left + right) / 2;
            init(2 * node, left, mid);
            init(2 * node + 1, mid + 1, right);

            int minIndexHalf1 = tree[2 * node];
            int minIndexHalf2 = tree[2 * node + 1];

            tree[node] = (values[minIndexHalf1] <= values[minIndexHalf2]) ? minIndexHalf1 : minIndexHalf2;
        }
    }

    /**
     * @param node Id of the node
     * @param left Current considered interval left index
     * @param right Current considered interval right index 
     * @param i Initial query interval left index
     * @param j Initial query interval right index
     * @return The index in the array that holds the minimum value of values between i and j index
     */
    private int query(int node, int left, int right, int i, int j) {
        if (i <= left && right <= j) {
            return tree[node];
        }
        else {

            int mid = (left + right) / 2;
            int minIndexHalf1 = -1, minIndexHalf2 = -1;

            if (i <= mid) {
                minIndexHalf1 = query(2 * node, left, mid, i, j);
            }
            if (j > mid) {
                minIndexHalf2 = query(2 * node + 1, mid + 1, right, i, j);
            }

            if (minIndexHalf1 == -1) {
                return minIndexHalf2;
            }
            if (minIndexHalf2 == -1) {
                return minIndexHalf1;
            }
            if (values[minIndexHalf1] <= values[minIndexHalf2]) {
                return minIndexHalf1;
            }
            return minIndexHalf2;
        }
    }

    /**
     * Updates a value in the array at a specified index. Rebuilds the tree going back up
     * 
     * @param node Id of the node
     * @param left Current considered interval left index
     * @param right Current considered interval right index 
     * @param i Index to be updated
     * @param val Value to which to update the index
     */
    private void update(int node, int left, int right, int i, int val) {
        if (left == right && left == i) {
            tree[node] = i;
            values[i] = val;
        } else {
        
            int mijl = (left + right) / 2;
            
            if (i <= mijl) {
                update(2 * node, left, mijl, i, val);
            }
            else {
                update(2 * node + 1, mijl + 1, right, i, val);
            }
            
            // rebuild tree cache going back up
            tree[node] = (values[tree[2 * node]] < values[tree[2 * node + 1]]) 
                          ? tree[2 * node]
                          : tree[2 * node + 1];
        }
    }
    
    /**
     * @param i Left index
     * @param j Right index
     * @return Index of minimum value in the array in the specified index range
     */
    public int query(int i, int j) {
        return query(1, 0, values.length-1, i, j);
    }
    
    /**
     * Update the value at index i to be val
     * @param i Index to be updated
     * @param val Value to use in the update
     */
    public void update(int i, int val) {
        update(1, 0, values.length-1, i, val);
    }
    
}