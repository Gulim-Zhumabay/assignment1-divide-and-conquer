public class MergeSorter {
    private static final int CUTOFF = 16;
    private final Metrics m;
    private int[] buf;

    public MergeSorter(Metrics m) { this.m = m; }

    public void sort(int[] a) {
        if (a.length < 2) return;
        buf = new int[a.length];          // один буфер на всё время сортировки
        sort(a, 0, a.length - 1);
    }

    private void sort(int[] a, int lo, int hi) {
        m.enter();
        if (hi - lo + 1 <= CUTOFF) {
            insertion(a, lo, hi);
        } else {
            int mid = (lo + hi) >>> 1;
            sort(a, lo, mid);
            sort(a, mid + 1, hi);
            merge(a, lo, mid, hi);
        }
        m.exit();
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

    private void merge(int[] a, int lo, int mid, int hi) {
        System.arraycopy(a, lo, buf, lo, hi - lo + 1);
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid)      a[k] = buf[j++];
            else if (j > hi)  a[k] = buf[i++];
            else {
                m.comparisons++;
                a[k] = (buf[j] < buf[i]) ? buf[j++] : buf[i++];
            }
        }
    }
}