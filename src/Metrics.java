public class Metrics {
    public long comparisons, swaps, calls;
    public int maxDepth, curDepth;

    public void reset() { comparisons = swaps = calls = 0; maxDepth = curDepth = 0; }
    public void enter() { calls++; if (++curDepth > maxDepth) maxDepth = curDepth; }
    public void exit()  { curDepth--; }
}