import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class QuickSorterTest {
    @Test
    void randomArrays() {
        Random r = new Random(1);
        for (int t = 0; t < 100; t++) {
            int[] a = r.ints(r.nextInt(1000), -100, 100).toArray();   // много дубликатов
            int[] expected = a.clone();
            Arrays.sort(expected);
            new QuickSorter(new Metrics()).sort(a);
            assertArrayEquals(expected, a);
        }
    }

    @Test
    void sortedReverseAndAllEqual() {
        int n = 2000;
        int[] up = new int[n], down = new int[n], same = new int[n];
        for (int i = 0; i < n; i++) { up[i] = i; down[i] = n - i; same[i] = 7; }
        int[] expectedDown = down.clone();
        Arrays.sort(expectedDown);

        new QuickSorter(new Metrics()).sort(up);
        new QuickSorter(new Metrics()).sort(down);
        new QuickSorter(new Metrics()).sort(same);

        for (int i = 0; i < n; i++) { assertEquals(i, up[i]); assertEquals(7, same[i]); }
        assertArrayEquals(expectedDown, down);
    }

    @Test
    void emptyAndSingle() {
        int[] empty = {};
        new QuickSorter(new Metrics()).sort(empty);
        assertEquals(0, empty.length);

        int[] one = {5};
        new QuickSorter(new Metrics()).sort(one);
        assertArrayEquals(new int[]{5}, one);
    }

    @Test
    void recursionDepthIsSmall() {
        int[] a = new Random(2).ints(100_000, 0, 1_000_000).toArray();
        Metrics m = new Metrics();
        new QuickSorter(m).sort(a);
        assertTrue(m.maxDepth <= 20, "depth was " + m.maxDepth);
    }
}