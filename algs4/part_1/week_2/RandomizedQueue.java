import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
  private int queueSize;
  private Item[] queue;

  // construct an empty randomized queue
  public RandomizedQueue() {
    queueSize = 0;
    queue = (Item[]) new Object[1];
  }

  // is the randomized queue empty?
  public boolean isEmpty() {
    return queueSize == 0;
  }

  // return the number of items on the randomized queue
  public int size() {
    return queueSize;
  }

  private void resize(int capacity) {
    Item[] temp = (Item[]) new Object[capacity];
    for (int i = 0; i < queueSize; i++) {
      temp[i] = queue[i];
    }

    queue = temp;
  }

  // add the item
  public void enqueue(Item item) {
    if (item == null) throw new IllegalArgumentException("Input arg CANNOT be null");
    if (queueSize == queue.length) {
      resize(queue.length * 2);
    }
    queue[queueSize++] = item;
  }

  // remove and return a random item
  public Item dequeue() {
    checkExistence();
    int delIdx = StdRandom.uniform(queueSize);
    Item delItem = queue[delIdx];
    queue[delIdx] = queue[--queueSize];
    queue[queueSize] = null;
    if (queueSize > 0 && queueSize < queue.length / 4) resize(queue.length / 2);
    return delItem;
  }

  // return a random item (but do not remove it)
  public Item sample() {
    checkExistence();
    return queue[StdRandom.uniform(queueSize)];
  }

  private void checkExistence() {
    if (isEmpty()) throw new NoSuchElementException("CANNOT get element from empty RandomizedQueue");
  }

  // return an independent iterator over items in random order
  public Iterator<Item> iterator() {
    return new RandomizedQueueIterator();
  }

  private class RandomizedQueueIterator implements Iterator<Item> {
    private final Item[] shuffledItems;
    private int idx;

    public RandomizedQueueIterator() {
      shuffledItems = (Item[]) new Object[queueSize];
      for (int i = 0; i < queueSize; i++) {
        shuffledItems[i] = queue[i];
      }
      StdRandom.shuffle(shuffledItems);
      idx = 0;
    }

    public boolean hasNext() {
      return idx < shuffledItems.length;
    }

    public Item next() {
      if (!hasNext()) {
        throw new NoSuchElementException("no such element when call next");
      }
      Item item = shuffledItems[idx];
      idx++;
      return item;
    }

    public void remove() {
      throw new UnsupportedOperationException("remove is not supported for RandomizedQueue Iterator");
    }
  }

  // unit testing (required)
  public static void main(String[] args) {
    RandomizedQueue<String> rq = new RandomizedQueue<String>();
    rq.enqueue("first");
    rq.enqueue("second");
    rq.enqueue("third");
    rq.enqueue("fourth");
    rq.enqueue("fifth");
    // System.out.println(rq.sample());
    // System.out.println(rq.dequeue());
    // System.out.println(rq.dequeue());
    // System.out.println(rq.isEmpty());
    // System.out.println(rq.dequeue());
    // System.out.println(rq.dequeue());
    // System.out.println(rq.isEmpty());

    for (String s: rq) {
      System.out.println(s);
    }
  }

}