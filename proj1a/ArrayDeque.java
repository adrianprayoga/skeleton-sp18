public class ArrayDeque<T> {
  private int size;
  private int nextFirst = 0;
  private int nextLast = 1;
  private T[] items;
  private int capacity = 8;
  private int rFactor = 2;
  private int ratio = 4;

  public ArrayDeque() {
    size = 0;
    items = (T[]) new Object[capacity];
  }

  private void resize(int newCapacity) {
    System.out.println("Resizing to new capacity " + newCapacity);

    T[] newItems = (T[]) new Object[newCapacity];
    System.arraycopy(items, 
                    (nextFirst + 1) % capacity, 
                    newItems,
                    0,
                    size - (nextFirst + 1) % capacity
    );

    System.arraycopy(items, 
        0, 
        newItems,
        size - (nextFirst + 1) % capacity,
        nextLast
    );
    
    items = newItems;
    nextLast = size - (nextFirst + 1) % capacity + nextLast;
    nextFirst = newCapacity - 1;
    capacity = newCapacity;
    
  }

  private void downsize(int newCapacity) {
    System.out.println("Resizing to new capacity " + newCapacity);

    T[] oldRef = items;
    int oldSize = size;
    int oldNextFirst = nextFirst;
    items = (T[]) new Object[newCapacity];

    size = 0;
    nextFirst = 0;
    nextLast = 1;

    for(int i = 0; i < oldSize; i++) {
      addLast(oldRef[(oldNextFirst + 1 + i) % capacity]);
    }

    capacity = newCapacity;
  }

  public void addFirst(T item) {
    if(capacity == size) {
      resize(capacity * rFactor);
    }

    size += 1;
    items[nextFirst] = item;
    nextFirst = mod((nextFirst - 1), capacity);
  }

  public void addLast(T item) {
    if(capacity == size) {
      resize(capacity * rFactor);
    }

    size += 1;
    items[nextLast] = item;
    nextLast = (nextLast + 1) % capacity;
  }

  public boolean isEmpty() {
      return size == 0;
  }

  public int size() {
      return size;
  }

  private int mod (int a, int b) {
    if (a >= 0) {
      return a % b;
    } else {
      return b + a;
    }
  }

  public void printDeque() {
    for(int i = 0; i < size; i++) {
      System.out.print(get(i) + " ");
    }  
    System.out.println();
  }

  public T removeFirst() {
    T item = get(0);

    if ((size - 1) * ratio < capacity) {
      downsize(capacity/2);
    }

    items[(nextFirst + 1) % capacity] = null;
    nextFirst = (nextFirst + 1) % capacity;
    size -= 1;
    return item;
  }

  public T removeLast() {
    T item = get(size - 1);

    if ((size - 1) * ratio < capacity) {
      downsize(capacity/2);
    }

    items[mod(nextLast - 1, capacity)] = null;
    nextLast = mod(nextLast - 1, capacity);
    size -= 1;
    return item;
  }

  public T get(int index) {
    if (index >= size) {
      return null;
    }
    return items[(nextFirst + 1 + index) % capacity];
  }
}