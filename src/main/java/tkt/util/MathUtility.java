package tkt.util;

import java.awt.geom.Point2D;

/**
 * Utility class for performing math using Robocode physics.
 *
 * @author Todd Taomae
 */
public class MathUtility {

  /**
   * Returns the relative angle in degrees from (x1, y1) to (x2, y2).
   *
   * @param x1 starting x position
   * @param y1 starting y position
   * @param x2 ending x position
   * @param y2 ending y position
   * @return  the angle from (x1, y1) to (x2, y2)
   */
  public static double getDirectionDegrees(double x1, double y1, double x2, double y2) {
    return Math.toDegrees(getDirectionRadians(x1, y1, x2, y2));
  }

  /**
   * Returns the relative angle in radians from (x1, y1) to (x2, y2).
   *
   * @param x1 starting x position
   * @param y1 starting y position
   * @param x2 ending x position
   * @param y2 ending y position
   * @return  the angle from (x1, y1) to (x2, y2)
   */
  public static double getDirectionRadians(double x1, double y1, double x2, double y2) {
    double xDiff = x2 - x1;
    double yDiff = y2 - y1;

    return Math.atan2(xDiff, yDiff);
  }

  /**
   * Returns the distance from (x1, y1) to (x2, y2).
   *
   * @param x1 starting x position
   * @param y1 starting y position
   * @param x2 ending x position
   * @param y2 ending y position
   * @return the distance from (x1, y1) to (x2, y2)
   */
  public static double getDistance(double x1, double y1, double x2, double y2) {
    double xDiff = x2 - x1;
    double yDiff = y2 - y1;

    return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
  }

  /**
   * Returns the absolute bearing from one robot to a target. Works for either degrees or radians.
   *
   * @param robotHeading the heading of the robot
   * @param targetBearing the relative bearing of the target
   * @return the absolute bearing from one robot to a target
   */
  public static double getAbsoluteBearing(double robotHeading, double targetBearing) {
    return robotHeading + targetBearing;
  }

  /**
   * Returns the Point2D.Double containing the location of the target, given an initial
   * x- and y-coordinate, a distance to the target, and an absolute bearing to the target.
   * @param x starting x-coordinate
   * @param y starting y-coordinate
   * @param bearing absolute bearing in radians to target
   * @param distance distance to target
   * @return the Point containing the location of the target
   */
  public static Point2D.Double getTargetPoint(double x, double y, double bearing, double distance) {
    double targetX = x + (Math.sin(bearing) * distance);
    double targetY = y + (Math.cos(bearing) * distance);

    return new Point2D.Double(targetX, targetY);
  }
}
