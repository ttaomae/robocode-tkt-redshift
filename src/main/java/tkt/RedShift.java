package tkt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import robocode.AdvancedRobot;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RoundEndedEvent;
import robocode.ScannedRobotEvent;
import tkt.util.AdvancedRobotUtility;
import tkt.util.BoundedQueue;
import tkt.util.MathUtility;
import tkt.util.RobotInfo;

/**
 * AdvancedRobot that circles and tracks the enemy.
 *
 * @author Todd Taomae
 */
public class RedShift extends AdvancedRobot {
  /** Print debug information if this value is true. */
  public static final boolean DEBUG = false;
  /** Preferred distance from the enemy. */
  public static final double PREFERRED_DISTANCE = 200.0;
  /** Plus/minus distance from the preferred distance. */
  public static final double DISTANCE_BUFFER = 50.0;
  /** Maximum distance from the enemy where you will still fire. */
  public static final double MAX_FIRING_DISTANCE = 400.0;
  /** Number of velocities being tracked. */
  public static int NUM_VELOCITIES = 1;
  /** Maps the number of velocities tracked to an accuracy. */
  public static Map<Integer, Double> accuracies = new HashMap<Integer, Double>();
  /** The best enemy accuracy with dodging. */
  public static double enemyAccuracyWithDodge = 0.0;
  /** The best enemy accuracy without dodging. */
  public static double enemyAccuracyWithoutDodge = 0.0;

  /** Defines the direction to move. 1 is forward, -1 is backward */
  private int direction = 1;
  /** Information about your target. */
  private RobotInfo targetInfo = null;
  /** Most recent history of velocities of the scanned robot. */
  private BoundedQueue<Double> velocities = new BoundedQueue<Double>(NUM_VELOCITIES);
  /** Number of bullets that hit an enemy. */
  private int hits = 0;
  /** Number of bullets that missed an enemy. */
  private int misses = 0;
  /** True if you want to change directions each time the enemy fires. */
  private boolean dodge = false;
  /** Random number generator. */
  private Random rng = new Random();

  /** Helper for this robot. */
  private AdvancedRobotUtility roboUtil = new AdvancedRobotUtility(this);

  /**
   * On the first round, check if this is a melee battle. Sets up the necessary information
   * at the start of each round.
   */
  @Override
  public void run() {
    setBodyColor(new Color(64, 0, 0));
    setGunColor(new Color(128, 0, 0));
    setRadarColor(new Color(172, 0, 0));
    setBulletColor(new Color(255, 0,0));
    setAdjustGunForRobotTurn(true);
    setAdjustRadarForGunTurn(true);
    setAdjustRadarForRobotTurn(true);

    // determine how many velocities to keep track of and whether or not to dodge
    // on the first round
    if (this.getRoundNum() == 0) {
      // track 1 velocity
      RedShift.NUM_VELOCITIES = 1;
      // don't dodge
      this.dodge = false;
    }
    // on the second round
    else if (this.getRoundNum() == 1) {
      // track 50 velocities
      RedShift.NUM_VELOCITIES = 50;
      // dodge
      this.dodge = true;
    }
    else {
      // dodge if it is the better option
      this.dodge = RedShift.enemyAccuracyWithDodge < RedShift.enemyAccuracyWithoutDodge;

      // on even numbered rounds
      if (this.getRoundNum() % 2 == 0) {
        // try to find the best number of velocities to track, which will be between 1 and 100
        RedShift.NUM_VELOCITIES = RedShift.getBestNumVelocities();
      }
      // on odd numbered rounds
      else {
        // select a random number close to the best option
        int num = RedShift.getBestNumVelocities() + (this.rng.nextInt(21) - 10);
        RedShift.NUM_VELOCITIES = Math.max(1, num);

      }

      if (RedShift.DEBUG) {
        out.printf("velocities tracked: %d%n", NUM_VELOCITIES);
      }
      this.velocities = new BoundedQueue<Double>(NUM_VELOCITIES);
    }

    setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
    execute();
  }

  /**
   * Whenever this robot scans a robot, it will move and aim/fire.
   *
   * @param event information about the scanned robot.
   */
  @Override
  public void onScannedRobot(ScannedRobotEvent event) {
    // infinity lock
    // if you scan a robot, spin radar in opposite direction
    this.setTurnRadarLeftRadians(getRadarTurnRemainingRadians());

    // if it is a melee battle always assume it is a new robot
    if (this.targetInfo == null) {
      this.targetInfo = new RobotInfo(this, event);
    }
    else {
      this.targetInfo.updateInfo(this, event);
    }

    this.velocities.add(event.getVelocity());

    setTurn(event);
    setAimAndFire(event);

    setAhead(this.direction * 100.0);
  }

  /**
   * Turns perpendicular to the scanned robot. Tries to move to a certain distance away.
   *
   * @param event ScannedRobotEvent
   */
  private void setTurn(ScannedRobotEvent event) {
    // if it is a 1-v-1 and the target just fired
    if (this.targetInfo.justFired()) {
      if (RedShift.DEBUG) {
        out.println("enemy fired");
      }
      if (this.dodge) {
        this.direction *= -1;
      }
    }

    // perpendicular to target
    double turnHeading = this.targetInfo.getAbsoluteBearing() + (Math.PI / 2);

    // if you are too far from scanned robot
    if (event.getDistance() > PREFERRED_DISTANCE + DISTANCE_BUFFER) {
      // turn toward the scanned robot
      turnHeading -= this.direction * (Math.PI / 8);
    }
    // if you are too close to scanned robot
    else if (event.getDistance() < PREFERRED_DISTANCE - DISTANCE_BUFFER) {
      // turn away from the scanned robot
      turnHeading += this.direction * (Math.PI / 8);
    }

    roboUtil.setTurnToHeadingRadians(turnHeading);
  }

  /**
   * Uses linear targeting to track the scanned robot.
   *
   * @param event ScannedRobotEvent
   */
  private void setAimAndFire(ScannedRobotEvent event) {
    // choose bullet power
    double bulletPower = 3.0 - (event.getDistance() / 150);
    double bulletVelocity = 20.0 - (3 * bulletPower);

    double myX = getX();
    double myY = getY();

    // get enemy information
    //  _____E
    // |    /
    // |   /
    // |  /
    // |a/
    // |/
    // R
    // R is this Robot; E is the enemy; a is absolute bearing.
    double enemyHeading = event.getHeadingRadians();
    double enemyVelocity = this.getAverageEnemyVelocity();

    double predictedX = this.targetInfo.getX();
    double predictedY = this.targetInfo.getY();

    //
    //  _____F
    // |    /
    // |   /
    // |  / h
    // |a/
    // |/
    // E
    // E is the enemy; F is the final destination; a is absolute bearing; h is hypotenuse
    // hypotenuse is given by distance = velocity*time

    // the amount that the enemy moves during each time step
    double enemyDeltaX = Math.sin(enemyHeading) * enemyVelocity;
    double enemyDeltaY = Math.cos(enemyHeading) * enemyVelocity;

    // distance the bullet travels before reaching target
    double bulletTravelDistance = 0.0;

    // simulate robot movement; each iteration is one tick
    do {
      // update the predicted position of the enemy
      predictedX += enemyDeltaX;
      predictedY += enemyDeltaY;

      // update the position of the bullet
      bulletTravelDistance += bulletVelocity;

      // while bullet has not reached predicted location
    } while (bulletTravelDistance < Point2D.Double.distance(myX, myY, predictedX, predictedY));


    double gunHeading = MathUtility.getDirectionRadians(myX, myY, predictedX, predictedY);
    roboUtil.setTurnGunToHeadingRadians(gunHeading);

    // if it is a melee battle, always fire
    // otherwise only fire if you are within the max firing distance
    if (event.getDistance() <= MAX_FIRING_DISTANCE) {
      setFire(bulletPower);
    }
  }

  /**
   * Returns the weighted moving average velocity of the scanned robot.
   * @return the weighted moving average velocity of the scanned robot
   */
  public double getAverageEnemyVelocity() {
    double result = 0.0;

    // weight equal to the number of elements
    int weight = this.velocities.size();
    int totalWeight = 0;
    for (double v : this.velocities) {
      result += weight * v;
      // decrease weights for older elements
      totalWeight += weight--;
    }

    // divide by the sum of weights which is a triangle number
    result /= totalWeight;

    return result;
  }

  /**
   * Change directions if you hit a wall.
   *
   * @param event HitWallEvent
   */
  @Override
  public void onHitWall(HitWallEvent event) {
//    out.println("hit wall");
    this.direction *= -1;
  }

  /**
   * Change directions if you hit a robot.
   *
   * @param event HitWallEvent
   */
  @Override
  public void onHitRobot(HitRobotEvent event) {
//    out.println("hit robot");
    this.direction *= -1;
  }

  @Override
  public void onHitByBullet(HitByBulletEvent event) {
    this.targetInfo.addShotHit();
  }

  @Override
  public void onBulletHit(BulletHitEvent event) {
    this.hits++;
  }

  @Override
  public void onBulletMissed(BulletMissedEvent event) {
    this.misses++;
  }

  @Override
  public void onBulletHitBullet(BulletHitBulletEvent event) {
    this.misses++;
  }

  @Override
  public void onRoundEnded(RoundEndedEvent event) {

    if (RedShift.DEBUG) {
      out.printf("my accuracy:              %f%n", this.getAccuracy());
      out.printf("estimated enemy accuracy: %f%n", this.targetInfo.getAccuracy());
    }
    RedShift.addAccuracy(NUM_VELOCITIES, this.getAccuracy());

    if (this.dodge) {
      RedShift.setEnemyAccuracyWithDodge(this.targetInfo.getAccuracy());
    }
    else {
      RedShift.setEnemyAccuracyWithoutDodge(this.targetInfo.getAccuracy());
    }
  }

  /**
   * Returns this robots accuracy.
   * @return this robots accuracy
   */
  public double getAccuracy() {
    return (double)this.hits / (double)(this.hits + this.misses);
  }
  /**
   * Draws debugging information.
   *
   * @param g graphics
   */
  @Override
  public void onPaint(Graphics2D g) {
    if (this.targetInfo == null) {
      return;
    }

    if (RedShift.DEBUG) {
      // draw preferred distance
      drawCircle(this.targetInfo.getX(), this.targetInfo.getY(),
          PREFERRED_DISTANCE - DISTANCE_BUFFER, Color.BLUE, g);
      drawCircle(this.targetInfo.getX(), this.targetInfo.getY(),
          PREFERRED_DISTANCE + DISTANCE_BUFFER, Color.BLUE, g);

      // draw max firing distance
      drawCircle(this.targetInfo.getX(), this.targetInfo.getY(),
          MAX_FIRING_DISTANCE, Color.RED, g);
    }
  }

  /**
   * Helper method to draw a circle.
   *
   * @param x x-coordinate of the center
   * @param y y-coordinate of the center
   * @param r radius
   * @param c color
   * @param g graphics
   */
  private void drawCircle(double x, double y, double r, Color c, Graphics2D g) {
    Color original = g.getColor();

    g.setColor(c);
    g.drawOval((int)(x - r), (int)(y - r), (int)(2 * r), (int)(2 * r));

    g.setColor(original);
  }

  /**
   * Adds a new (# velocities, accuracy) pair to the map of accuracies.
   * If the key already exists, only add the new value if it is worse than the existing value.
   * This will ensure that you only keep track of the worst performance.
   * @param num number of velocities used
   * @param acc accuracy
   */
  private static void addAccuracy(int num, double acc) {
    // if the key is not in the map
    // or if the key existing value is greater than the new value
    if (!RedShift.accuracies.containsKey(num) || RedShift.accuracies.get(num) > acc) {
      RedShift.accuracies.put(num, acc);
    }
  }

  /**
   * Returns the best number of velocities to keep track of.
   * @return the best number of velocities to keep track of.
   */
  private static int getBestNumVelocities() {
    // get weighted average of all past # velocities
    // the weight is equal to the accuracy
    double result = 0;
    double totalWeight = Double.MIN_VALUE;  // use small value to prevent divide by zero
    // iterate through each entry
    for (Map.Entry<Integer, Double> entry : RedShift.accuracies.entrySet()) {
      double weight = entry.getValue() * entry.getValue();
      result += weight * (double)entry.getKey();
      totalWeight += weight;
    }

    result /= totalWeight;
    return Math.max(1, (int)result);
  }

  /**
   * Sets the value of enemyAccuracyWithDodge to the maximum of the current value and the
   * specified new accuracy.
   * @param accuracy new accuracy
   */
  public static void setEnemyAccuracyWithDodge(double accuracy) {
    RedShift.enemyAccuracyWithDodge = Math.max(RedShift.enemyAccuracyWithDodge, accuracy);
  }

  /**
   * Sets the value of enemyAccuracyWithoutDodge to the maximum of the current value and the
   * specified new accuracy.
   * @param accuracy new accuracy
   */
  public static void setEnemyAccuracyWithoutDodge(double accuracy) {
    RedShift.enemyAccuracyWithoutDodge = Math.max(RedShift.enemyAccuracyWithoutDodge, accuracy);
  }
}