package tkt.test;

import org.junit.Before;

/**
 * Tests that RedShift can beat Crazy at least 90% of the time.
 * @author Todd Taomae
 */
public class TestBattleResultsVersusCrazy extends TestBattleResults {
  /**
   * Sets the names of the robots.
   */
  @Before
  public void setNames() {
    super.setTestRobot("tkt.RedShift");
    super.setEnemyRobot("sample.Crazy");
  }
}
