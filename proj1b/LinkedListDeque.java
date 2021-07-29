public class LinkedListDeque<T> implements Deque<T>{

    private Node sentinel;
    private int size;

    private class Node {
        private T item;
        private Node next;
        private Node prev;

        private Node(T item, Node next, Node prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        size += 1;
        sentinel.next = new Node(item, sentinel.next, sentinel);
        sentinel.next.next.prev = sentinel.next;
    }

    @Override
    public void addLast(T item) {
        size += 1;
        sentinel.prev = new Node(item, sentinel, sentinel.prev);
        sentinel.prev.prev.next = sentinel.prev;

    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        if (size == 0) {
            System.out.println("");
        }

        Node first = sentinel.next;

        for (int i = 0; i < size(); i++) {
            System.out.print(first.item + " ");
            first = first.next;
        }

        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        size -= 1;
        T item = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;

        return item;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }

        size -= 1;
        T item = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;

        return item;
    }

    @Override
    public T get(int index) {

        if (index >= size) {
            return null;
        }

        Node first = sentinel.next;
        int counter = 0;
        while (counter <= index) {
            if (counter == index) {
                return first.item;
            }

            first = first.next;
            counter++;
        }

        return null;
    }

    private T getRecursive(int index, Node node) {
        if (index == 0) {
            return node.item;
        }

        return getRecursive(index - 1, node.next);
    }

    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }

        return getRecursive(index, sentinel.next);
    }
}
