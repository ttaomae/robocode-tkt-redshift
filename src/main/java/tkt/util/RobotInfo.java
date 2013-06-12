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
  /** Current energy of the robot. */
  public double energy;
  /** Current x-coordinate of the robot. */
  public double x;
  /** Current y-coordinate of the robot. */
  public double y;
  /** Current absoluteBearing of the robot. */
  public double absoluteBearing;
  /** True if the robot has just fired. */
  public boolean justFired;
  /** Number of shots fired by the robot. */
  public int shotsFired;
  /** Number of shots fires that sucessfully hit. */
  public int shotsHit;

  /**
   * Constructs a new RobotInfo which contains information taken from the specified
   * ScannedRobotEvent.
   * @param robot scanning robot
   * @param event describes scanned robot
   */
  public RobotInfo(Robot robot, ScannedRobotEvent event) {
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
    double previousEnergy = this.energy;
    this.energy = event.getEnergy();

    // set position
    this.absoluteBearing = robot.getHeading() + event.getBearing();
    this.absoluteBearing = Math.toRadians(this.absoluteBearing);
    double targetX = robot.getX() + (Math.sin(this.absoluteBearing) * event.getDistance());
    double targetY = robot.getY() + (Math.cos(this.absoluteBearing) * event.getDistance());
    Point2D.Double position = new Point2D.Double(targetX, targetY);

    this.x = position.getX();
    this.y = position.getY();

    double energyChange = previousEnergy - this.energy;
    if (energyChange >= 0.1 && energyChange <= 3.0) {
      this.justFired = true;
      this.shotsFired++;
    }
    else {
      this.justFired = false;
    }
  }

  /**
   * Returns the accuracy of the robot.
   * @return the accuracy of the robot
   */
  public double getAccuracy() {
    return (double)this.shotsHit / (double)(this.shotsFired + this.shotsHit);
  }
}
