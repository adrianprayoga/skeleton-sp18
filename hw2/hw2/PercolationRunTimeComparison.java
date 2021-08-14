package hw2;

import edu.princeton.cs.algs4.Stopwatch;

public class PercolationRunTimeComparison {
    public static void main(String[] args) {
        int[] nList = {50, 100, 200};
        int[] tList = {50, 100};

        for (int n : nList) {
            for (int t : tList) {
                Stopwatch s = new Stopwatch();
                new PercolationStats(n, t, new PercolationFactory());
                System.out.println("Weighted Find N: "+n+"; T: "+t+"; time taken: "+s.elapsedTime());
            }
        }

        for (int n : nList) {
            for (int t : tList) {
                Stopwatch s = new Stopwatch();
                new PercolationStats(n, t, new PercolationFactory(), true);
                System.out.println("Quick Find N: "+n+"; T: "+t+"; time taken: "+s.elapsedTime());
            }
        }
    }

    /*
    N: 400; T: 200; time taken: 2.121
    N: 400; T: 400; time taken: 3.378
    N: 400; T: 800; time taken: 6.666
    N: 400; T: 1600; time taken: 13.364
    N: 800; T: 200; time taken: 11.165
    N: 800; T: 400; time taken: 21.607
    N: 800; T: 800; time taken: 42.571
    N: 800; T: 1600; time taken: 85.374
    N: 1600; T: 200; time taken: 71.831
    N: 1600; T: 400; time taken: 144.148
    N: 1600; T: 800; time taken: 286.786
    N: 1600; T: 1600; time taken: 573.004


    Weighted Find N: 50; T: 50; time taken: 0.054
    Weighted Find N: 50; T: 100; time taken: 0.033
    Weighted Find N: 100; T: 50; time taken: 0.048
    Weighted Find N: 100; T: 100; time taken: 0.083
    Weighted Find N: 200; T: 50; time taken: 0.134
    Weighted Find N: 200; T: 100; time taken: 0.252
    Quick Find N: 50; T: 50; time taken: 0.144
    Quick Find N: 50; T: 100; time taken: 0.276
    Quick Find N: 100; T: 50; time taken: 1.956
    Quick Find N: 100; T: 100; time taken: 3.969
    Quick Find N: 200; T: 50; time taken: 27.437
    Quick Find N: 200; T: 100; time taken: 55.404
     */
}
