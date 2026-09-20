import java.util.Arrays;
import java.util.Comparator;

public class ClosestPairSolver {
    private final Metrics m;
    private double best;
    private Point[] tmp;

    public ClosestPairSolver(Metrics m) { this.m = m; }

    public double solve(Point[] pts) {
        if (pts.length < 2) throw new IllegalArgumentException("need at least 2 points");
        Point[] p = pts.clone();
        Arrays.sort(p, Comparator.comparingDouble(Point::x));
        tmp = new Point[p.length];
        best = Double.MAX_VALUE;
        rec(p, 0, p.length - 1);
        return best;
    }

    private void rec(Point[] p, int lo, int hi) {
        m.enter();
        int n = hi - lo + 1;

        if (n <= 3) {
            for (int i = lo; i <= hi; i++)
                for (int j = i + 1; j <= hi; j++)
                    update(p[i], p[j]);
            for (int i = lo + 1; i <= hi; i++) {
                Point x = p[i];
                int j = i - 1;
                while (j >= lo && p[j].y() > x.y()) { p[j + 1] = p[j]; j--; }
                p[j + 1] = x;
            }
            m.exit();
            return;
        }

        int mid = (lo + hi) >>> 1;
        double midX = p[mid].x();
        rec(p, lo, mid);
        rec(p, mid + 1, hi);

        int i = lo, j = mid + 1, k = lo;
        while (i <= mid && j <= hi) {
            m.comparisons++;
            tmp[k++] = (p[j].y() < p[i].y()) ? p[j++] : p[i++];
        }
        while (i <= mid) tmp[k++] = p[i++];
        while (j <= hi)  tmp[k++] = p[j++];
        System.arraycopy(tmp, lo, p, lo, n);

        int cnt = 0;
        for (int s = lo; s <= hi; s++) {
            if (Math.abs(p[s].x() - midX) < best) {
                for (int t = cnt - 1; t >= 0 && p[s].y() - tmp[t].y() < best; t--)
                    update(p[s], tmp[t]);
                tmp[cnt++] = p[s];
            }
        }
        m.exit();
    }

    private void update(Point a, Point b) {
        m.comparisons++;
        double dx = a.x() - b.x(), dy = a.y() - b.y();
        double d = Math.sqrt(dx * dx + dy * dy);
        if (d < best) best = d;
    }

    public static double bruteForce(Point[] p) {
        double res = Double.MAX_VALUE;
        for (int i = 0; i < p.length; i++)
            for (int j = i + 1; j < p.length; j++) {
                double dx = p[i].x() - p[j].x(), dy = p[i].y() - p[j].y();
                res = Math.min(res, Math.sqrt(dx * dx + dy * dy));
            }
        return res;
    }
}
