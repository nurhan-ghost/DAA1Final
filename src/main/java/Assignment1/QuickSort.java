package Assignment1;

import java.util.Random;

public class QuickSort {
    private final Metrics m;
    private final Random rng;
    public QuickSort(Metrics m, Random rng){ this.m = m; this.rng = rng; }
    public void sort(int[] a){
        if (a==null || a.length<2) return;
        quick(a, 0, a.length-1);
    }
    private void quick(int[] a, int l, int r){
        while (l < r){
            m.enter();
            int pivotIdx = l + rng.nextInt(r - l + 1);
            int pivot = a[pivotIdx];
            swap(a, pivotIdx, r);
            int store = l;
            for (int i=l;i<r;i++){
                m.incComp();
                if (a[i] <= pivot){ swap(a, store, i); store++; }
            }
            swap(a, store, r);
            m.exit();
            int leftSize = store - l;
            int rightSize = r - store;
            if (leftSize < rightSize){
                quick(a, l, store-1);
                l = store+1;
            } else {
                quick(a, store+1, r);
                r = store-1;
            }
        }
    }
    private void swap(int[] a,int i,int j){
        if (i==j) return;
        int t=a[i]; a[i]=a[j]; a[j]=t;
        m.incWrite(); m.incWrite();
    }
}
