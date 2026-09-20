import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ClosestPairSolverTest {
    @Test
    void matchesBruteForceIntegerCoords() {
        Random r = new Random(4);
        for (int t = 0; t < 200; t++) {
            int n = 2 + r.nextInt(300);
            Point[] p = new Point[n];
            for (int i = 0; i < n; i++) p[i] = new Point(r.nextInt(100), r.nextInt(100));
            double expected = ClosestPairSolver.bruteForce(p);
            double got = new ClosestPairSolver(new Metrics()).solve(p);
            assertEquals(expected, got, 1e-9);
        }
    }

    @Test
    void matchesBruteForceLargerSet() {
        Random r = new Random(5);
        Point[] p = new Point[2000];
        for (int i = 0; i < p.length; i++) p[i] = new Point(r.nextDouble() * 1000, r.nextDouble() * 1000);
        assertEquals(ClosestPairSolver.bruteForce(p),
                new ClosestPairSolver(new Metrics()).solve(p), 1e-9);
    }

    @Test
    void duplicatePointsGiveZero() {
        Point[] p = {new Point(1, 1), new Point(5, 5), new Point(9, 2), new Point(5, 5), new Point(0, 7)};
        assertEquals(0.0, new ClosestPairSolver(new Metrics()).solve(p), 1e-9);
    }

    @Test
    void twoPoints() {
        Point[] p = {new Point(0, 0), new Point(3, 4)};
        assertEquals(5.0, new ClosestPairSolver(new Metrics()).solve(p), 1e-9);
    }

    @Test
    void tooFewPointsThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new ClosestPairSolver(new Metrics()).solve(new Point[]{new Point(0, 0)}));
    }
}