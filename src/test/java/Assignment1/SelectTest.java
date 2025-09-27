package Assignment1;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SelectTest {

    @Test
    void testKthElementAgainstSort() {
        Random rnd = new Random(42);
        int trials = 100;

        for (int t = 1; t <= trials; t++) {
            int n = rnd.nextInt(50) + 1;
            int[] arr = rnd.ints(n, 0, 100).toArray();

            int k = rnd.nextInt(n);

            Metrics m = new Metrics();
            DeterministicSelect select = new DeterministicSelect(m);

            int expected = Arrays.stream(arr).sorted().toArray()[k];
            int actual = select.select(Arrays.copyOf(arr, arr.length), k);

            System.out.printf(
                    "Trial %3d | n=%2d | k=%2d | expected=%2d | actual=%2d | comps=%d | writes=%d | depth=%d%n",
                    t, n, k, expected, actual, m.comparisons, m.writes, m.maxDepth
            );

            assertEquals(expected, actual, "Mismatch at trial " + t);
        }
    }
}


