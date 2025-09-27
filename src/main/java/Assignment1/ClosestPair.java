package Assignment1;

import java.util.*;

/**
 * Closest pair in 2D - divide and conquer, O(n log n).
 * Uses arrays sorted by x and y.
 */
public class ClosestPair {
    public static class Point {
        public final double x, y;
        public Point(double x, double y){ this.x = x; this.y = y; }
    }
    public static class Result { public final double dist; public final Point a, b; public Result(double d, Point a, Point b){ this.dist=d; this.a=a; this.b=b; } }

    private final Metrics m;
    public ClosestPair(Metrics m){ this.m = m; }

    public Result closest(Point[] pts){
        if (pts == null || pts.length < 2) return new Result(Double.POSITIVE_INFINITY, null, null);
        Point[] px = pts.clone();
        Arrays.sort(px, Comparator.comparingDouble(p -> p.x));
        Point[] py = pts.clone();
        Arrays.sort(py, Comparator.comparingDouble(p -> p.y));
        return rec(px, py);
    }

    private Result rec(Point[] px, Point[] py){
        m.enter();
        try {
            int n = px.length;
            if (n <= 3) return brute(px);
            int mid = n / 2;
            double midx = px[mid].x;
            Point[] leftX = Arrays.copyOfRange(px, 0, mid);
            Point[] rightX = Arrays.copyOfRange(px, mid, n);
            List<Point> ly = new ArrayList<>(), ry = new ArrayList<>();
            for (Point p : py) {
                if (p.x <= midx) ly.add(p); else ry.add(p);
            }
            Result L = rec(leftX, ly.toArray(new Point[0]));
            Result R = rec(rightX, ry.toArray(new Point[0]));
            Result best = L.dist <= R.dist ? L : R;
            double d = best.dist;
            // build strip: points within d of mid line, using py order
            List<Point> strip = new ArrayList<>();
            for (Point p : py) if (Math.abs(p.x - midx) < d) strip.add(p);
            for (int i = 0; i < strip.size(); i++) {
                // check up to 7 neighbors
                for (int j = i+1; j < strip.size() && j <= i+7; j++) {
                    Point a = strip.get(i), b = strip.get(j);
                    double dd = dist(a,b);
                    if (dd < d) { d = dd; best = new Result(d, a, b); }
                }
            }
            return best;
        } finally {
            m.exit();
        }
    }

    private Result brute(Point[] arr){
        double best = Double.POSITIVE_INFINITY; Point pa=null, pb=null;
        for (int i=0;i<arr.length;i++) for (int j=i+1;j<arr.length;j++){
            double d = dist(arr[i], arr[j]);
            if (d < best) { best = d; pa = arr[i]; pb = arr[j]; }
        }
        return new Result(best, pa, pb);
    }

    private double dist(Point a, Point b){
        double dx = a.x - b.x, dy = a.y - b.y; return Math.hypot(dx, dy);
    }
}
