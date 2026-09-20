import java.util.Random;

public class QuickSorter {
    private final Metrics m;
    private final Random rnd = new Random();

    public QuickSorter(Metrics m) { this.m = m; }

    public void sort(int[] a) {
        if (a.length < 2) return;
        sort(a, 0, a.length - 1);
    }

    private void sort(int[] a, int lo, int hi) {
        m.enter();
        while (lo < hi) {
            int pivot = a[lo + rnd.nextInt(hi - lo + 1)];

            int lt = lo, i = lo, gt = hi;
            while (i <= gt) {
                m.comparisons++;
                if (a[i] < pivot)      swap(a, lt++, i++);
                else if (a[i] > pivot) swap(a, i, gt--);
                else                   i++;
            }


            if (lt - lo < hi - gt) {
                sort(a, lo, lt - 1);
                lo = gt + 1;
            } else {
                sort(a, gt + 1, hi);
                hi = lt - 1;
            }
        }
        m.exit();
    }

    private void swap(int[] a, int i, int j) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
        m.swaps++;
    }
}