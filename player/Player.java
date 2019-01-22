/**
 * [Player].java
 * Player is a class to store
 * information about a player
 * @author      Yili Liu
 * @since       Dec.18.2018
 */
package player;
import java.awt.AWTException;
import java.awt.Robot;

import data_structures.SimpleLinkedList;
import player.builds.Build;

public class Player implements Moveable {

	// init vars
	private boolean clickedLeft, clickedRight; // mouse clicked
	private boolean left, right, forward, back; // keys pressed
	private boolean inGame;
	
	// cur reload respawn time left
	private int reload;
	private int respawn;

	private double x, y; // player positions
	private double xDir, yDir; // player directions vectors
	private double xPlane, yPlane; // farthest edge of the cinGamera's view
	private double rotation; // player cinGamera rotation
	
	// player build
	private Build build;
	
	// misc
	private int score;
	private String name;
	private Robot robot;
	private SimpleLinkedList<Bullet> bullets;
	private long lastFired;
	private String eliminator;

	/**
	 * Player
	 * constructor
	 * @param name, name of the player
	 * @param x, x pos of the player
	 * @param y, y pos of the player
	 * @param xd, x dir of the player
	 * @param yd, y dir of the player
	 * @param xp, x plane of the player
	 * @param yp, y plane of the player
	 */
	public Player(String name, double x, double y, double xd, double yd, double xp, double yp){
		// set values
		this.name = name;
		this.x = x;
		this.y = y;
		this.xDir = xd;
		this.yDir = yd;
		this.xPlane = xp;
		this.yPlane = yp;
		this.inGame = true;
		this.score = 0;
		this.bullets = new SimpleLinkedList<Bullet>();
		this.reload = 0;
		this.respawn = 0;
		this.eliminator = "";

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Player
	 * constructor
	 * @param name, name of the player
	 */
	public Player(String name) {
		// set values
		this.name = name;
		this.inGame = true;
		this.score = 0;
		this.bullets = new SimpleLinkedList<Bullet>();
		this.reload = 0;
		this.respawn = 0;
		this.eliminator = "";

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	/**
	 * getForward
	 * this method returns if the player is
	 * moving forward
	 * @return forward, if the player is moving forward
	 */
	public boolean getForward() {
		return forward;
	}
	
	/**
	 * getLeft
	 * this method returns if the player is
	 * moving left
	 * @return left, if the player is moving left
	 */
	public boolean getLeft() {
		return left;
	}

	/**
	 * getBack
	 * this method returns if the player is
	 * moving back
	 * @return back, if the player is moving back
	 */
	public boolean getBack() {
		return back;
	}

	/**
	 * getRight
	 * this method returns if the player is
	 * moving right
	 * @return right, if the player is moving right
	 */
	public boolean getRight() {
		return right;
	}

	/**
	 * getClickedLeft
	 * this method returns if the left
	 * mouse button is pressed
	 * @return clickedLeft, if the player clicking left
	 */
	public boolean getClickedLeft() {
		return clickedLeft;
	}

	/**
	 * getClickedRight
	 * this method returns if the right
	 * mouse button is pressed
	 * @return clickedRight, if the player clicking right
	 */
	public boolean getClickedRight() {
		return clickedRight;
	}

	/**
	 * inGame
	 * this method returns if the player is in game
	 * @return inGame, if the player is in game
	 */
	public boolean inGame() {
		return inGame;
	}

	/**
	 * getReload
	 * this method returns time left for the player to reload
	 * @return reload, time left for the player to reload
	 */
	public int getReload() {
		return reload;
	}
	
	/**
	 * getRespawn
	 * this method returns time left for the player to respawn
	 * @return respawn, time left for the player to respawn
	 */
	public int getRespawn() {
		return respawn;
	}
	
	/**
	 * getScore
	 * this method returns cur score of the player
	 * @return score, cur score of the player
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * getLastFired
	 * this method returns when the player last fired
	 * @return lastFired, when the player last fired
	 */
	public long getLastFired() {
		return lastFired;
	}
	
	/**
	 * getRotation
	 * this method returns cur rotation of player
	 * @return rotation, rotation of player
	 */
	public double getRotation() {
		return rotation;
	}

	@Override
	public double getX() {
		return x;
	}
	
	@Override
	public double getY() {
		return y;
	}

	@Override
	public double getXDir() {
		return xDir;
	}

	@Override
	public double getYDir() {
		return yDir;
	}

	/**
	 * getXPlane
	 * this method returns the x plane of the player
	 * @return xPlane, the x plane of the player
	 */
	public double getXPlane() {
		return xPlane;
	}

	/**
	 * getYPlane
	 * this method returns the y plane of the player
	 * @return yPlane, the y plane of the player
	 */
	public double getYPlane() {
		return yPlane;
	}
	
	/**
	 * getRobot
	 * this method returns the player's robot
	 * @return robot, the robot of the player
	 */
	public Robot getRobot() {
		return robot;
	}

	/**
	 * getName
	 * this method returns the name of the player
	 * @return name, name of player
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * getEliminator
	 * this method returns name of the eliminator of player
	 * @return eliminator, name of the eliminator of player
	 */
	public String getEliminator() {
		return eliminator;
	}

	/**
	 * getBullets
	 * this method returns a list of 
	 * currently active bullets
	 * @return bullets, active bullets
	 */
	public SimpleLinkedList<Bullet> getBullets() {
		return bullets;
	}
	
	/**
	 * getBuild
	 * this method returns the build of player
	 * @return build, build of player
	 */
	public Build getBuild() {
		return build;
	}
	
	/**
	 * setClickedLeft
	 * takes in a new value
	 * and sets it as new clickedLeft
	 * @param clickedLeft, new clickedLeft of player
	 */
	public void setClickedLeft(boolean clickedLeft) {
		this.clickedLeft = clickedLeft;
	}

	/**
	 * setClickedRight
	 * takes in a new value
	 * and sets it as new clickedRight
	 * @param clickedRight, new clickedLeft of player
	 */
	public void setClickedRight(boolean clickedRight) {
		this.clickedRight = clickedRight;
	}

	/**
	 * setForward
	 * takes in a new value
	 * and sets it as new forward
	 * @param forward, new forward of player
	 */
	public void setForward(boolean forward) {
		this.forward = forward;
	}

	/**
	 * setLeft
	 * takes in a new value
	 * and sets it as new left
	 * @param left, new left of player
	 */
	public void setLeft(boolean left) {
		this.left = left;
	}

	/**
	 * setBack
	 * takes in a new value
	 * and sets it as new back
	 * @param back, new back of player
	 */
	public void setBack(boolean back) {
		this.back = back;
	}

	/**
	 * setRight
	 * takes in a new value
	 * and sets it as new right
	 * @param right, new right of player
	 */
	public void setRight(boolean right) {
		this.right = right;
	}

	/**
	 * setInGame
	 * takes in a new value
	 * and sets it as new inGame
	 * @param inGame, new inGame of player
	 */
	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	/**
	 * setReload
	 * takes in a new value
	 * and sets it as new reload
	 * @param reload, new reload of player
	 */
	public void setReload(int reload) {
		this.reload = reload;
	}
	
	/**
	 * setRespawn
	 * takes in a new value
	 * and sets it as new respawn
	 * @param respawn, new respawn of player
	 */
	public void setRespawn(int respawn) {
		this.respawn = respawn;
	}
	
	/**
	 * setScore
	 * takes in a new value
	 * and sets it as new score
	 * @param score, new score of player
	 */
	public void setScore(int score) {
		this.score = score;
	}
	
	/**
	 * setLastFired
	 * takes in a new value
	 * and sets it as new lastFired
	 * @param lastFired, new lastFired of player
	 */
	public void setLastFired(long lastFired) {
		this.lastFired = lastFired;
	}

	/**
	 * setX
	 * takes in a new value
	 * and sets it as new x
	 * @param x, new x of player
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * setY
	 * takes in a new value
	 * and sets it as new y
	 * @param y, new y of player
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * setXDir
	 * takes in a new value
	 * and sets it as new xDir
	 * @param xDir, new xDir of player
	 */
	public void setXDir(double xDir) {
		this.xDir = xDir;
	}

	/**
	 * setYDir
	 * takes in a new value
	 * and sets it as new yDir
	 * @param yDir, new yDir of player
	 */
	public void setYDir(double yDir) {
		this.yDir = yDir;
	}

	/**
	 * setXPlane
	 * takes in a new value
	 * and sets it as new xPlane
	 * @param xPlane, new xPlane of player
	 */
	public void setXPlane(double xPlane) {
		this.xPlane = xPlane;
	}

	/**
	 * setYPlane
	 * takes in a new value
	 * and sets it as new yPlane
	 * @param yPlane, new yPlane of player
	 */
	public void setYPlane(double yPlane) {
		this.yPlane = yPlane;
	}

	/**
	 * setRotation
	 * takes in a new value
	 * and sets it as new rotation
	 * @param rotation, new rotation of player
	 */
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	/**
	 * setEliminator
	 * takes in a new value
	 * and sets it as new eliminator
	 * @param eliminator, new eliminator of player
	 */
	public void setEliminator(String eliminator) {
		this.eliminator = eliminator;
	}
	
	/**
	 * setBuild
	 * takes in a new value
	 * and sets it as new build
	 * @param build, new build of player
	 */
	public void setBuild(Build build) {
		this.build = build;
	}
}
