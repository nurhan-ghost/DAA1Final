package Assignment1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

public class ClosestPairTest {
    private final Random rng = new Random(789);

    @Test
    void testClosestPairSmallN() {
        Metrics m = new Metrics();
        int n = 500;
        ClosestPair.Point[] pts = new ClosestPair.Point[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new ClosestPair.Point(rng.nextDouble() * 1000, rng.nextDouble() * 1000);
        }

        ClosestPair cp = new ClosestPair(m);
        ClosestPair.Result res = cp.closest(pts);

        double bruteForce = Double.POSITIVE_INFINITY;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double d = Math.hypot(pts[i].x - pts[j].x, pts[i].y - pts[j].y);
                bruteForce = Math.min(bruteForce, d);
            }
        }

        assertEquals(bruteForce, res.dist, 1e-9, "Closest pair mismatch");
    }
}

