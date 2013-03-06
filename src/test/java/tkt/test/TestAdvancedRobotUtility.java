package tkt.test;

import static org.junit.Assert.assertTrue;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.IRobotSnapshot;
import robocode.control.testing.RobotTestBed;

/**
 * Runs a number of battles between TestRobot and SittingDuck. The behavior of TestRobot
 * must be manually inspected.
 *
 * @author Todd Taomae
 */
public class TestAdvancedRobotUtility extends RobotTestBed {
  private static final double EPSILON = 0.00001;
  private boolean moveToLocationPassed = false;
  private boolean setMoveToLocationPassed = false;
  private boolean turnGunToLocationPassed = false;
  private boolean setTurnGunToLocationPassed = false;

  /**
   * Specifies the robots that are to be matched up in this test case.
   * @return The comma-delimited list of robots in this match.
   */
  @Override
  public String getRobotNames() {
    return "tkt.TestRobot,sample.SittingDuck";
  }

  /**
   * This test runs for 20 rounds.
   * @return The number of rounds.
   */
  @Override
  public int getNumRounds() {
    return 20;
  }

  /**
   * Returns true if the battle should be deterministic and thus robots will always start
   * in the same position each time.
   *
   * Override to return false to support random initialization.
   * @return True if the battle will be deterministic.
   */
  @Override
  public boolean isDeterministic() {
    return false;
  }

  /**
   * At the end of each turn, checks to see if TestRobot has performed the right behavior.
   *
   * @param event Info about the current state of the battle
   */
  @Override
  public void onTurnEnded(TurnEndedEvent event) {
    IRobotSnapshot[] robots = event.getTurnSnapshot().getRobots();

    IRobotSnapshot testRobot;

    if (robots[0].getName().startsWith("tkt.TestRobot")) {
      testRobot = robots[0];
    }
    else {
      testRobot = robots[1];
    }

    double target;
    double deltaX;
    double deltaY;
    double expectedGunHeading = (5.0 / 4.0) * Math.PI;
    switch (event.getTurnSnapshot().getRound() % 4) {
    case 0:
      target = 50.0;
      deltaX = Math.abs(testRobot.getX() - target);
      deltaY = Math.abs(testRobot.getY() - target);
      if (deltaX < 1.0 && deltaY < 1.0) {
        this.moveToLocationPassed = true;
      }
      break;
    case 1:
      target = 350.0;
      deltaX = Math.abs(testRobot.getX() - target);
      deltaY = Math.abs(testRobot.getY() - target);
      if (deltaX < 1.0 && deltaY < 1.0) {
        this.setMoveToLocationPassed = true;
      }
      break;
    case 2:
      if (Math.abs(testRobot.getGunHeading() - expectedGunHeading) < EPSILON) {
        this.turnGunToLocationPassed = true;
      }
      break;
    case 3:
      if (Math.abs(testRobot.getGunHeading() - expectedGunHeading) < EPSILON) {
        this.setTurnGunToLocationPassed = true;
      }
      break;
    default:
      // do nothing
    }
  }

  /**
   * The actual test, which asserts that TestRobot had the expected behavior for each round.
   * @param event Details about the completed battle.
   */
  @Override
  public void onBattleCompleted(BattleCompletedEvent event) {
    assertTrue("Test moveToLocation", this.moveToLocationPassed);
    assertTrue("Test setMoveToLocation", this.setMoveToLocationPassed);
    assertTrue("Test turnGunToLocation", this.turnGunToLocationPassed);
    assertTrue("Test setTurnGunToLocation", this.setTurnGunToLocationPassed);
  }
}
