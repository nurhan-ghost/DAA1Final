package Assignment1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Random;

public class QuickSortDepthTest {

    @Test
    void testCorrectnessOnRandomArrays() {
        Random rng = new Random(42);
        for (int t = 0; t < 20; t++) {
            int n = rng.nextInt(500) + 1;
            int[] a = rng.ints(n, -1000, 1000).toArray();
            int[] b = Arrays.copyOf(a, a.length);

            Metrics m = new Metrics();
            new QuickSort(m, rng).sort(a);

            Arrays.sort(b);
            assertArrayEquals(b, a, "Mismatch on random array, trial=" + t);
        }
    }

    @Test
    void testCorrectnessOnAdversarialArrays() {
        Random rng = new Random(42);

        int[][] cases = {
                {1,2,3,4,5,6,7,8,9,10},   // sorted ascending
                {10,9,8,7,6,5,4,3,2,1},   // sorted descending
                {5,5,5,5,5,5,5,5,5,5},    // all equal
                {1,2,1,2,1,2,1,2,1,2}     // repeating pattern
        };

        for (int t = 0; t < cases.length; t++) {
            int[] a = Arrays.copyOf(cases[t], cases[t].length);
            int[] b = Arrays.copyOf(cases[t], cases[t].length);

            Metrics m = new Metrics();
            new QuickSort(m, rng).sort(a);

            Arrays.sort(b);
            assertArrayEquals(b, a, "Mismatch on adversarial case " + t);
        }
    }

    @Test
    void testRecursionDepthIsBounded() {
        int n = 10_000;
        int[] a = new int[n];
        Random rng = new Random(42);
        for (int i = 0; i < n; i++) a[i] = rng.nextInt();

        Metrics m = new Metrics();
        QuickSort qs = new QuickSort(m, rng);
        qs.sort(a);


        for (int i = 1; i < n; i++) {
            assertTrue(a[i - 1] <= a[i], "Array is not sorted");
        }

        // expected depth ≈ 2*log2(n) + small margin
        int bound = (int) (2 * Math.floor(Math.log(n) / Math.log(2))) + 5;
        assertTrue(m.maxDepth <= bound,
                "Recursion depth too high: " + m.maxDepth + " (bound=" + bound + ")");
    }
}
