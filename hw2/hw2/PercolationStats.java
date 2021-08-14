package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] pList;
    private int T;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("N & T cannot be <= 0");
        }

        StdRandom.setSeed(1);

        this.T = T;
        pList = new double[T];

        for(int i = 0; i < T; i++) {
            Percolation p = pf.make(N);

            while (!p.percolates()) {
                int x = StdRandom.uniform(N);
                int y = StdRandom.uniform(N);

                p.open(x, y);
            }

            pList[i] = Double.valueOf(p.numberOfOpenSites()) / (N*N);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(pList);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(pList);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

}
