import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Player {

	private double x, y; // player positions
	private double xDir, yDir; // player directions vectors
	private double xPlane, yPlane; // farthest edge of the camera's view

	private boolean left, right, forward, back; // keys pressed
	private double speed = 0.1;
	private double rotation; // = 0.05;

	Robot robot;
	boolean inGame = true;

	private int screenWidth;
	private int screenHeight;

	private PlayerMouseMotionListener pmml;
	private PlayerKeyListener pkl;
	private Game game;

	// constructor
	public Player (double x, double y, double xd, double yd, double xp, double yp, int sw, int sh, Game game){	
		this.x = x;
		this.y = y;
		this.xDir = xd;
		this.yDir = yd;
		this.xPlane = xp;
		this.yPlane = yp;
		this.screenWidth = sw;
		this.screenHeight = sh;
		this.game = game;

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		pmml = new PlayerMouseMotionListener();
		pkl = new PlayerKeyListener();
	}

	// getters
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

	public PlayerMouseMotionListener getMouseMotionListener() {
		return pmml;
	}

	public PlayerKeyListener getKeyListener() {
		return pkl;
	}

	// update camera view
	public void update(int[][] map) {
		// player movements
		int counter = 0;
		if (forward || back) {
			counter++;
		}
		if (left || right) {
			counter++;
		}

		// if pressing 2 buttons simultaneously
		if (counter == 2) {

		}

		if (forward){
			if (map[(int)(x + xDir * speed)][(int)y] == 0){
				x += xDir * speed;
			}
			if (map[(int)x][(int)(y + yDir * speed)] == 0){
				y += yDir * speed;
			}
		}
		if (back){
			if (map[(int)(x - xDir * speed)][(int)y] == 0){
				x -= xDir * speed;
			}
			if (map[(int)x][(int)(y - yDir * speed)] == 0){
				y -= yDir * speed;
			}
		}
		// reduced speed on strafing
		if (left) {
			if (map[(int)(x - yDir * speed / 1.5)][(int)(y)] == 0){
				x -= yDir * speed / 1.5;
			}
			if (map[(int)x][(int)(y + xDir * speed / 1.5)] == 0){
				y += xDir * speed / 1.5;
			}
		}
		if (right) {
			if (map[(int)(x + yDir * speed / 1.5)][(int)y] == 0){
				x += yDir * speed / 1.5;
			}
			if (map[(int)x][(int)(y - xDir * speed / 1.5)] == 0){
				y -= xDir * speed / 1.5;
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

	//----------INNER CLASSES----------//
	// Mouse motion listener
	private class PlayerMouseMotionListener implements MouseMotionListener {
		@Override
		public void mouseDragged(MouseEvent e) {
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (inGame) {
				// change rotational value according to how much the mouse has moved
				// in relation to the center of the screen
				int curx = e.getX();
				rotation = -(curx - screenWidth / 2) / 175.0; // 175 is an arbitrary value for tweaking sensitivity

				// use robot to move mouse back to the center of the screen
				robot.mouseMove(screenWidth / 2, screenHeight / 2);
			}
		}
	}

	// Key Listener
	private class PlayerKeyListener implements KeyListener {
		@Override
		public void keyTyped (KeyEvent e){ }

		@Override
		public void keyPressed (KeyEvent e){
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_W){
				forward = true;
			} else if (key == KeyEvent.VK_A){
				left = true;
			} else if (key == KeyEvent.VK_S){
				back = true;
			} else if (key == KeyEvent.VK_D){
				right = true;
			} else if (key == KeyEvent.VK_ESCAPE){
				rotation = 0;
				inGame = !inGame;
				game.toggleCursor(true);
			}
		}

		@Override
		public void keyReleased (KeyEvent e){
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_W){
				forward = false;
			} else if (key == KeyEvent.VK_A){
				left = false;
			} else if (key == KeyEvent.VK_S){
				back = false;
			} else if (key == KeyEvent.VK_D){
				right = false;
			}
		}
	}
}
