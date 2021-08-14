package hw2;

import org.junit.Test;

import static org.junit.Assert.*;

public class PercolationTest {

    @Test
    public void percolationTest() {
        Percolation p = new Percolation(4);
        p.open(2, 1);

        assertFalse(p.isFull(2, 1));
        assertFalse(p.percolates());

        p.open(1, 2);
        p.open(0, 2);
        assertFalse(p.isFull(2, 1));
        assertFalse(p.percolates());

        p.open(2, 2);
        p.open(2, 2);
        assertTrue(p.isFull(2, 1));
        assertEquals(4, p.numberOfOpenSites());

        p.open(3, 2);
        assertTrue(p.percolates());
    }

}
