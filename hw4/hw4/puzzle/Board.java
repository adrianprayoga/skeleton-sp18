package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class Board implements WorldState{
    private int[][] board;
    private int N;
    private int emptyLocationX;
    private int emptyLocationY;


    public Board(int[][] tiles) {

        //use manual for loop to check empty position
        //this.board = Arrays.stream(tiles).map(int[]::clone).toArray(int[][]::new);
        this.N = tiles.length;
        this.board = new int[N][N];

        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                this.board[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    emptyLocationX = i;
                    emptyLocationY = j;
                }
            }
        }
    }

    public int tileAt(int i, int j) {
        if (!isValid(i) || !isValid(j)) {
            throw new IndexOutOfBoundsException("index needs to be between 0 and "+ (N-1));
        }

        return board[i][j];
    }

    private boolean isValid(int x) {
        return !(x < 0 || x > N - 1);
    }

    public int size() {
        return N;
    }

    @Override
    public Iterable<WorldState> neighbors() {
        ArrayList<WorldState> ls = new ArrayList<>();
        int[] iter = {-1, 1};

        for (int i : iter) {
            int newX = emptyLocationX + i;
            int newY = emptyLocationY + 0;
            if (isValid(newX) && isValid(newY)) {
                ls.add(new Board(getNewBoardPosition(newX, newY)));
            }
        }

        for (int i : iter) {
            int newX = emptyLocationX + 0;
            int newY = emptyLocationY + i;
            if (isValid(newX) && isValid(newY)) {
                ls.add(new Board(getNewBoardPosition(newX, newY)));
            }
        }

        return ls;
    }

    private int[][] getNewBoardPosition (int newX, int newY) {
        int[][] newBoard = Arrays.stream(this.board).map(int[]::clone).toArray(int[][]::new);
        int temp = newBoard[newX][newY];
        newBoard[newX][newY] = 0;
        newBoard[emptyLocationX][emptyLocationY] = temp;

        return newBoard;
    }

    public int hamming() {
        int distance = 0;
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                if (i == N - 1 && j == N - 1) {
                    continue;
                } if (board[i][j] != i * N + j + 1) {
                    distance += 1;
                }
            }
        }
        return distance;
    }

    public int manhattan() {
        int distance = 0;
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                if (board[i][j] == 0) {
                    continue;
                }

                int currentVal = board[i][j];
                int correctI = (currentVal - 1) / N;
                int correctJ = (currentVal - 1) % N;

                distance += Math.abs(correctI - i) + Math.abs(correctJ - j);
            }
        }
        return distance;
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    @Override
    public boolean equals(Object y) {
        if (y == null) return false;
        if (!(y instanceof Board)) return false;
        if (this == y) return true;

        return Arrays.deepEquals(board, ((Board) y).getBoard());
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode( board );
    }

    public int[][] getBoard() {
        return board;
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
