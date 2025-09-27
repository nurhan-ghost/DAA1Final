package Assignment1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    static final Random RNG = new Random(12345);

    static void assertSorted(int[] a) {
        for (int i = 1; i < a.length; i++)
            if (a[i - 1] > a[i]) throw new AssertionError("Not sorted");
    }

    static void runSanity(Metrics m) {
        System.out.println("=== Running sanity tests ===");

        // MergeSort
        int[] arr = new int[1000];
        for (int i = 0; i < arr.length; i++) arr[i] = RNG.nextInt(10000);
        int[] c = arr.clone();
        m.reset();
        long s = System.nanoTime();
        new MergeSort(m).sort(arr);
        long e = System.nanoTime();
        double ms = (e - s) / 1_000_000.0;
        assertSorted(arr);
        Arrays.sort(c);
        if (!Arrays.equals(arr, c)) throw new AssertionError("Merge mismatch");
        System.out.printf("MergeSort: %.3f ms | depth=%d | comps=%d | writes=%d | allocs=%d%n",
                ms, m.maxDepth, m.comparisons, m.writes, m.allocations);

        // QuickSort
        int n = 1000;
        int[] asc = new int[n];
        for (int i = 0; i < n; i++) asc[i] = i;
        int[] ac = asc.clone();
        m.reset();
        s = System.nanoTime();
        new QuickSort(m, new Random(1)).sort(asc);
        e = System.nanoTime();
        ms = (e - s) / 1_000_000.0;
        assertSorted(asc);
        Arrays.sort(ac);
        if (!Arrays.equals(asc, ac)) throw new AssertionError("Quick mismatch");
        System.out.printf("QuickSort: %.3f ms | depth=%d | comps=%d | writes=%d | allocs=%d%n",
                ms, m.maxDepth, m.comparisons, m.writes, m.allocations);

        // Deterministic Select
        int[] arr2 = new int[1000];
        for (int i = 0; i < 1000; i++) arr2[i] = RNG.nextInt(10000);
        int[] cp = arr2.clone();
        m.reset();
        s = System.nanoTime();
        int kth = new DeterministicSelect(m).select(arr2, 400);
        e = System.nanoTime();
        ms = (e - s) / 1_000_000.0;
        Arrays.sort(cp);
        if (kth != cp[400]) throw new AssertionError("Select mismatch");
        System.out.printf("DeterministicSelect: %.3f ms | depth=%d | comps=%d | writes=%d | allocs=%d%n",
                ms, m.maxDepth, m.comparisons, m.writes, m.allocations);

        // Closest Pair
        ClosestPair.Point[] pts = new ClosestPair.Point[200];
        for (int i = 0; i < 200; i++)
            pts[i] = new ClosestPair.Point(RNG.nextDouble() * 1000, RNG.nextDouble() * 1000);
        ClosestPair cpAlg = new ClosestPair(m);
        m.reset();
        s = System.nanoTime();
        ClosestPair.Result r = cpAlg.closest(pts);
        e = System.nanoTime();
        ms = (e - s) / 1_000_000.0;
        double best = Double.POSITIVE_INFINITY;
        for (int i = 0; i < 200; i++)
            for (int j = i + 1; j < 200; j++)
                best = Math.min(best, Math.hypot(pts[i].x - pts[j].x, pts[i].y - pts[j].y));
        if (Math.abs(best - r.dist) > 1e-9) throw new AssertionError("Closest mismatch");
        System.out.printf("ClosestPair: %.3f ms | depth=%d | comps=%d | writes=%d | allocs=%d%n",
                ms, m.maxDepth, m.comparisons, m.writes, m.allocations);

        System.out.println("=== All sanity tests passed ===\n");
    }

    
    public static void main(String[] args) throws IOException {
        Metrics m = new Metrics();
        runSanity(m);

        int[] sizes = new int[]{1000, 2000, 5000};
        int trials = 3;
        try (FileWriter csv = new FileWriter("metrics_full.csv")) {
            csv.append("algo,n,trial,time_ms,depth,comparisons,writes,allocations\n");
            for (int n : sizes) {
                for (int t = 0; t < trials; t++) {
                    int[] a = new int[n];
                    for (int i = 0; i < n; i++) a[i] = RNG.nextInt(n * 10);

                    // Merge
                    m.reset();
                    int[] c1 = a.clone();
                    long s = System.nanoTime();
                    new MergeSort(m).sort(c1);
                    long e = System.nanoTime();
                    double ms = (e - s) / 1_000_000.0;
                    csv.append(String.format(Locale.ROOT,
                            "MergeSort,%d,%d,%.3f,%d,%d,%d,%d%n",
                            n, t, ms, m.maxDepth, m.comparisons, m.writes, m.allocations));

                    // Quick
                    m.reset();
                    int[] c2 = a.clone();
                    s = System.nanoTime();
                    new QuickSort(m, new Random()).sort(c2);
                    e = System.nanoTime();
                    ms = (e - s) / 1_000_000.0;
                    csv.append(String.format(Locale.ROOT,
                            "QuickSort,%d,%d,%.3f,%d,%d,%d,%d%n",
                            n, t, ms, m.maxDepth, m.comparisons, m.writes, m.allocations));

                    // Select
                    m.reset();
                    int[] c3 = a.clone();
                    int k = n / 2;
                    s = System.nanoTime();
                    int val = new DeterministicSelect(m).select(c3, k);
                    e = System.nanoTime();
                    ms = (e - s) / 1_000_000.0;
                    csv.append(String.format(Locale.ROOT,
                            "Select,%d,%d,%.3f,%d,%d,%d,%d%n",
                            n, t, ms, m.maxDepth, m.comparisons, m.writes, m.allocations));

                    // Closest
                    m.reset();
                    ClosestPair.Point[] pts = new ClosestPair.Point[n];
                    for (int i = 0; i < n; i++)
                        pts[i] = new ClosestPair.Point(RNG.nextDouble() * n, RNG.nextDouble() * n);
                    s = System.nanoTime();
                    ClosestPair.Result res = new ClosestPair(m).closest(pts);
                    e = System.nanoTime();
                    ms = (e - s) / 1_000_000.0;
                    csv.append(String.format(Locale.ROOT,
                            "ClosestPair,%d,%d,%.3f,%d,%d,%d,%d%n",
                            n, t, ms, m.maxDepth, m.comparisons, m.writes, m.allocations));
                }
            }
        }
        System.out.println("Bench finished. metrics_full.csv written.");
    }
}
