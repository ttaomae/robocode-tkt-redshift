package tkt.test;

import static org.junit.Assert.assertTrue;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.IRobotSnapshot;
import robocode.control.testing.RobotTestBed;
import tkt.RedShift;

/**
 * Tests that RedShift is within at the preferred distance from the enemy robot
 * for the majority of the time.
 *
 * @author Todd Taomae
 */
public class TestRedShiftPreferredDistance extends RobotTestBed {
  private long turnsAtPreferredDistance = 0;
  private long turnsNotAtPreferredDistance = 0;

  /**
   * Specifies the robots that are to be matched up in this test case.
   * @return The comma-delimited list of robots in this match.
   */
  @Override
  public String getRobotNames() {
    return "tkt.RedShift,sample.SittingDuck";
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
   * At the end of each turn, checks to see if the two robots are within within the preferred
   * distance.
   *
   * @param event Info about the current state of the battle
   */
  @Override
  public void onTurnEnded(TurnEndedEvent event) {
    IRobotSnapshot[] robots = event.getTurnSnapshot().getRobots();

    IRobotSnapshot robotA = robots[0];
    IRobotSnapshot robotB = robots[1];

    // get distance between two robots
    double xDiff = robotB.getX() - robotA.getX();
    double yDiff = robotB.getY() - robotA.getY();
    double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);

    // if robots are within the preferred distance
    if (Math.abs(RedShift.PREFERRED_DISTANCE - distance) < RedShift.DISTANCE_BUFFER) {
      turnsAtPreferredDistance++;
    }
    else {
      turnsNotAtPreferredDistance++;
    }
  }


  /**
   * The actual test, which asserts that the two robots were at the preferred distance
   * for most of the battle.
   * @param event Details about the completed battle.
   */
  @Override
  public void onBattleCompleted(BattleCompletedEvent event) {
    assertTrue("Preffered distance for at least half the battle",
        turnsAtPreferredDistance > turnsNotAtPreferredDistance);
  }
}
