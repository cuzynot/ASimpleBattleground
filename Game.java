import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;
	private final double WIDTH_TO_HEIGHT;

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

	// current cursor
	private boolean cursor;

	// constructor
	public Game() {
		color = new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
		randomizeMap();

		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		defaultCursor = Cursor.getDefaultCursor();
		blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		// toggleCursor(false);

		SCREEN_WIDTH = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
		SCREEN_HEIGHT = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		WIDTH_TO_HEIGHT = SCREEN_WIDTH / SCREEN_HEIGHT;

		image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData(); // links pixels to image 

		// init JFrame
		setResizable(false);
		setTitle("A Simple Battleground");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		// make this frame full screen
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
		gd.setFullScreenWindow(this);

		// make cursor blank
		cursor = false;
		toggleCursor(cursor);

		// init Screen
		display = new Display(map, SCREEN_WIDTH, SCREEN_HEIGHT, color); // screen = new Screen(map, mapWidth, mapHeight, textures, 640, 480);

		// spawn the player
		spawnPlayer();
	}

	// public methods
	public void spawnPlayer() {
		// init Player
		int x = 0, y = 0;
		while (map[x][y] != 0) {
			x = (int)(Math.random() * map.length);
			y = (int)(Math.random() * map[0].length);
		}

		player = new Player(x, y, 1, 0, 0, -0.66, SCREEN_WIDTH, SCREEN_HEIGHT);
		PlayerKeyListener pkl = new PlayerKeyListener();
		PlayerMouseListener pml = new PlayerMouseListener();
		addKeyListener(pkl);
		addMouseListener(pml);

		// enter game loop
		run();
	}

	// private methods
	private void toggleCursor(boolean solid) {
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

	// game loop
	private void run() {
		while(true) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mouseMoved();

			update();
			display.update(player, pixels);

			render(); //displays to the screen unrestricted time
		}

	}

	// update camera view
	private void update() {
		// get player variables
		boolean forward = player.getForward();
		boolean left = player.getLeft();
		boolean back = player.getBack();
		boolean right = player.getRight();

		double x = player.getX();
		double y = player.getY();
		double xDir = player.getXDir();
		double yDir = player.getYDir();
		double speed = player.getSpeed();
		double rotation = player.getRotation();
		double xPlane = player.getXPlane();
		double yPlane = player.getYPlane();

		// move player
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

		player.setX(x);
		player.setY(y);
		player.setXDir(xDir);
		player.setYDir(yDir);
		player.setXPlane(xPlane);
		player.setYPlane(yPlane);
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy(); // used when rendering so the screen updates are smoother

		if(bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		if (player.getClickedRight()) { // if the player is scoped in
			int zoom = 200; // temp
			g.drawImage(image.getSubimage(zoom, (int)(zoom / WIDTH_TO_HEIGHT), image.getWidth() - zoom * 2, (int)(image.getHeight() - zoom / WIDTH_TO_HEIGHT * 2)), 0, 0, image.getWidth(), image.getHeight(), null);
		} else {
			g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		}

		bs.show();
	}

	private void mouseMoved() {
		if (player.getInGame()) {
			// get current x position of the cursor
			int curx = (int)(MouseInfo.getPointerInfo().getLocation().getX());
			// change rotational value according to how much the mouse has moved
			// in relation to the center of the screen
			player.setRotation(-(curx - SCREEN_WIDTH / 2) / 1000.0); // 1000 is an arbitrary value for tweaking sensitivity

			// use robot to move mouse back to the center of the screen
			player.getRobot().mouseMove(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
		}
	}

	//----------INNER CLASSES----------//
	// Mouse motion listener
	private class PlayerMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			int button = e.getButton();
			if (button == MouseEvent.BUTTON1) {
				player.setClickedLeft(true);
			} else if (button == MouseEvent.BUTTON3) {
				player.setClickedRight(true);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			int button = e.getButton();
			if (button == MouseEvent.BUTTON1) {
				player.setClickedLeft(false);
			} else if (button == MouseEvent.BUTTON3) {
				player.setClickedRight(false);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
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
				player.setForward(true);
			} else if (key == KeyEvent.VK_A){
				player.setLeft(true);
			} else if (key == KeyEvent.VK_S){
				player.setBack(true);
			} else if (key == KeyEvent.VK_D){
				player.setRight(true);
			} else if (key == KeyEvent.VK_R) {
				spawnPlayer();
			} else if (key == KeyEvent.VK_ESCAPE){
				player.setRotation(0);
				player.setInGame(!player.getInGame());

				cursor = !cursor;
				toggleCursor(cursor);
			}
		}

		@Override
		public void keyReleased (KeyEvent e){
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_W){
				player.setForward(false);
			} else if (key == KeyEvent.VK_A){
				player.setLeft(false);
			} else if (key == KeyEvent.VK_S){
				player.setBack(false);
			} else if (key == KeyEvent.VK_D){
				player.setRight(false);
			}
		}
	}
}