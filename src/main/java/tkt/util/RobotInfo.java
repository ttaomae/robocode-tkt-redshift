package tkt.util;

import java.awt.geom.Point2D;
import robocode.Robot;
import robocode.ScannedRobotEvent;

/**
 * Robot information.
 *
 * @author Todd Taomae
 */
public class RobotInfo {
  private final String name;
  private double energy;
  private double x;
  private double y;
  private double heading;
  private double velocity;
  private double absoluteBearing;
  private boolean justFired;
  private double shotsFired;
  private double shotsHit;

  /**
   * Constructs a new RobotInfo which contains information taken from the specified
   * ScannedRobotEvent.
   * @param robot scanning robot
   * @param event describes scanned robot
   */
  public RobotInfo(Robot robot, ScannedRobotEvent event) {
    this.name = event.getName();

    this.energy = -1.0; // initialize energy to an invalid value
    this.updateInfo(robot, event);
  }

  /**
   * Updates the robot info.
   * @param robot scanning robot
   * @param event describes the scanned robot
   * @throws IllegalArgumentException if the event describes a different robot
   */
  public final void updateInfo(Robot robot, ScannedRobotEvent event) {
    if (!event.getName().equals(this.name)) {
      throw new IllegalArgumentException("event must describe the same robot");
    }
    double previousEnergy = this.energy;
    this.energy = event.getEnergy();
    this.heading = event.getHeadingRadians();
    this.velocity = event.getVelocity();

    // set position
    this.absoluteBearing = MathUtility.getAbsoluteBearing(robot.getHeading(), event.getBearing());
    this.absoluteBearing = Math.toRadians(this.absoluteBearing);
    Point2D.Double position = MathUtility.getTargetPoint(robot.getX(), robot.getY(),
       this.absoluteBearing, event.getDistance());

    this.x = position.getX();
    this.y = position.getY();

    double energyChange = previousEnergy - this.energy;
    if (energyChange > 0.1 && energyChange < 3.0) {
      this.justFired = true;
      this.shotsFired++;
    }
    else {
      this.justFired = false;
    }
  }

  /**
   * Returns the name of the robot.
   * @return the name of the robot
   */
  public String getRobotName() {
    return name;
  }

  /**
   * Returns the energy of the robot.
   * @return the energy of the robot
   */
  public double getEnergy() {
    return energy;
  }

  /**
   * Returns the location of the robot.
   * @return the location of the robot.
   */
  public Point2D.Double getLocation() {
    return new Point2D.Double(this.x, this.y);
  }

  /**
   * Returns the x-coordinate of the robot.
   * @return the x-coordinate of the robot
   */
  public double getX() {
    return this.x;
  }

  /**
   * Returns the y-coordinate of the robot.
   * @return the y-coordinate of the robot
   */
  public double getY() {
    return this.y;
  }

  /**
   * Returns the heading of the robot.
   * @return the heading of the robot
   */
  public double getHeading() {
    return heading;
  }

  /**
   * Returns the velocity of the robot.
   * @return the velocity of the robot.
   */
  public double getVelocity() {
    return velocity;
  }

  /**
   * Returns the absolute bearing to the robot.
   * @return the absolute bearing to the robot
   */
  public double getAbsoluteBearing() {
    return this.absoluteBearing;
  }

  /**
   * Returns whether or not the robot just fired.
   * @return whether or not the robot just fired
   */
  public boolean justFired() {
    return justFired;
  }

  /**
   * Returns the accuracy of the robot.
   * @return the accuracy of the robot
   */
  public double getAccuracy() {
    return (double)this.shotsHit / (double)(this.shotsFired + this.shotsHit);
  }

  /**
   * Adds one to the number of shots hit.
   */
  public void addShotHit() {
    this.shotsHit++;
  }
}
