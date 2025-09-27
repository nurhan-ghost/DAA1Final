package Assignment1;

import java.util.Arrays;

/**
 * Deterministic Select (Median of Medians, groups of 5), in-place.
 * select(arr, k) returns k-th smallest (0-based).
 */
public class DeterministicSelect {
    private final Metrics m;
    public DeterministicSelect(Metrics m){ this.m = m; }



    public int select(int[] a, int k){
        if (k < 0 || k >= a.length) throw new IllegalArgumentException("k out of range");
        return selectInplace(a, 0, a.length - 1, k);
    }

    private int selectInplace(int[] a, int lo, int hi, int k){
        while (true){
            if (lo == hi) return a[lo];
            if (hi - lo + 1 <= 10) {
                Arrays.sort(a, lo, hi+1);
                m.addWrites(hi-lo+1);
                return a[lo + k];
            }
            int pivot = medianOfMedians(a, lo, hi);
            int p = partitionAround(a, lo, hi, pivot);
            int leftSize = p - lo;
            if (k == leftSize) return a[p];
            else if (k < leftSize) { hi = p - 1; }
            else { k = k - leftSize - 1; lo = p + 1; }
        }
    }

    private int medianOfMedians(int[] a, int lo, int hi){
        int write = lo;
        for (int i = lo; i <= hi; i += 5){
            int subHi = Math.min(i+4, hi);
            insertionSort(a, i, subHi);
            int median = i + ((subHi - i) >>> 1);
            swap(a, write, median);
            write++;
        }
        int num = write - lo;
        // recursively find median of medians
        return selectInplace(a, lo, write - 1, num/2);
    }

    private void insertionSort(int[] a, int l, int r){
        for (int i = l+1; i <= r; i++){
            int key = a[i];
            int j = i-1;
            while (j >= l){
                m.incComp();
                if (a[j] > key) { a[j+1] = a[j]; m.incWrite(); j--; }
                else break;
            }
            a[j+1] = key; m.incWrite();
        }
    }

    private int partitionAround(int[] a, int lo, int hi, int pivotVal){
        int pivotIdx = lo;
        while (pivotIdx <= hi && a[pivotIdx] != pivotVal) pivotIdx++;
        if (pivotIdx > hi) pivotIdx = lo; // fallback
        swap(a, pivotIdx, hi);
        int store = lo;
        for (int i = lo; i < hi; i++){
            m.incComp();
            if (a[i] < pivotVal) { swap(a, store, i); store++; }
        }
        swap(a, store, hi);
        return store;
    }

    private void swap(int[] a, int i, int j){
        if (i==j) return;
        int t=a[i]; a[i]=a[j]; a[j]=t;
        m.addWrites(2);
    }
}
