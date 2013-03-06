package tkt.util;

import robocode.AdvancedRobot;
import robocode.util.Utils;

/**
 * Utility class which helps control an AdvancedRobot.
 * @author Todd Taomae
 */
public class AdvancedRobotUtility {
  private AdvancedRobot robot;

  /**
   * Constructs a new AdvancedRobotUtility which helps control the specified robot.
   * @param robot the robot which this AdvancedRobotUtility controls
   */
  public AdvancedRobotUtility(AdvancedRobot robot) {
    this.robot = robot;
  }

  /**
   * Immediately moves robot to the specified location.
   * Returns whether or not the robot successfully moved to the location.
   *
   * @param x x-coordinate of the location
   * @param y y-coordinate of the location
   * @return true if robot successfully moved to location, false otherwise
   */
  public boolean moveToLocation(double x, double y) {
    double robotX = this.robot.getX();
    double robotY = this.robot.getY();

    double distance = MathUtility.getDistance(robotX, robotY, x, y);

    this.turnToLocation(x, y);

    this.robot.ahead(distance);

    return (Math.abs(this.robot.getX() - x) < 1.0 && Math.abs(this.robot.getY() - y) < 1.0);
  }

  /**
   * Immediately turns the robot to face the specified location.
   *
   * @param x x-coordinate of the location
   * @param y y-coordinate of the location
   */
  public void turnToLocation(double x, double y) {
    double robotX = this.robot.getX();
    double robotY = this.robot.getY();

    double heading = MathUtility.getDirectionRadians(robotX, robotY, x, y);

    this.turnToHeadingRadians(heading);
  }

  /**
   * Immediately turns the robot to face the heading specified in degrees.
   *
   * @param headingDegrees heading in degrees
   */
  public void turnToHeading(double headingDegrees) {
    double turnAmount = headingDegrees - this.robot.getHeading();
    turnAmount = Utils.normalRelativeAngleDegrees(turnAmount);

    this.robot.turnRight(turnAmount);
  }

  /**
   * Immediately turns the robot to face the heading specified in radians.
   *
   * @param headingRadians heading in radians
   */
  public void turnToHeadingRadians(double headingRadians) {
    double headingDegrees = Math.toDegrees(headingRadians);

    this.turnToHeading(headingDegrees);
  }

  /**
   * Immediately turns the robot's gun to point at the specified location.
   *
   * @param x x-coordinate of the location
   * @param y y-coordinate of the location
   */
  public void turnGunToLocation(double x, double y) {
    double robotX = this.robot.getX();
    double robotY = this.robot.getY();

    double heading = MathUtility.getDirectionRadians(robotX, robotY, x, y);

    this.turnGunToHeadingRadians(heading);
  }

  /**
   * Immediately turns the robot's gun to point to the heading specified in degrees.
   *
   * @param headingDegrees heading in degrees
   */
  public void turnGunToHeading(double headingDegrees) {
    double turnAmount = headingDegrees - this.robot.getGunHeading();
    turnAmount = Utils.normalRelativeAngleDegrees(turnAmount);

    this.robot.turnGunRight(turnAmount);
  }

  /**
   * Immediately turns the robot's gun to point to the heading specified in radians.
   *
   * @param headingRadians heading in radians
   */
  public void turnGunToHeadingRadians(double headingRadians) {
    double headingDegrees = Math.toDegrees(headingRadians);

    this.turnGunToHeading(headingDegrees);
  }

  /**
   * Sets the robot to move to the specified location when the next execution takes place.
   * Due to robot's limited turn rate and the behavior of synchronous commands, this method
   * will not always be able to move the robot to the specified location.
   *
   * @param x x-coordinate of the location
   * @param y y-coordinate of the location
   */
  public void setMoveToLocation(double x, double y) {
    double robotX = this.robot.getX();
    double robotY = this.robot.getY();

    double distance = MathUtility.getDistance(robotX, robotY, x, y);

    this.setTurnToLocation(x, y);

    this.robot.setAhead(distance);

  }

  /**
   * Sets the robot to turn to face the specified location when the next execution takes place.
   *
   * @param x x-coordinate of the location
   * @param y y-coordinate of the location
   */
  public void setTurnToLocation(double x, double y) {
    double robotX = this.robot.getX();
    double robotY = this.robot.getY();

    double heading = MathUtility.getDirectionRadians(robotX, robotY, x, y);

    this.setTurnToHeadingRadians(heading);
  }

  /**
   * Sets the robot to turn to the heading specified in degrees when the next execution takes
   * place.
   *
   * @param headingDegrees heading in degrees
   */
  public void setTurnToHeading(double headingDegrees) {
    double turnAmount = headingDegrees - this.robot.getHeading();
    turnAmount = Utils.normalRelativeAngleDegrees(turnAmount);

    this.robot.setTurnRight(turnAmount);
  }

  /**
   * Sets the robot to turn to the heading specified in radians when the next execution takes
   * place.
   *
   * @param headingRadians heading in radians
   */
  public void setTurnToHeadingRadians(double headingRadians) {
    double headingDegrees = Math.toDegrees(headingRadians);

    this.setTurnToHeading(headingDegrees);
  }

  /**
   * Sets the robot's gun to face the specified location when the next execution takes place.
   *
   * @param x x-coordinate of the location
   * @param y y-coordinate of the location
   */
  public void setTurnGunToLocation(double x, double y) {
    double robotX = this.robot.getX();
    double robotY = this.robot.getY();

    double heading = MathUtility.getDirectionDegrees(robotX, robotY, x, y);

    this.setTurnGunToHeading(heading);
  }

  /**
   * Sets the robot's gun to turn to the heading specified in degrees when the next execution
   * takes place.
   *
   * @param headingDegrees heading in degrees
   */
  public void setTurnGunToHeading(double headingDegrees) {

    double turnAmount = headingDegrees - this.robot.getGunHeading();
    turnAmount = Utils.normalRelativeAngleDegrees(turnAmount);

    this.robot.setTurnGunRight(turnAmount);
  }

  /**
   * Sets the robot's gun to turn to the heading specified in radians when the next execution
   * takes place.
   *
   * @param headingRadians heading in radians
   */
  public void setTurnGunToHeadingRadians(double headingRadians) {
    double headingDegrees = Math.toDegrees(headingRadians);

    this.setTurnGunToHeading(headingDegrees);
  }
}
