package synthesizer;

import org.junit.Test;
import org.junit.Assert.*;

import static org.junit.Assert.*;

public class ArrayRIngBufferTest {

    @Test
    public void arrayRingBufferTest() {

        ArrayRingBuffer a = new ArrayRingBuffer<Integer>(3);
        a.enqueue(1);
        a.enqueue(3);
        a.enqueue(4);

        assertArrayEquals(new Integer[] {1, 3, 4}, a.getArray());
        assertEquals(a.fillCount(), 3);

        assertEquals(a.dequeue(), 1);
        assertEquals(a.fillCount(), 2);

        a.enqueue(5);
        assertArrayEquals(new Integer[] {5, 3, 4}, a.getArray());

        assertEquals(a.dequeue(), 3);
    }
}
