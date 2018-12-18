import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

import javax.swing.JFrame;

public class Game extends JFrame {

	// run
	public static void main (String[] args){
		new Game();
	}

	// Camera
	private Player player;

	// Screen
	private Display screen;

	// screen dimensions
	private int screenWidth;
	private int screenHeight;

	// images
	private BufferedImage image;
	private int[] pixels;
	private final int mapSize = 15;
	private static int[][] map;
	//	= {
	//			{1,1,1,1,1,1,1,1,2,2,2,2,2,2,2},
	//			{1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
	//			{1,0,3,3,3,3,3,0,0,0,0,0,0,0,2},
	//			{1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
	//			{1,0,3,0,0,0,3,0,2,2,2,0,2,2,2},
	//			{1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
	//			{1,0,3,3,0,3,3,0,2,0,0,0,0,0,2},
	//			{1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
	//			{1,1,1,1,1,1,1,1,4,4,4,0,4,4,4},
	//			{1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
	//			{1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
	//			{1,0,0,2,0,0,1,4,0,3,3,3,3,0,4},
	//			{1,0,0,0,0,0,1,4,0,3,3,3,3,0,4},
	//			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
	//			{1,1,1,1,1,1,1,4,4,4,4,4,4,4,4}
	//	};

	// cursors
	private Cursor defaultCursor;
	private Cursor blankCursor;

	// constructor
	public Game() {
		randomizeMap();

		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		defaultCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(10, 10), "default cursor");
		blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
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
		player = new Player(7, 7, 1, 0, 0, -0.66, screenWidth, screenHeight, this);
		addKeyListener(player.getKeyListener());
		addMouseMotionListener(player.getMouseMotionListener());

		// init Screen
		screen = new Display(map, screenWidth, screenHeight); // screen = new Screen(map, mapWidth, mapHeight, textures, 640, 480);

		run();
	}

	// public methods
	public void toggleCursor(boolean solid) {
		Cursor cursor;

		if (solid) {
			cursor = defaultCursor;
		} else {
			// Create a new blank cursor.
			cursor = blankCursor;
		}
		getContentPane().setCursor(cursor);
	}

	private void randomizeMap() {
		map = new int[mapSize][mapSize];

		// randomize the walls
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				map[i][j] = (int)(Math.random() * 2);
			}
		}
		
		// make sure there's a passage in the middle
		for (int i = 0; i < map.length; i++) {
			map[map.length / 2][i] = 0;
			map[i][map.length / 2] = 0;
		}
		
		// make sure the exterior are walls
		for (int i = 0; i < map.length; i++) {
			map[0][i] = 1;
			map[map[0].length - 1][i] = 1;
		}
		for (int i = 0; i < map[0].length; i++) {
			map[i][0] = 1;
			map[i][map.length - 1] = 1;
		}

		for (int i = 0; i < map.length; i++) {
			System.out.println(Arrays.toString(map[i]));
		}

	}

	// required for implementation
	private void run() {

		while(true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			player.update(map);
			screen.update(player, pixels);

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