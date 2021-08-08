// TODO: Make sure to make this class a part of the synthesizer package
package synthesizer;
import java.util.Iterator;

//TODO: Make sure to make this class and all of its methods public
//TODO: Make sure to make this class extend AbstractBoundedQueue<t>
public class ArrayRingBuffer<T>  extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        this.first = 0;
        this.last = 0;
        this.capacity = capacity;
        this.fillCount = 0;
        this.rb = (T[]) new Object[capacity];
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        if (this.isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }

        this.rb[last] = x;
        last = (last+1) % capacity;
        fillCount += 1;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    @Override
    public T dequeue() {
        if (this.fillCount == 0) {
            throw new RuntimeException("Ring buffer underflow");
        }

        T temp = this.rb[first];
        first = (first+1) % capacity;

        fillCount -= 1;
        return temp;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    @Override
    public T peek() {
        return rb[first];
    }

    public T[] getArray() {
        return rb;
    }

    @Override
    public Iterator<T> iterator() {
        return new KeyIterator();
    }

    public class KeyIterator implements Iterator<T> {
        private int ptr;
        private int count;
        public KeyIterator() {
            ptr = first;
            count = 0;
        }
        public boolean hasNext() {
            return (count < fillCount());
        }

        public T next() {
            T returnItem = rb[ptr];
            ptr = (ptr+1) % capacity();
            count++;
            return returnItem;
        }
    }

}
