package tkt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Iterator;
import org.junit.Test;
import tkt.util.BoundedQueue;

/**
 * Tests the BoundedQueue class.
 *
 * @author Todd Taomae
 */
public class TestBoundedQueue {
  private static String GET_ZERO = "get(0) is equal to the most recent element added";
  private static String EXPECTED_VALUE = "iterator returns the expected value";
  /**
   * Tests the constructor of the BoundedQueue class with an illegal argument.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testConstructorIllegalArgument() {
    new BoundedQueue<Integer>(0);
  }

  /**
   * Tests the maxSize and setMaxSize methods of the BoundedQueue class.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testMaxSize() {
    BoundedQueue<Integer> bq = new BoundedQueue<Integer>(10);

    assertEquals("max size equal to value passed to constructor", bq.maxSize(), 10);

    bq.setMaxSize(20);
    assertEquals("max size equal to value passed to setMaxSize",bq.maxSize(), 20);

    // should throw exception
    bq.setMaxSize(0);
  }

  /**
   * Tests the size method of the BoundedQueue class.
   */
  @Test
  public void testSize() {
    BoundedQueue<Integer> bq = new BoundedQueue<Integer>(3);

    assertEquals("size of a new BoundedQueue equals 0", 0, bq.size());

    bq.add(5);
    assertEquals("size is equal to the number of elements added", 1, bq.size());

    bq.add(10);
    assertEquals("size is equal to the number of elements added", 2, bq.size());

    bq.add(15);
    assertEquals("size is equal to the number of elements added", 3, bq.size());

    // should not exceed the maximum size (3) even though 4 elements were added
    bq.add(20);
    assertEquals("size does not exceed spcified max size", 3, bq.size());
  }


  /**
   * Tests the add and get method of the BoundedQueue class.
   */
  @Test (expected = IndexOutOfBoundsException.class)
  public void testAddAndGet() {
    BoundedQueue<Integer> bq = new BoundedQueue<Integer>(3);

    int expected;
    int actual;

    // add the value 5
    expected = 5;
    bq.add(expected);
    actual = bq.get(0);
    assertEquals(GET_ZERO, expected, actual);

    // add the value 10
    expected = 10;
    bq.add(expected);
    actual = bq.get(0);
    assertEquals(GET_ZERO, expected, actual);
    // value 5 should be at index 1
    expected = 5;
    actual = bq.get(1);
    assertEquals("get(1) is equal to the second most recent element added", expected, actual);

    // add the value 15
    expected = 15;
    bq.add(expected);
    actual = bq.get(0);
    assertEquals(GET_ZERO, expected, actual);
    //value 10 should be at index 1
    expected = 10;
    actual = bq.get(1);
    assertEquals("get(1) is equal to the second most recent element added", expected, actual);
    // value 5 should be at index 2;
    expected = 5;
    actual = bq.get(2);
    assertEquals("get(2) is equal to the third most recent element added", expected, actual);

    // add the value 20
    expected = 20;
    bq.add(expected);
    actual = bq.get(0);
    assertEquals(GET_ZERO, expected, actual);
    // value 15 should be at index 1;
    expected = 15;
    actual = bq.get(1);
    assertEquals("get(1) is equal to the second most recent element added", expected, actual);
    //value 10 should be at index 2
    expected = 10;
    actual = bq.get(2);
    assertEquals("get(2) is equal to the third most recent element added", expected, actual);

    // value 5 would be at index 3, but it should be dequeued
    // should throw an exception
    bq.get(3);
  }

  /**
   * Tests the iterator method of the BoundedQueue class.
   */
  @Test
  public void testIterator() {
    BoundedQueue<Integer> bq = new BoundedQueue<Integer>(3);

    int actual;
    int expected;

    // add the value 5
    expected = 5;
    bq.add(expected);
    // create iterator
    Iterator<Integer> it = bq.iterator();
    // test next and hasNext
    assertTrue("iterator has one element after calling add", it.hasNext());
    actual = it.next();
    assertEquals("iterator returns the expected value", expected, actual);

    assertFalse("iterator does not have a second element after calling add once", it.hasNext());

    // add values 10 and 15
    bq.add(10);
    bq.add(15);

    // create iterator
    it = bq.iterator();

    // test values
    expected = 15;
    assertTrue("iterator has at least one element after calling add three times", it.hasNext());
    actual = it.next();
    assertEquals(EXPECTED_VALUE, expected, actual);

    expected = 10;
    assertTrue("iterator has at least two element after calling add three times", it.hasNext());
    actual = it.next();
    assertEquals(EXPECTED_VALUE, expected, actual);

    expected = 5;
    assertTrue("iterator has at least three element after calling add three times", it.hasNext());
    actual = it.next();
    assertEquals(EXPECTED_VALUE, expected, actual);

    // no more elements
    assertFalse("iterator only has three elements after calling add three times", it.hasNext());

    // add value 20; value 5 should be dequeued
    bq.add(20);

    // create iterator
    it = bq.iterator();

    // test values
    expected = 20;
    assertTrue("iterator has at least one element after calling add three times", it.hasNext());
    actual = it.next();
    assertEquals(EXPECTED_VALUE, expected, actual);

    expected = 15;
    assertTrue("iterator has at least two element after calling add three times", it.hasNext());
    actual = it.next();
    assertEquals(EXPECTED_VALUE, expected, actual);

    expected = 10;
    assertTrue("iterator has at least three element after calling add three times", it.hasNext());
    actual = it.next();
    assertEquals(EXPECTED_VALUE, expected, actual);

    // no more elements
    assertFalse("iterator does not have more elements than the max size of the BoundedQueue",
        it.hasNext());

    // set the max size to 2; value 10 should be removed
    bq.setMaxSize(2);

    // create iterator
    it = bq.iterator();

    expected = 20;
    assertTrue("iterator has at least one element after setting max size to 2", it.hasNext());
    actual = it.next();
    assertEquals(EXPECTED_VALUE, expected, actual);

    expected = 15;
    assertTrue("iterator has at least two element after setting max size to 2", it.hasNext());
    actual = it.next();
    assertEquals(EXPECTED_VALUE, expected, actual);

    // only two elements now
    assertFalse("iterator does not have elements that were removed by setting the max size",
        it.hasNext());

  }
}
