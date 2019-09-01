import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
  private int dequeSize = 0;
  private Node first;
  private Node last;

  private class Node {
    private Item item;
    private Node next;
    private Node prev;
  }

  // construct an empty deque
  public Deque() {
    dequeSize = 0;
    first = null;
    last = null;
  }

  // is the deque empty?
  public boolean isEmpty() {
    return dequeSize == 0;
  }

  // return the number of items on the deque
  public int size() {
    return dequeSize;
  }

  // add the item to the front
  public void addFirst(Item item) {
    checkArgs(item);
    Node oldFirst = first;
    first = new Node();
    first.item = item;
    first.next = oldFirst;
    if (oldFirst == null) {
      first.prev = null;
      last = first;
    } else {
      oldFirst.prev = first;
    }
    dequeSize++;
  }

  // add the item to the back
  public void addLast(Item item) {
    checkArgs(item);
    Node oldLast = last;
    last = new Node();
    last.item = item;
    last.prev = oldLast;
    if (oldLast == null) {
      last.next = null;
      first = last;
    } else {
      oldLast.next = last;
    }
    dequeSize++;
  }

  private void checkArgs(Item item) {
    if (item == null) {
      throw new IllegalArgumentException("input should NOT be null");
    }
  }

  // remove and return the item from the front
  public Item removeFirst() {
    isRemovable();
    Node oldFirst = first;
    first = first.next;
    if (first != null) {
      first.prev = null;
    } else {
      last = null;
    }
    dequeSize--;
    return oldFirst.item;
  }

  // remove and return the item from the back
  public Item removeLast() {
    isRemovable();
    Node oldLast = last;
    last = last.prev;
    if (last != null) {
      last.next = null;
    } else {
      first = null;
    }
    dequeSize--;
    return oldLast.item;
  }

  // public Node getFirst() {
  //   return first;
  // }

  // public Node getLast() {
  //   return last;
  // }

  private void isRemovable() {
    if (isEmpty()) throw new NoSuchElementException("CANNOT remove element from empty Deque");
  }

  // return an iterator over items in order from front to back
  public Iterator<Item> iterator() {
    return new DequeIterator(first);
  }

  private class DequeIterator implements Iterator<Item> {
    private Node current;
    
    public DequeIterator(Node first) {
      current = first;
    }

    public boolean hasNext() {
      return current != null;
    }

    public Item next() {
      if (current == null) {
        throw new NoSuchElementException("no such element when call next");
      }
      Item item = current.item;
      current = current.next;
      return item;
    }

    public void remove() {
      throw new UnsupportedOperationException("remove is not supported for Deque iterator");
    }
  }

  // unit testing (required)
  public static void main(String[] args) {
    // Deque<String> deque = new Deque<String>();

    // test addFirst/addLast/removeFirst/removeLast
    // deque.addFirst("second");
    // deque.addFirst("first");
    // System.out.println(deque.removeFirst());
    // System.out.println(deque.removeFirst());
    // System.err.println(deque.getFirst());
    // System.out.println(deque.getLast());
    // deque.addLast("one");
    // deque.addLast("two");
    // deque.addLast("three");
    // System.out.println(deque.removeLast());
    // System.err.println(deque.removeFirst());
    // System.err.println(deque.removeLast());
    // System.out.println(deque.getLast());
    // System.out.println(deque.getFirst());

    // deque.addFirst("second");
    // deque.addFirst("first");
    // deque.addLast("three");
    // deque.addLast("four");
    // for (String s: deque) {
    //   System.out.println(s);
    // }


    // String first = deque.removeFirst();
    // System.out.println(first);
  }
}