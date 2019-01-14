package main;
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
import java.util.ArrayList;

import javax.swing.JFrame;

import graphics.Display;

public class Game extends JFrame {

	// run
	public static void main (String[] args){
		new Game(new Client("localhost", 5001, "blah"));
	}

	// Camera
	private Player player;

	// Screen
	private Display display;

	// screen dimensions
	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;

	// images
	private BufferedImage image;
	private int[] pixels;
	private static int[][] map;

	// cursors
	private Cursor defaultCursor;
	private Cursor blankCursor;

	// random color;
	private Color color;

	// current cursor
	private boolean cursorShown;

	// client object
	private Client client;

	// list of players
	private ArrayList<Player> players;

	// constructor
	public Game(Client client) {
		this.client = client;

		color = new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));

		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		defaultCursor = Cursor.getDefaultCursor();
		blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		// toggleCursor(false);

		SCREEN_WIDTH = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
		SCREEN_HEIGHT = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight());

		image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData(); // links pixels to image 

		// init JFrame
		setResizable(false);
		setTitle("A Simple Battleground");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setUndecorated(true);
		setVisible(true);
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

		// make this frame full screen
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
		gd.setFullScreenWindow(this);

		// make cursor blank
		cursorShown = false;
		toggleCursor(cursorShown);

		players = client.getPlayers();
		map = client.getMap();

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

		player = new Player(client.getName(), x, y, 1, 0, 0, -1, "sniper"); // temp
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
			display.update(player, pixels, players);
			client.update(player);

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
		double speed = player.getBuild().getSpeed();
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
		if (left){
			if (map[(int)(x - xPlane * speed)][(int)y] == 0){
				x -= xPlane * speed;
			}
			if (map[(int)x][(int)(y - yPlane * speed)] == 0){
				y -= yPlane * speed;
			}
		}
		if (right){
			if (map[(int)(x + xPlane * speed)][(int)y] == 0){
				x += xPlane * speed;
			}
			if (map[(int)x][(int)(y + yPlane * speed)] == 0){
				y += yPlane * speed;
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
			int zoomW = (int)(image.getWidth() * player.getBuild().getZoom());
			int zoomH = (int)(image.getHeight() * player.getBuild().getZoom());
			BufferedImage sub = image.getSubimage(zoomW, zoomH, image.getWidth() - zoomW * 2, image.getHeight() - zoomH * 2);
			g.drawImage(sub, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
		} else {
			g.drawImage(image, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
		}

		// draw players
		// MAKE MORE EFFICIENT
		//		double m = player.getXDir() / player.getYDir();
		//		double b = player.getY() - m * player.getX();
		//


		bs.show();
	}

	private void mouseMoved() {
		if (player.getInGame()) {
			// get current x position of the cursor
			int curx = (int)(MouseInfo.getPointerInfo().getLocation().getX());

			// change rotational value according to how much the mouse has moved
			// in relation to the center of the screen
			player.setRotation(-(curx - SCREEN_WIDTH / 2) / 3500.0); // 1000 is an arbitrary value for tweaking sensitivity

			// use robot to move mouse back to the center of the screen
			player.getRobot().mouseMove((curx + SCREEN_WIDTH / 2) / 2, SCREEN_HEIGHT / 2);
		}
	}

	//----------INNER CLASSES----------//
	// Mouse motion listener
	private class PlayerMouseListener implements MouseListener {

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
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
	}

	// Key Listener
	private class PlayerKeyListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {}

		@Override
		public void keyPressed(KeyEvent e) {
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

				cursorShown = !cursorShown;
				toggleCursor(cursorShown);
			} else if (key == KeyEvent.VK_Z) {
				System.out.println("this " + " xy " + player.getX() + " " + player.getY());
				for (Player p : players) {
					System.out.println(p.getName() + " xy " + p.getX() + " " + p.getY());
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
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