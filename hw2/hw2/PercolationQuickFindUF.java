package hw2;

import edu.princeton.cs.algs4.QuickFindUF;

public class PercolationQuickFindUF {
    // create N-by-N grid, with all sites initially blocked
    private boolean[][] sites;
    private QuickFindUF dsSites;
    private QuickFindUF dsSites2;
    private int numberOfOpenSites;
    private int N;
    private int virtualTopNode;
    private int virtualBottomNode;

    public PercolationQuickFindUF(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N cannot be <= 0");
        }

        this.N = N;
        this.sites = new boolean[N][N];
        this.dsSites = new QuickFindUF(N*N + 2);
        this.dsSites2 = new QuickFindUF(N*N + 1);
        this.virtualTopNode = N*N;
        this.virtualBottomNode = N*N+1;
        this.numberOfOpenSites = 0;
    }

    private int getPositionNumber(int row, int col) {
        return row * N + col;
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            sites[row][col] = true;
            this.numberOfOpenSites += 1;

            int dsPosition =  getPositionNumber(row, col);
            updateDs(row + 1, col, dsPosition);
            updateDs(row - 1, col, dsPosition);
            updateDs(row, col + 1, dsPosition);
            updateDs(row, col - 1, dsPosition);

            if (row == 0) {
                dsSites.union(dsPosition, virtualTopNode);
                dsSites2.union(dsPosition, virtualTopNode);
            }

            if (row == N-1) {
                dsSites.union(dsPosition, virtualBottomNode);
            }
        }
    }

    private void updateDs (int row, int col, int dsPosition) {
       try {
           if(isOpen(row, col)) {
               dsSites.union(dsPosition, getPositionNumber(row, col));
               dsSites2.union(dsPosition, getPositionNumber(row, col));
           }
       } catch (IndexOutOfBoundsException e) {
           return;
       }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col)   {
        validateIndex(row, col);
        return sites[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateIndex(row, col);
        return dsSites2.connected(getPositionNumber(row, col), virtualTopNode);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates()  {
        return dsSites.connected(virtualBottomNode, virtualTopNode);
    }

    private void validateIndex(int row, int col) {
        if (row < 0 || row > N - 1 || col < 0 || col > N - 1) {
            throw new IndexOutOfBoundsException("Out of bounds! Row: "+row+" Col:"+col+" N:"+N);
        }
    }

    public static void main(String[] args) {
        PercolationQuickFindUF p = new PercolationQuickFindUF(3);
    }
}
