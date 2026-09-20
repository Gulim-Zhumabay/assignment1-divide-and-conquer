public class DeterministicSelector {
    private final Metrics m;

    public DeterministicSelector(Metrics m) { this.m = m; }

    public int select(int[] a, int k) {
        if (k < 0 || k >= a.length) throw new IllegalArgumentException("k out of range");
        return select(a, 0, a.length - 1, k);
    }

    private int select(int[] a, int lo, int hi, int k) {
        m.enter();
        while (true) {
            if (hi - lo + 1 <= 5) {
                insertion(a, lo, hi);
                m.exit();
                return a[k];
            }
            int pivot = medianOfMedians(a, lo, hi);

            int lt = lo, i = lo, gt = hi;
            while (i <= gt) {
                m.comparisons++;
                if (a[i] < pivot)      swap(a, lt++, i++);
                else if (a[i] > pivot) swap(a, i, gt--);
                else                   i++;
            }

            if (k < lt)      hi = lt - 1;
            else if (k > gt) lo = gt + 1;
            else {
                m.exit();
                return pivot;
            }
        }
    }

    private int medianOfMedians(int[] a, int lo, int hi) {
        int groups = (hi - lo + 5) / 5;
        for (int g = 0; g < groups; g++) {
            int gl = lo + g * 5;
            int gr = Math.min(gl + 4, hi);
            insertion(a, gl, gr);
            swap(a, lo + g, gl + (gr - gl) / 2);
        }

        return select(a, lo, lo + groups - 1, lo + (groups - 1) / 2);
    }

    private void insertion(int[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++) {
            int x = a[i], j = i - 1;
            while (j >= lo) {
                m.comparisons++;
                if (a[j] <= x) break;
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = x;
        }
    }

    private void swap(int[] a, int i, int j) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
        m.swaps++;
    }
}