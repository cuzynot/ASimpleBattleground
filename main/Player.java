package main;
import java.awt.AWTException;
import java.awt.Robot;

import builds.Assassin;
import builds.Build;
import builds.Guard;
import builds.Sniper;
import builds.Soldier;

public class Player {

	private boolean clickedLeft, clickedRight; // mouse clicked
	private boolean left, right, forward, back; // keys pressed
	
	private double x, y; // player positions
	private double xDir, yDir; // player directions vectors
	private double xPlane, yPlane; // farthest edge of the camera's view
	private double rotation;
	
	private String name;
	private Robot robot;
	private boolean inGame;
	private Build build;

	// constructor
	public Player(String name, double x, double y, double xd, double yd, double xp, double yp, String build){
		this.name = name;
		this.x = x;
		this.y = y;
		this.xDir = xd;
		this.yDir = yd;
		this.xPlane = xp;
		this.yPlane = yp;
		this.inGame = true;
		
		if (build.equals("assassin")) {
			this.build = new Assassin();
		} else if (build.equals("guard")) {
			this.build = new Guard();
		} else if (build.equals("sniper")) {
			this.build = new Sniper();
		} else {
			this.build = new Soldier();
		}

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public Player(String name) {
		this.name = name;
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
	
	public boolean getInGame() {
		return inGame;
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
	
	public String getName() {
		return name;
	}
	
	public Build getBuild() {
		return build;
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
