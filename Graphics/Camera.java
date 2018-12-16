package Graphics;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Camera implements KeyListener, MouseMotionListener{

	public double xPos, yPos, // player positions
	xDir, yDir, // player directions vectors
	xPlane, yPlane; // farthest edge of the camera's view

	public boolean left, right, forward, back; // keys pressed
	public double speed = 0.05;
	public double rotation = 0.05;

	int prevx = 100;
	int prevy;

	Robot robot;
	boolean inGame = true;

	// constructor
	public Camera (double x, double y, double xd, double yd, double xp, double yp){	
		xPos = x;
		yPos = y;
		xDir = xd;
		yDir = yd;
		xPlane = xp;
		yPlane = yp;

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	// Keys
	public void keyTyped (KeyEvent e){ }

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

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {

		if (inGame) {
			int curx = e.getX();

			if (curx > 305) {
				right = true;
				left = false;
				rotation = -(curx - 305) / 300.0;
			} else if (curx < 295){
				right = false;
				left = true;
				rotation = -(curx - 305) / 300.0;
			} else {
				right = false;
				left = false;
				rotation = 0;
			}
			prevx = curx;

			robot.mouseMove(300, 300);
		}
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

		//		if (left){
		//			double oldxDir=xDir;
		//			xDir = xDir * Math.cos(rotationSpeed) - yDir * Math.sin(rotationSpeed);
		//			yDir = oldxDir * Math.sin(rotationSpeed) + yDir * Math.cos(rotationSpeed);
		//
		//			double oldxPlane = xPlane;
		//			xPlane = xPlane * Math.cos(rotationSpeed) - yPlane * Math.sin(rotationSpeed);
		//			yPlane = oldxPlane * Math.sin(rotationSpeed) + yPlane * Math.cos(rotationSpeed);
		//		}
	}

}
