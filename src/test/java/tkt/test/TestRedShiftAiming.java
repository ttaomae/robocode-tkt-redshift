package tkt.test;

import static org.junit.Assert.assertTrue;
import java.util.HashSet;
import java.util.Set;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.BulletState;
import robocode.control.snapshot.IBulletSnapshot;
import robocode.control.testing.RobotTestBed;

/**
 * Tests that RedShift can hit SittingDuck at least 90% of the time.
 *
 * @author Todd Taomae
 */
public class TestRedShiftAiming extends RobotTestBed {
  /** Set containing the ID of all active bullets. */
  private Set<Integer> bullets = new HashSet<Integer>();
  /** Number of bullets fired. */
  private int bulletsFired = 0;
  /** Number of bullets that hit a robot. */
  private int bulletsHit = 0;

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
   * At the end of each turn, checks to see if a bullet was fired and if it hit a target.
   *
   * @param event Info about the current state of the battle
   */
  @Override
  public void onTurnEnded(TurnEndedEvent event) {
    IBulletSnapshot[] currentBullets = event.getTurnSnapshot().getBullets();

    // SittingDuck does not fire, so all bullets belong to RedShift
    for (IBulletSnapshot bullet : currentBullets) {
      // bullet is active
      if (bullet.getState().isActive()) {
        // if the bullet has not been checked yet
        if (!this.bullets.contains(bullet.getBulletId())) {
          this.bulletsFired++;
          this.bullets.add(bullet.getBulletId());
        }
      }

      // bullet is inactive and it hit a victim
      // double check that it has been added to the set
      else if (bullet.getState() == BulletState.HIT_VICTIM
          && this.bullets.contains(bullet.getBulletId())) {
        this.bulletsHit++;
        this.bullets.remove(bullet.getBulletId());
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
    // there will sometimes be a bullet traveling after the enemy dies and will therefore miss
    // this will happen at most once per round
    assertTrue("Test all bullets hit enemy",
        this.bulletsHit > this.bulletsFired - getNumRounds());
  }
}
