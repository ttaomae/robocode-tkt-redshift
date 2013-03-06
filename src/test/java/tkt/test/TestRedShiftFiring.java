package tkt.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.HashSet;
import java.util.Set;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.IBulletSnapshot;
import robocode.control.snapshot.IRobotSnapshot;
import robocode.control.testing.RobotTestBed;
import tkt.RedShift;
import tkt.util.MathUtility;

/**
 * Tests that RedShift is within at the preferred distance from the enemy robot
 * for the majority of the time.
 *
 * @author Todd Taomae
 */
public class TestRedShiftFiring extends RobotTestBed {
  /** Set containing the ID of all bullets that have been fired. */
  private Set<Integer> bullets = new HashSet<Integer>();
  /** True if RedShift fired a bullet less than or equal to 1.0. */
  private boolean firePowerOne = false;
  /** True if RedShift fired a bullet between (1.0, 2.0]. */
  private boolean firePowerTwo = false;
  /** True if RedShift fired a bullet greater than 2.0. */
  private boolean firePowerThree = false;
  /** True if RedShift fired a bullet while farthter than the maximum firing distance. */
  private boolean firedOutsideMaxDistance = false;

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
    IBulletSnapshot[] currentBullets = event.getTurnSnapshot().getBullets();

    IRobotSnapshot robotA = robots[0];
    IRobotSnapshot robotB = robots[1];

    // get distance between two robots
    double distance = MathUtility.getDistance(robotA.getX(), robotA.getY(),
                                              robotB.getX(), robotB.getY());

    // SittingDuck does not fire, so all bullets belong to RedShift

    for (IBulletSnapshot bullet : currentBullets) {
      // if the bullet has not been checked yet
      if (!this.bullets.contains(bullet.getBulletId())) {
        this.bullets.add(bullet.getBulletId());

        // since the bullet was not previously in the set, this is the first turn that it exists
        // check if it was fired while the robots were farther than the max distance
        // 8 is the max velocity of a robot; add 16 to the max distance in case both robots
        // moved 8 pixels away from each other between the turn the setFire command was issued
        // and the turn when the bullet was actually fired
        if (distance > RedShift.getMaxFiringDistance() + 16) {
          this.firedOutsideMaxDistance = true;
        }
      }

      // tests different bullet powers
      if (bullet.getPower() < 1.0) {
        this.firePowerOne = true;
      }
      else if (bullet.getPower() <= 2.0) {
        this.firePowerTwo = true;
      }
      else if (bullet.getPower() > 2.0) {
        this.firePowerThree = true;
      }
    }
  }

  /**
   * The actual test, which asserts that the RedShift fired bullets of different power and
   * that it did not fire when it was outside of the maximum distance.
   * @param event Details about the completed battle.
   */
  @Override
  public void onBattleCompleted(BattleCompletedEvent event) {
    assertTrue("Bullet Power less than 1.0", this.firePowerOne);
    assertTrue("Bullet Power between 1.0 and 2.0", this.firePowerTwo);
    assertTrue("Bullet Power greater than 2.0", this.firePowerThree);

    assertFalse("Did not fire outside of max distance", this.firedOutsideMaxDistance);
  }
}
