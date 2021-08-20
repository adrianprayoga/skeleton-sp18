package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.*;

public class Solver{
    private MinPQ priorityQ;
    private SearchNode initialNode;
    private int numberOfMoves;
    private Deque<WorldState> solution;
    public int numOfEnqueue = 0;
    private boolean FULL_OPTIMIZATION = false;
    private Set<WorldState> completedNodes = new HashSet<>();

    /*
    Before we describe this algorithm, we’ll first define a search node of the puzzle to be:

    a WorldState.
    the number of moves made to reach this world state from the initial state.
    a reference to the previous search node.
     */

    private class SearchNode {
        private WorldState ws;
        private int moves;
        private SearchNode prevNode;
        private int estimatedDistanceToGoal;

        public SearchNode(WorldState ws, int moves, SearchNode prevNode) {
            this.ws = ws;
            this.moves = moves;
            this.prevNode = prevNode;
            this.estimatedDistanceToGoal = ws.estimatedDistanceToGoal();
        }
    }

    private class SearchNodeComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode firstNode, SearchNode secondNode) {
            return Integer.compare(firstNode.moves + firstNode.estimatedDistanceToGoal,
                    secondNode.moves + secondNode.estimatedDistanceToGoal);
        }
    }

    public Solver(WorldState initial) {
        this.initialNode = new SearchNode(initial, 0, null);
        this.priorityQ = new MinPQ(8, new SearchNodeComparator());
        this.solution = new ArrayDeque<>();

        priorityQ.insert(this.initialNode);
        iterateSolver();
    }

    private void iterateSolver() {

        while(!priorityQ.isEmpty()) {
            SearchNode searchNode = (SearchNode) priorityQ.delMin();
            if (FULL_OPTIMIZATION) {
                completedNodes.add(searchNode.ws);
            }

            if (searchNode.ws.isGoal()) {
                // Set the number of moves
                this.numberOfMoves = searchNode.moves;
                // Create a list of solution
                getSolution(searchNode);
                return;
            } else {
                int currentMove = searchNode.moves;
                for (WorldState neighbor : searchNode.ws.neighbors()) {
                    if (FULL_OPTIMIZATION && !completedNodes.contains(neighbor)) {
                        priorityQ.insert(new SearchNode(neighbor, currentMove+1, searchNode));
                        numOfEnqueue++;
                    } else if (!FULL_OPTIMIZATION && (searchNode.prevNode == null || !searchNode.prevNode.ws.equals(neighbor))) {
                        priorityQ.insert(new SearchNode(neighbor, currentMove+1, searchNode));
                        numOfEnqueue++;
                    }
                }
            }
        }
    }

    private void getSolution(SearchNode finalNode) {
        SearchNode currentNode = finalNode;
        while(currentNode != null) {
            this.solution.addFirst(currentNode.ws);
            currentNode = currentNode.prevNode;
        }
    }

    public int moves() {
        return this.numberOfMoves;
    }

    public Iterable<WorldState> solution() {
        return this.solution;
    }

//    public static void main(String[] args) {
//        String start = "horse";
//        String goal = "nurse";
//
//        Word startState = new Word(start, goal);
//        Solver solver = new Solver(startState);
//
//        StdOut.println("Minimum number of moves = " + solver.moves());
//        for (WorldState ws : solver.solution()) {
//            StdOut.println(ws);
//        }
//    }
}
