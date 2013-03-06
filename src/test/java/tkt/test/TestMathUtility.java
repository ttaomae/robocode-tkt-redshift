package tkt.test;

import static org.junit.Assert.assertEquals;
import java.awt.geom.Point2D;
import org.junit.Test;
import tkt.util.MathUtility;

/**
 * Tests the MathUtility class.
 *
 * @author Todd Taomae
 */
public class TestMathUtility {
  /**
   * Tests the MathUtility constructor.
   */
  @Test
  public void testConstructor() {
    new MathUtility();
  }
  /**
   * Tests the getDirection methods.
   */
  @Test
  public void testGetDirection() {
    testGetDirectionCardinalDirections();
    testGetDirectionDegreeRadianEquivalence();
  }

  /**
   * Tests the getDirection methods.
   */
  public void testGetDirectionCardinalDirections() {
    double actual;
    double expected;

    // test four cardinal and intermediate directions
    expected = 0.0; // north
    actual = MathUtility.getDirectionDegrees(0.0, 0.0, 0.0, 100.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    expected = 45.0; // north-east
    actual = MathUtility.getDirectionDegrees(0.0, 0.0, 100.0, 100.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    expected = 90.0; // east
    actual = MathUtility.getDirectionDegrees(0.0, 0.0, 100.0, 0.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    expected = 135.0; // south-east
    actual = MathUtility.getDirectionDegrees(0.0, 0.0, 100.0, -100.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    expected = 180.0; // south
    actual = MathUtility.getDirectionDegrees(0.0, 0.0, 0.0, -100.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    expected = -135.0; // south-west
    actual = MathUtility.getDirectionDegrees(0.0, 0.0, -100.0, -100.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    expected = -90.0; // west
    actual = MathUtility.getDirectionDegrees(0.0, 0.0, -100.0, 0.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    expected = -45.0; // north-west
    actual = MathUtility.getDirectionDegrees(0.0, 0.0, -100.0, 100.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));
  }

  /**
   * Tests the getDirection methods.
   */
  public void testGetDirectionDegreeRadianEquivalence() {
    double actual;
    double expected;

    actual = MathUtility.getDirectionDegrees(1.0, 2.0, 3.0, 4.0);
    expected = Math.toDegrees(MathUtility.getDirectionRadians(1.0, 2.0, 3.0, 4.0));
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    actual = MathUtility.getDirectionRadians(1.0, 2.0, 3.0, 4.0);
    expected = Math.toRadians(MathUtility.getDirectionDegrees(1.0, 2.0, 3.0, 4.0));
    assertEquals(expected, actual, 5 * Math.ulp(actual));
  }

  /**
   * Tests the getDistance method.
   */
  @Test
  public void testGetDistance() {

    testGetDistanceFromOrigin();
    testGetDistanceToOrigin();
    testGetDistancePythagoreanTriples();
  }

  /**
   * Tests the getDistance method.
   */
  public void testGetDistanceFromOrigin() {
    double actual;
    double expected;

    // test from origin to 100 units in one direction
    expected = 100.0;

    actual = MathUtility.getDistance(0.0, 0.0, 100.0, 0.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    actual = MathUtility.getDistance(0.0, 0.0, 0.0, 100.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    actual = MathUtility.getDistance(0.0, 0.0, -100.0, 0.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    actual = MathUtility.getDistance(0.0, 0.0, 0.0, -100.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));


    // test from origin to 100 units in both directions
    expected = Math.sqrt(2) * 100.0;

    actual = MathUtility.getDistance(0.0, 0.0, 100.0, 100.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    actual = MathUtility.getDistance(0.0, 0.0, 100.0, -100.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    actual = MathUtility.getDistance(0.0, 0.0, -100.0, 100.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    actual = MathUtility.getDistance(0.0, 0.0, -100.0, -100.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));
  }

  /**
   * Tests the getDistance method.
   */
  public void testGetDistanceToOrigin() {
    double actual;
    double expected;

    // test to origin from 100 units in one direction
    expected = 100.0;

    actual = MathUtility.getDistance(100.0, 0.0, 0.0, 0.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    actual = MathUtility.getDistance(0.0, 100.0, 0.0, 0.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    actual = MathUtility.getDistance(-100.0, 0.0, 0.0, 0.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    actual = MathUtility.getDistance(0.0, -100.0, 0.0, 0.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));


    // test from origin to 100 units in both directions
    expected = Math.sqrt(2) * 100.0;

    actual = MathUtility.getDistance(100.0, 100.0, 0.0, 0.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    actual = MathUtility.getDistance(100.0, -100.0, 0.0, 0.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    actual = MathUtility.getDistance(-100.0, 100.0, 0.0, 0.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    actual = MathUtility.getDistance(-100.0, -100.0, 0.0, 0.0);
    assertEquals(expected, actual, 5 * Math.ulp(actual));
  }

  /**
   * Tests the getDistance method.
   */
  public void testGetDistancePythagoreanTriples() {
    double actual;
    double expected;

    actual = MathUtility.getDistance(0.0, 0.0, 3.0, 4.0);
    expected = 5.0;
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    actual = MathUtility.getDistance(0.0, 0.0, 5.0, 12.0);
    expected = 13.0;
    assertEquals(expected, actual, 5 * Math.ulp(actual));

    actual = MathUtility.getDistance(0.0, 0.0, 7.0, 24.0);
    expected = 25.0;
    assertEquals(expected, actual, 5 * Math.ulp(actual));
  }

  /**
   * Tests the getTargetPoint method.
   */
  @Test
  public void testGetTargetPoint() {
    Point2D.Double point;
    double actualX;
    double actualY;
    double expectedX;
    double expectedY;

    // test point at 100 units up and to the right
    point = MathUtility.getTargetPoint(0.0, 0.0, Math.PI / 4, 100.0);
    actualX = point.getX();
    actualY = point.getY();
    expectedX = 100.0 / Math.sqrt(2.0);
    expectedY = 100.0 / Math.sqrt(2.0);
    assertEquals(expectedX, actualX, 5 * Math.ulp(actualX));
    assertEquals(expectedY, actualY, 5 * Math.ulp(actualY));
  }
}
