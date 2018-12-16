package Graphics;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Player implements KeyListener, MouseMotionListener{

	private double xPos, yPos; // player positions
	private double xDir, yDir; // player directions vectors
	private double xPlane, yPlane; // farthest edge of the camera's view

	private boolean left, right, forward, back; // keys pressed
	private double speed = 0.07;
	private double rotation = 0.05;

	Robot robot;
	boolean inGame = true;
	
	private int screenWidth;
	private int screenHeight;

	// constructor
	public Player (double x, double y, double xd, double yd, double xp, double yp, int sw, int sh){	
		xPos = x;
		yPos = y;
		xDir = xd;
		yDir = yd;
		xPlane = xp;
		yPlane = yp;
		screenWidth = sw;
		screenHeight = sh;

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	// Keys
	@Override
	public void keyTyped (KeyEvent e){ }

	@Override
	public void keyPressed (KeyEvent e){
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_W){
			forward = true;
		} else if (key == KeyEvent.VK_LEFT){
			left = true;
		} else if (key == KeyEvent.VK_S){
			back = true;
		} else if (key == KeyEvent.VK_RIGHT){
			right = true;
		} else if (key == KeyEvent.VK_ESCAPE){
			inGame = !inGame;
		}
	}

	@Override
	public void keyReleased (KeyEvent e){
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_W){
			forward = false;
		} else if (key == KeyEvent.VK_LEFT){
			left = false;
		} else if (key == KeyEvent.VK_S){
			back = false;
		} else if (key == KeyEvent.VK_RIGHT){
			right = false;
		}
	}

	// Mouse motion listener
	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (inGame) {
			int curx = e.getX();

			if (curx > screenWidth + 10) {
				right = true;
				left = false;
				rotation = -(curx - screenWidth / 2) / 300.0;
			} else if (curx < screenWidth - 10){
				right = false;
				left = true;
				rotation = -(curx - screenWidth / 2) / 300.0;
			} else {
				right = false;
				left = false;
				rotation = 0;
			}

			robot.mouseMove(screenWidth / 2, screenHeight / 2);
		}
	}
	
	// getters
	public double getXPos() {
		return xPos;
	}
	
	public double getYPos() {
		return yPos;
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

	// update camera view
	public void update(int[][] map) {
		//		System.out.println("xpos " + xPos);
		//		System.out.println("ypos " + yPos);

		if (forward){
			if (map[(int)(xPos + xDir * speed)][(int)yPos] == 0){
				xPos += xDir * speed;
			}
			if (map[(int)xPos][(int)(yPos + yDir * speed)] == 0){
				yPos += yDir * speed;
			}
		}
		if (back){
			if (map[(int)(xPos - xDir * speed)][(int)yPos] == 0){
				xPos -= xDir * speed;
			}
			if (map[(int)xPos][(int)(yPos - yDir * speed)] == 0){
				yPos -= yDir * speed;
			}
		}

		// camera rotation
		double oldxDir = xDir;
		xDir = xDir * Math.cos(rotation) - yDir * Math.sin(rotation);
		yDir = oldxDir * Math.sin(rotation) + yDir * Math.cos(rotation);

		double oldxPlane = xPlane;
		xPlane = xPlane * Math.cos(rotation) - yPlane * Math.sin(rotation);
		yPlane = oldxPlane * Math.sin(rotation) + yPlane * Math.cos(rotation);
	}

}
