import java.awt.AWTException;
import java.awt.Robot;

public class Player {

	private double x, y; // player positions
	private double xDir, yDir; // player directions vectors
	private double xPlane, yPlane; // farthest edge of the camera's view

	private boolean clickedLeft, clickedRight; // mouse clicked
	private boolean left, right, forward, back; // keys pressed
	private double speed = 0.1;
	private double rotation; // = 0.05;

	Robot robot;
	boolean inGame = true;

	// constructor
	public Player (double x, double y, double xd, double yd, double xp, double yp, int sw, int sh){	
		this.x = x;
		this.y = y;
		this.xDir = xd;
		this.yDir = yd;
		this.xPlane = xp;
		this.yPlane = yp;

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
	
	public boolean getInGame() {
		return inGame;
	}
	
	public double getSpeed() {
		return speed;
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
	
	public Robot getRobot() {
		return robot;
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
}
