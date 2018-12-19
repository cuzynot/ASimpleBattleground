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

import data_structures.SimpleQueue;

public class Game extends JFrame {

	// run
	public static void main (String[] args){
		new Game();
	}

	// Camera
	private Player player;

	// Screen
	private Display display;

	// screen dimensions
	private int screenWidth;
	private int screenHeight;

	// images
	private BufferedImage image;
	private int[] pixels;
	private final int mapSize = 30;
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

	// random color;
	private Color color;

	// constructor
	public Game() {
		color = new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
		randomizeMap();

		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		defaultCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(10, 10), "default cursor");
		blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		toggleCursor(false);

		screenWidth = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
		screenHeight = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight());

		image = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData(); // links pixels to image 

		// init JFrame
		setResizable(false);
		setTitle("A Simple Battleground");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setUndecorated(true);
		setVisible(true);

		// init Screen
		display = new Display(map, screenWidth, screenHeight, color); // screen = new Screen(map, mapWidth, mapHeight, textures, 640, 480);

		// spawn the player
		spawnPlayer();

	}

	public void spawnPlayer() {
		// init Player
		int x = 0, y = 0;
		while (map[x][y] != 0) {
			x = (int)(Math.random() * mapSize);
			y = (int)(Math.random() * mapSize);
		}
		player = new Player(x, y, 1, 0, 0, -0.66, screenWidth, screenHeight, this);
		addKeyListener(player.getKeyListener());
		addMouseMotionListener(player.getMouseMotionListener());

		// enter game loop
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
		// instantiate map
		map = new int[mapSize][mapSize];

		// randomize the walls
		for (int i = 1; i < map.length - 1; i++) {
			for (int j = 1; j < map[0].length - 1; j++) {
				map[i][j] = (int)(Math.random() * 2) + 1; // either 1 (a wall) or 0 (open)
			}
		}

		//		// make sure there's a passage in the middle
		//		for (int i = 1; i < map.length - 1; i++) {
		//			map[map.length / 2][i] = 0;
		//			map[i][map.length / 2] = 0;
		//		}

		// make sure the exterior are walls
		for (int i = 0; i < map.length; i++) {
			map[0][i] = 1;
			map[map[0].length - 1][i] = 1;
		}
		for (int i = 0; i < map[0].length; i++) {
			map[i][0] = 1;
			map[i][map.length - 1] = 1;
		}

		int prevx = 1;
		int prevy = 1;

		for (int i = 1; i < map.length - 1; i++) {
			for (int j = 1; j < map[0].length - 1; j++) {
				if (map[i][j] == 0) {
					prevx = i;
					prevy = j;
				} else if (map[i][j] == 2) {
					for (int k = prevx; k < i; k++) {
						map[k][j] = 0;
					}
					for (int l = Math.min(prevy, j); l < Math.max(prevy, j); l++) {
						map[prevx][l] = 0;
					}

					bfs(i, j);
					prevx = i;
					prevy = j;
				}
			}
		}

		// print in console
		for (int i = 0; i < map.length; i++) {
			System.out.println(Arrays.toString(map[i]));
		}
	}

	private void bfs(int i, int j) {
		SimpleQueue<Integer> queue = new SimpleQueue<Integer>();
		queue.enqueue(i); // add x
		queue.enqueue(j); // and y coordinates of the staring point

		// bfs to make sure every cell in the map is connected
		while (!queue.isEmpty()) {
			int x = queue.dequeue();
			int y = queue.dequeue();

			// if current coordinate has not been visited yet
			if (map[x][y] == 2) {
				// make current cell blank and randomize adj cells
				map[x][y] = 0;

				// if any adj cell is open, they get added to the queue
				if (x - 1 > 0) {
					queue.enqueue(x - 1);
					queue.enqueue(y);
				}

				if (y - 1 > 0) {
					queue.enqueue(x);
					queue.enqueue(y - 1);
				}

				if (x + 1 < map.length) {
					queue.enqueue(x + 1);
					queue.enqueue(y);
				}

				if (y + 1 < map[0].length) {
					queue.enqueue(x);
					queue.enqueue(y + 1);
				}
			}
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
			display.update(player, pixels);

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