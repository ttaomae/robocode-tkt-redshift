package tkt.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * A bounded queue which automatically dequeues the earliest value as new values are inserted.
 * Once added, elements cannot be removed manually. The element at index 0 will always be the
 * most recently added element.
 *
 * @param <E> e
 *
 * @author Todd Taomae
 */
public class BoundedQueue<E> implements Iterable<E> {
  private List<E> elements;
  private int maxSize;

  /**
   * Constructs a new BoundedQueue with the specified maximum size.
   * @param size maximum size of this BoundedQueue
   * @throws IllegalArgumentException if the size is less than or equal to zero
   */
  public BoundedQueue(int size) throws IllegalArgumentException {
    if (size <= 0) {
      throw new IllegalArgumentException("size " + size + ": must be greater than 0.");
    }
    this.maxSize = size;
    this.elements = new LinkedList<E>();
  }

  /**
   * Sets the maximum size of this BoundedQueue.
   * @param size the new maximum size of this BoundedQueue
   */
  public void setMaxSize(int size) {
    if (size <= 0) {
      throw new IllegalArgumentException("size " + size + ": must be greater than 0.");
    }

    this.maxSize = size;

    // remove the oldest elements until the size of the list is equal to the size of this queue
    while (this.elements.size() > this.maxSize) {
      this.elements.remove(this.elements.size() - 1);
    }
  }

  /**
   *
   * @param e e
   */
  public void add(E e) {
    // add to front of list
    this.elements.add(0, e);

    // if the queue is full
    if (this.elements.size() > this.maxSize) {
      // remove last element
      this.elements.remove(this.elements.size() - 1);
    }
  }

  /**
   * Returns the size of this BoundedQueue.
   * @return the size of this BoundedQueue
   */
  public int size() {
    return this.elements.size();
  }

  /**
   * Returns the maximum size of this BoundedQueue.
   * @return the maximum size of this BoundedQueue
   */
  public int maxSize() {
    return this.maxSize;
  }

  /**
   * Returns the element at the specified index.
   * @param index index of the element to return
   * @return the element at the specified index
   * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
   */
  public E get(int index) throws IndexOutOfBoundsException {
    return this.elements.get(index);
  }

  @Override
  public Iterator<E> iterator() {
    return this.elements.iterator();
  }
}
