package lab11.graphs;

import edu.princeton.cs.algs4.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    private int s; //Start
    private int t; //
    private boolean targetFound = false;
    private Maze maze;
    private Queue<Integer> fringe;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s; // Set edge to start from start
        marked[s] = true;

        fringe = new Queue<Integer>();
        fringe.enqueue(s);
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()

        while (!fringe.isEmpty() && !targetFound) {
            int v = fringe.dequeue();
            for (int neighbor : maze.adj(v)) {
                if (marked[neighbor]) {
                    continue;
                }

                marked[neighbor] = true;
                edgeTo[neighbor] = v;
                distTo[neighbor] = distTo[v] + 1;
                announce();

                if (neighbor == t) {
                    targetFound = true;
                    return;
                }

                fringe.enqueue(neighbor);
            }
        }
    }

    @Override
    public void solve() {
         bfs();
         System.out.println(distTo[0]);
         System.out.println(edgeTo[0]);

    }
}

