package tkt;

import robocode.AdvancedRobot;
import tkt.util.AdvancedRobotUtility;

/**
 * An AdvancedRobot that uses the AdvancedRobotUtility.
 *
 * @author Todd Taomae
 */
public class TestRobot extends AdvancedRobot {
  private AdvancedRobotUtility roboUtil = new AdvancedRobotUtility(this);

  @Override
  public void run() {
    switch (this.getRoundNum() % 4) {
    case 0:
      this.testMoveToLocation();
      break;
    case 1:
      this.testSetMoveToLocation();
      break;
    case 2:
      this.testTurnGunToLocation();
      break;
    case 3:
      this.testSetTurnGunToLocation();
      break;
    default:
      // do nothing
      break;
    }
  }

  /**
   * Moves the robot to the bottom left of the battlefield.
   */
  private void testMoveToLocation() {
    double target = 50;
    // while the robot is one pixel away from the destination
    double deltaX;
    double deltaY;
    do {
      roboUtil.moveToLocation(50, 50);
      deltaX = Math.abs(this.getX() - target);
      deltaY = Math.abs(this.getY() - target);
    } while (deltaX > 1.0 && deltaY > 1.0);
  }

  /**
   * Moves the robot to the top right of the battlefield.
   */
  private void testSetMoveToLocation() {
    // first move the robot to the bottom left
    // this will ensure that the setMoveToLocation method will work
    this.testMoveToLocation();

    // 50 less than the smallest battlefield size
    double target = 350;

    // while the robot is more than 1 pixel away from the destination.
    double deltaX;
    double deltaY;
    do {
      roboUtil.setMoveToLocation(target, target);
      execute();
      deltaX = Math.abs(this.getX() - target);
      deltaY = Math.abs(this.getY() - target);
    } while (deltaX > 1.0 && deltaY > 1.0);
  }

  /**
   * Turns the robots gun to face the bottom left of the battlefield.
   */
  private void testTurnGunToLocation() {
    this.testMoveToLocation();
    roboUtil.turnGunToLocation(0, 0);
  }

  /**
   * Turns the robot's gun to face the top left of the battlefield.
   */
  private void testSetTurnGunToLocation() {
    this.testMoveToLocation();
    roboUtil.setTurnGunToLocation(0, 0);
  }
}
