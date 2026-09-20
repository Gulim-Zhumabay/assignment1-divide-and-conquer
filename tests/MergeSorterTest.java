import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class MergeSorterTest {
    @Test
    void randomArrays() {
        Random r = new Random(1);
        for (int t = 0; t < 100; t++) {
            int[] a = r.ints(r.nextInt(1000), -100, 100).toArray();   // много дубликатов
            int[] expected = a.clone();
            Arrays.sort(expected);
            new MergeSorter(new Metrics()).sort(a);
            assertArrayEquals(expected, a);
        }
    }

    @Test
    void sortedAndReverse() {
        int[] up = new int[500], down = new int[500];
        for (int i = 0; i < 500; i++) { up[i] = i; down[i] = 500 - i; }
        int[] expectedDown = down.clone();
        Arrays.sort(expectedDown);
        new MergeSorter(new Metrics()).sort(up);
        new MergeSorter(new Metrics()).sort(down);
        for (int i = 0; i < 500; i++) assertEquals(i, up[i]);
        assertArrayEquals(expectedDown, down);
    }

    @Test
    void emptyAndSingle() {
        int[] empty = {};
        new MergeSorter(new Metrics()).sort(empty);
        assertEquals(0, empty.length);

        int[] one = {5};
        new MergeSorter(new Metrics()).sort(one);
        assertArrayEquals(new int[]{5}, one);
    }
}