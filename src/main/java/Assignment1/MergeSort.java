package Assignment1;

public class MergeSort {
    private final Metrics m;
    private final int CUTOFF = 32;
    public MergeSort(Metrics m){ this.m = m; }
    public void sort(int[] a){
        if (a==null || a.length<2) return;
        int[] buf = new int[a.length];
        mergesort(a, buf, 0, a.length, 0);
    }
    private void mergesort(int[] a, int[] buf, int l, int r, int depth){
        m.enter();
        try {
            if (r - l <= CUTOFF) {
                insertionSort(a, l, r);
                return;
            }
            int mid = (l + r) >>> 1;
            mergesort(a, buf, l, mid, depth+1);
            mergesort(a, buf, mid, r, depth+1);
            if (a[mid-1] <= a[mid]) return;
            int i=l, j=mid, k=l;
            while (i<mid && j<r){
                m.incComp();
                if (a[i] <= a[j]) { buf[k++] = a[i++]; m.incWrite(); }
                else { buf[k++] = a[j++]; m.incWrite(); }
            }
            while (i<mid) { buf[k++]=a[i++]; m.incWrite(); }
            while (j<r) { buf[k++]=a[j++]; m.incWrite(); }
            for (k=l;k<r;k++){ a[k]=buf[k]; m.incWrite(); }
        } finally { m.exit(); }
    }
    private void insertionSort(int[] a,int l,int r){
        for (int i=l+1;i<r;i++){
            int key=a[i]; int j=i-1;
            while (j>=l){
                m.incComp();
                if (a[j] > key){ a[j+1]=a[j]; m.incWrite(); j--; }
                else break;
            }
            a[j+1]=key; m.incWrite();
        }
    }
}
