import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

public class MergeSort {
    /**
     * Removes and returns the smallest item that is in q1 or q2.
     *
     * The method assumes that both q1 and q2 are in sorted order, with the smallest item first. At
     * most one of q1 or q2 can be empty (but both cannot be empty).
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      The smallest item that is in q1 or q2.
     */
    private static <Item extends Comparable> Item getMin(
            Queue<Item> q1, Queue<Item> q2) {
        if (q1.isEmpty()) {
            return q2.dequeue();
        } else if (q2.isEmpty()) {
            return q1.dequeue();
        } else {
            // Peek at the minimum item in each queue (which will be at the front, since the
            // queues are sorted) to determine which is smaller.
            Comparable q1Min = q1.peek();
            Comparable q2Min = q2.peek();
            if (q1Min.compareTo(q2Min) <= 0) {
                // Make sure to call dequeue, so that the minimum item gets removed.
                return q1.dequeue();
            } else {
                return q2.dequeue();
            }
        }
    }

    /** Returns a queue of queues that each contain one item from items. */
    private static <Item extends Comparable> Queue<Queue<Item>>
            makeSingleItemQueues(Queue<Item> items) {
        Queue<Queue<Item>> newQ = new Queue<>();
        for (Item i : items) {
            Queue<Item> individualQ = new Queue<Item>();
            individualQ.enqueue(i);
            newQ.enqueue(individualQ);
        }
        return newQ;
    }

    /**
     * Returns a new queue that contains the items in q1 and q2 in sorted order.
     *
     * This method should take time linear in the total number of items in q1 and q2.  After
     * running this method, q1 and q2 will be empty, and all of their items will be in the
     * returned queue.
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      A Queue containing all of the q1 and q2 in sorted order, from least to
     *              greatest.
     *
     */
    private static <Item extends Comparable> Queue<Item> mergeSortedQueues(
            Queue<Item> q1, Queue<Item> q2) {
        Queue<Item> newQ = new Queue<>();
        while (!q1.isEmpty() || !q2.isEmpty()) {
            newQ.enqueue(getMin(q1, q2));
        }
        return newQ;
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> mergeSort(
            Queue<Item> items) {

        if(items.isEmpty()) return items;

        Queue<Queue<Item>> singleQ = makeSingleItemQueues(items);
        while (singleQ.size() > 1) {
            singleQ.enqueue(mergeSortedQueues(singleQ.dequeue(), singleQ.dequeue()));
        }

        return singleQ.dequeue();
    }



    public static void testMergeSortedQueues() {
        Queue<Integer> q1 = new Queue<>();
        q1.enqueue(1);
        q1.enqueue(3);
        q1.enqueue(8);

        Queue<Integer> q2 = new Queue<>();
        q2.enqueue(2);
        q2.enqueue(9);
        q2.enqueue(10);

        Queue<Integer> q3 = mergeSortedQueues(q1, q2);
        System.out.println(q3);
    }

    public static void testMergeSort() {
        Queue<String> students = new Queue<>();
        students.enqueue("Alice");
        students.enqueue("Vanessa");
        students.enqueue("Ethan");

        Queue<String> studentsSorted = MergeSort.mergeSort(students);
        System.out.println(students);
        // Should be Alice Ethan Vanessa
        System.out.println(studentsSorted);

        Queue<String> students2 = new Queue<>();
        Queue<String> students2Sorted = MergeSort.mergeSort(students2);
    }

    public static void main(String[] args) {
        testMergeSort();
        testMergeSortedQueues();
    }
}
