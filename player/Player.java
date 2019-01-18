package player;
import java.awt.AWTException;
import java.awt.Robot;

import data_structures.SimpleLinkedList;

public class Player implements Moveable{

	private boolean clickedLeft, clickedRight; // mouse clicked
	private boolean left, right, forward, back; // keys pressed
	private boolean inGame;
	private boolean reloading;
	private boolean respawning;

	private double x, y; // player positions
	private double xDir, yDir; // player directions vectors
	private double xPlane, yPlane; // farthest edge of the camera's view
	private double rotation;

	private String name;
	private Robot robot;

	private SimpleLinkedList<Bullet> bullets;
	
	protected int ammo;
	protected int maxAmmo;
	protected double health;
	protected double maxHealth;
	protected double damage;
	protected double speed;
	protected double fireRate;
	protected double zoom;

	// constructor
	public Player(String name, double x, double y, double xd, double yd, double xp, double yp){
		this.name = name;
		this.x = x;
		this.y = y;
		this.xDir = xd;
		this.yDir = yd;
		this.xPlane = xp;
		this.yPlane = yp;
		this.inGame = true;
		this.bullets = new SimpleLinkedList<Bullet>();

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public Player(String name) {
		this.name = name;
		this.inGame = true;
		this.bullets = new SimpleLinkedList<Bullet>();

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	// getters
	public boolean getForward() {
		return forward;
	}

	public boolean getLeft() {
		return left;
	}

	public boolean getBack() {
		return back;
	}

	public boolean getRight() {
		return right;
	}

	public boolean getClickedLeft() {
		return clickedLeft;
	}

	public boolean getClickedRight() {
		return clickedRight;
	}

	public boolean inGame() {
		return inGame;
	}

	public boolean reloading() {
		return reloading;
	}
	
	public boolean respawning() {
		return respawning;
	}

	public int getAmmo() {
		return ammo;
	}
	
	public int getMaxAmmo() {
		return maxAmmo;
	}

	public double getRotation() {
		return rotation;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getXDir() {
		return xDir;
	}

	public double getYDir() {
		return yDir;
	}

	public double getXPlane() {
		return xPlane;
	}

	public double getYPlane() {
		return yPlane;
	}

	public double getHealth() {
		return health;
	}
	
	public double getMaxHealth() {
		return maxHealth;
	}
	
	public double getDamage() {
		return damage;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public double getFireRate() {
		return health;
	}
	
	public double getZoom() {
		return zoom;
	}
	
	public Robot getRobot() {
		return robot;
	}

	public String getName() {
		return name;
	}

	public SimpleLinkedList<Bullet> getBullets(){
		return bullets;
	}

	// setters
	public void setClickedLeft(boolean clickedLeft) {
		this.clickedLeft = clickedLeft;
	}

	public void setClickedRight(boolean clickedRight) {
		this.clickedRight = clickedRight;
	}

	public void setForward(boolean forward) {
		this.forward = forward;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setBack(boolean back) {
		this.back = back;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	public void setIsReloading(boolean reloading) {
		this.reloading = reloading;
	}
	
	public void setIsRespawning(boolean respawning) {
		this.respawning = respawning;
	}

	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setXDir(double xDir) {
		this.xDir = xDir;
	}

	public void setYDir(double yDir) {
		this.yDir = yDir;
	}

	public void setXPlane(double xPlane) {
		this.xPlane = xPlane;
	}

	public void setYPlane(double yPlane) {
		this.yPlane = yPlane;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public void setHealth(double health) {
		this.health = health;
	}

}
