package tkt.test;

import static org.junit.Assert.assertTrue;
import robocode.BattleResults;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.testing.RobotTestBed;

/**
 * Tests that the test robot can beat the enemy robot at least 90% of the time.
 *
 * @author Todd Taomae
 */
public class TestBattleResults extends RobotTestBed {
  private String testRobot = "sample.SittingDuck";
  private String enemyRobot = "sample.SittingDuck";

  /**
   * Specifies the robots that are to be matched up in this test case.
   * @return The comma-delimited list of robots in this match.
   */
  @Override
  public String getRobotNames() {
    return this.testRobot + "," + this.enemyRobot;
  }

  /**
   * This test runs for 50 rounds.
   * @return The number of rounds.
   */
  @Override
  public int getNumRounds() {
    return 50;
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
   * The actual test, which asserts that the test robot won at least 90% of the rounds
   * against the enemy robot.
   * @param event Details about the completed battle.
   */
  @Override
  public void onBattleCompleted(BattleCompletedEvent event) {
    // Return the results in order of getRobotNames.
    BattleResults[] battleResults = event.getSortedResults();

    BattleResults testRobotResults = battleResults[0];
    String testRobotName = testRobotResults.getTeamLeaderName();

    // check that the winner (first robot in sorted results) is the test robot
    assertTrue("Check " + this.testRobot + " winner", testRobotName.startsWith(this.testRobot));

    // check that the test robot won at least 90% of the battles
    assertTrue(testRobotName + " won " + testRobotResults.getFirsts() + " rounds",
        testRobotResults.getFirsts() > (int)(getNumRounds() * 0.9));
  }

  /**
   * Returns the name of the test robot.
   * @return the name of the test robot.
   */
  public String getTestRobot() {
    return testRobot;
  }

  /**
   * Sets the name of the test robot.
   * @param testRobot name of the test robot
   */
  public void setTestRobot(String testRobot) {
    this.testRobot = testRobot;
  }

  /**
   * Returns the name of the enemy robot.
   * @return the name of the enemy robot
   */
  public String getEnemyRobot() {
    return enemyRobot;
  }

  /**
   * Sets the name of the enemy robot.
   * @param enemyRobot name of the enemy robot
   */
  public void setEnemyRobot(String enemyRobot) {
    this.enemyRobot = enemyRobot;
  }
}
