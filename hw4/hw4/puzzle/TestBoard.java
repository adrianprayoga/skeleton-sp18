package hw4.puzzle;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestBoard {
    @Test
    public void verifyImmutability() {
        int r = 2;
        int c = 2;
        int[][] x = new int[r][c];
        int cnt = 0;
        for (int i = 0; i < r; i += 1) {
            for (int j = 0; j < c; j += 1) {
                x[i][j] = cnt;
                cnt += 1;
            }
        }
        Board b = new Board(x);
        assertEquals("Your Board class is not being initialized with the right values.", 0, b.tileAt(0, 0));
        assertEquals("Your Board class is not being initialized with the right values.", 1, b.tileAt(0, 1));
        assertEquals("Your Board class is not being initialized with the right values.", 2, b.tileAt(1, 0));
        assertEquals("Your Board class is not being initialized with the right values.", 3, b.tileAt(1, 1));

        x[1][1] = 1000;
        assertEquals("Your Board class is mutable and you should be making a copy of the values in the passed tiles array. Please see the FAQ!", 3, b.tileAt(1, 1));
    }

    @Test
    public void testEquals() {
        int[][] a = {{1, 2}, {3, 4}};
        int[][] b = {{1, 2}, {3, 5}};

        Board board1 = new Board(a);
        Board board2 = new Board(a);
        Board board3 = new Board(b);

        assertTrue(board1.equals(board2));
        assertFalse(board1.equals(board3));
    }

    @Test
    public void testDistance() {
        int[][] boardState = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};

        Board b = new Board(boardState);

        System.out.println(b);

        assertEquals(5, b.hamming());
        assertEquals(10, b.manhattan());

    }

    @Test
    public void testSimpleBoard() {
        int i = 50;
        String pnum = String.format("%02d", i);
//        String puzzleName = "input/puzzle3x3-" + pnum + ".txt";
        String puzzleName = "input/puzzle50.txt";
        Board b = TestSolver.readBoard(puzzleName);

        System.out.println(b);

        int numMoves = i;
        TestSolver.BoardPuzzleSolution bps = new TestSolver.BoardPuzzleSolution(puzzleName, b, numMoves);
        Solver s = new Solver(b);
        System.out.println("moves "+s.moves());
        assertEquals("Wrong number of moves on " + puzzleName, bps.numMoves, s.moves());
    }
} 
