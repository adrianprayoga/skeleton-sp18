package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private boolean cycleFound = false;
    private int count = 0;

    public MazeCycles(Maze m) {
        super(m);
    }

    @Override
    public void solve() {
        cycleDetector(0, 0);
    }

    private void cycleDetector(int v, int parent) {
        marked[v] = true;
        announce();

        for (int w : maze.adj(v)) {
            if (marked[w]) {
                if (w != parent) {
                    cycleFound = true;
                    edgeTo[w] = v;
                    announce();

                    return;
                }
            } else {
                if (!cycleFound) {
                    cycleDetector(w, v);
                    if (count < 2) {
                        edgeTo[w] = v;
                        announce();
                        count++;
                    }

                }
            }
        }
    }
}

