import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class DeterministicSelectorTest {
    @Test
    void randomTests() {
        Random r = new Random(3);
        for (int t = 0; t < 200; t++) {
            int n = 1 + r.nextInt(2000);
            int[] a = r.ints(n, -500, 500).toArray();
            int k = r.nextInt(n);
            int[] sorted = a.clone();
            Arrays.sort(sorted);
            int got = new DeterministicSelector(new Metrics()).select(a.clone(), k);
            assertEquals(sorted[k], got);
        }
    }

    @Test
    void smallAndAllEqual() {
        assertEquals(9, new DeterministicSelector(new Metrics()).select(new int[]{9}, 0));

        int[] same = new int[1000];
        Arrays.fill(same, 4);
        assertEquals(4, new DeterministicSelector(new Metrics()).select(same, 500));
    }

    @Test
    void sortedAndReverse() {
        int n = 5000;
        int[] up = new int[n], down = new int[n];
        for (int i = 0; i < n; i++) { up[i] = i; down[i] = n - 1 - i; }
        assertEquals(1234, new DeterministicSelector(new Metrics()).select(up, 1234));
        assertEquals(1234, new DeterministicSelector(new Metrics()).select(down, 1234));
    }

    @Test
    void badIndexThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new DeterministicSelector(new Metrics()).select(new int[]{1, 2, 3}, 3));
    }
}
