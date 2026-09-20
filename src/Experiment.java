import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class Experiment {
    private static final int[] SIZES = {1_000, 5_000, 10_000, 50_000, 100_000, 500_000, 1_000_000};
    private static final int CLOSEST_MAX_N = 200_000;
    private static final String[] TYPES = {"random", "sorted", "reverse", "duplicates"};
    private static final int WARMUP = 3;
    private static final int RUNS = 5;

    private final Random rnd = new Random(42);

    private interface IntAlgo { void run(int[] a, Metrics m); }
    private interface PointAlgo { void run(Point[] p, Metrics m); }

    public void runAll(String file) throws IOException {
        Path path = Path.of(file);
        Files.createDirectories(path.getParent());
        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(path))) {
            emit(out, "algorithm,inputType,n,timeMs,maxDepth,comparisons");
            runInts("mergesort", (a, m) -> new MergeSorter(m).sort(a), out);
            runInts("quicksort", (a, m) -> new QuickSorter(m).sort(a), out);
            runInts("select", (a, m) -> new DeterministicSelector(m).select(a, a.length / 2), out);
            runPoints("closest_pair", (p, m) -> new ClosestPairSolver(m).solve(p), out);
        }
    }

    private void runInts(String name, IntAlgo algo, PrintWriter out) {
        for (String type : TYPES) {
            for (int n : SIZES) {
                int[] input = genInts(type, n);
                Metrics m = new Metrics();
                long[] times = new long[RUNS];
                for (int r = -WARMUP; r < RUNS; r++) {
                    int[] a = input.clone();   // копия вне замера
                    m.reset();
                    long t0 = System.nanoTime();
                    algo.run(a, m);
                    long t1 = System.nanoTime();
                    if (r >= 0) times[r] = t1 - t0;
                }
                emit(out, line(name, type, n, times, m));
            }
        }
    }

    private void runPoints(String name, PointAlgo algo, PrintWriter out) {
        for (String type : TYPES) {
            for (int n : SIZES) {
                if (n > CLOSEST_MAX_N) continue;
                Point[] input = genPoints(type, n);
                Metrics m = new Metrics();
                long[] times = new long[RUNS];
                for (int r = -WARMUP; r < RUNS; r++) {
                    m.reset();
                    long t0 = System.nanoTime();
                    algo.run(input, m);
                    long t1 = System.nanoTime();
                    if (r >= 0) times[r] = t1 - t0;
                }
                emit(out, line(name, type, n, times, m));
            }
        }
    }

    private int[] genInts(String type, int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            switch (type) {
                case "sorted" -> a[i] = i;
                case "reverse" -> a[i] = n - i;
                case "duplicates" -> a[i] = rnd.nextInt(10);
                default -> a[i] = rnd.nextInt(1_000_000);
            }
        }
        return a;
    }

    private Point[] genPoints(String type, int n) {
        Point[] p = new Point[n];
        for (int i = 0; i < n; i++) {
            double x = switch (type) {
                case "sorted" -> (double) i;
                case "reverse" -> (double) (n - i);
                case "duplicates" -> (double) rnd.nextInt(10);
                default -> rnd.nextDouble() * 1_000_000;
            };
            double y = type.equals("duplicates") ? rnd.nextInt(10) : rnd.nextDouble() * 1_000_000;
            p[i] = new Point(x, y);
        }
        return p;
    }

    private static String line(String name, String type, int n, long[] times, Metrics m) {
        long[] sorted = times.clone();
        Arrays.sort(sorted);
        double ms = sorted[sorted.length / 2] / 1e6;
        return String.format(Locale.US, "%s,%s,%d,%.3f,%d,%d",
                name, type, n, ms, m.maxDepth, m.comparisons);
    }

    private static void emit(PrintWriter out, String line) {
        out.println(line);
        System.out.println(line);
    }
}