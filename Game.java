import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import Graphics.Player;
import Graphics.Screen;

public class Game extends JFrame {

	// run
	public static void main (String[] args){
		new Game();
	}

	// Camera
	private Player camera;

	// Screen
	private Screen screen;

	// screen dimensions
	private int screenWidth;
	private int screenHeight;

	// images
	private BufferedImage image;
	public int[] pixels;
	public static int[][] map = {
			{1,1,1,1,1,1,1,1,2,2,2,2,2,2,2},
			{1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
			{1,0,3,3,3,3,3,0,0,0,0,0,0,0,2},
			{1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
			{1,0,3,0,0,0,3,0,2,2,2,0,2,2,2},
			{1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
			{1,0,3,3,0,3,3,0,2,0,0,0,0,0,2},
			{1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
			{1,1,1,1,1,1,1,1,4,4,4,0,4,4,4},
			{1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
			{1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
			{1,0,0,2,0,0,1,4,0,3,3,3,3,0,4},
			{1,0,0,0,0,0,1,4,0,3,3,3,3,0,4},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
			{1,1,1,1,1,1,1,4,4,4,4,4,4,4,4}
	};

	// constructor
	public Game() {
		toggleCursor(false);

		screenWidth = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
		screenHeight = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight());

		image = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		
		// init jframe
		setSize(screenWidth, screenHeight);
		setResizable(false);
		setTitle("3D Test");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.black);
		setLocationRelativeTo(null);
		setVisible(true);

		// init Camera
		camera = new Player(4.5, 4.5, 1, 0, 0, -0.66, screenWidth, screenHeight);
		addKeyListener(camera);
		addMouseMotionListener(camera);

		// init Screen
		screen = new Screen(map, screenWidth, screenHeight); // screen = new Screen(map, mapWidth, mapHeight, textures, 640, 480);
		
		run();
	}
	
	// public methods

	public void toggleCursor(boolean solid) {
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor cursor;

		if (solid) {
			cursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(1, 1), "default cursor");
		} else {
			// Create a new blank cursor.
			cursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		}
		getContentPane().setCursor(cursor);
	}

	// required for implementation
	private void run() {

		while(true) {
			camera.update(map);
			screen.update(camera, pixels);

			render(); //displays to the screen unrestricted time
		}

	}
	
	// private methods
	
	private void render() {
		BufferStrategy bs = getBufferStrategy(); // used when rendering so the screen updates are smoother

		if(bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		bs.show();
	}
}