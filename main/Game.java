package main;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import data_structures.SimpleLinkedList;
import graphics.Display;
import player.Bullet;
import player.Player;
import player.builds.Assassin;
import player.builds.Guard;
import player.builds.Sniper;
import player.builds.Soldier;

public class Game extends JFrame {

	// Camera
	private Player player;

	// Screen
	private Display display;

	// screen dimensions
	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;

	// map
	private int[][] map;

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
	private SimpleLinkedList<Player> players;

	private static final int DELAY = 20;

	// constructor
	public Game(Client client) {
		setFocusable(true);
		requestFocusInWindow(true);

		this.client = client;
		this.color = new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
		this.SCREEN_WIDTH = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
		this.SCREEN_HEIGHT = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight());

		// init cursor
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		defaultCursor = Cursor.getDefaultCursor();
		blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		toggleCursor(false);

		// make cursor blank
		cursorShown = false;
		toggleCursor(cursorShown);

		// get player, players and map from client
		player = client.getPlayer();
		players = client.getPlayers();
		map = client.getMap();
		
		// make new display
		display = new Display(map, SCREEN_WIDTH, SCREEN_HEIGHT, color, player, players); // screen = new Screen(map, mapWidth, mapHeight, textures, 640, 480)

		// init JFrame
		add(display);
		addKeyListener(new GameKeyListener());
		addMouseListener(new GameMouseListener());
		setResizable(false);
		setUndecorated(true);
		setTitle("A Simple Battleground");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setVisible(true);

		// spawn the player
		spawnPlayer();

		Thread t = new Thread(new Runnable() {
			public void run() {
				loop();
			}
		});
		t.start();
	}

	// public methods
	public void spawnPlayer() {
		// init Player
		int x = 0, y = 0;
		while (map[x][y] != 0) {
			x = (int)(Math.random() * map.length);
			y = (int)(Math.random() * map[0].length);
		}
		
		// reset player build
		if (display.getCurBuild() == 0) {
			player.setBuild(new Assassin());
		} else if (display.getCurBuild() == 1) {
			player.setBuild(new Guard());
		} else if (display.getCurBuild() == 2) {
			player.setBuild(new Sniper());
		} else if (display.getCurBuild() == 3) {
			player.setBuild(new Soldier());
		}

		// reset player values
		player.setX(x);
		player.setY(y);
		player.setXDir(1);
		player.setYDir(0);
		player.setXPlane(0);
		player.setYPlane(-1);
		player.getBuild().setHealth(player.getBuild().getMaxHealth());
		player.getBuild().setAmmo(player.getBuild().getMaxAmmo());
		player.setReload(0);
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
	private void loop() {
		while (true) {
			playerStatus();
			if (player.inGame()) {
				updatePlayer();
				client.update();
			}

			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void playerStatus() {
		if (player.getBuild().getHealth() <= 0) {
			player.setRespawn(3000);
			player.setX(-1);
			player.setY(-1);
			player.getBuild().setHealth(player.getBuild().getMaxHealth());
		} else if (player.getRespawn() > 0) {
			player.setRespawn(player.getRespawn() - DELAY);
			if (player.getRespawn() <= 0) {
				spawnPlayer();
			}
		}
	}

	// update camera view
	private void updatePlayer() {
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
		if (forward) {
			if (map[(int)(x + xDir * speed)][(int)y] == 0) {
				x += xDir * speed;
			}
			if (map[(int)x][(int)(y + yDir * speed)] == 0) {
				y += yDir * speed;
			}
		}
		if (back) {
			if (map[(int)(x - xDir * speed)][(int)y] == 0) {
				x -= xDir * speed;
			}
			if (map[(int)x][(int)(y - yDir * speed)] == 0) {
				y -= yDir * speed;
			}
		}

		// reduced speed on strafing
		if (left) {
			if (map[(int)(x - xPlane * speed)][(int)y] == 0) {
				x -= xPlane * speed;
			}
			if (map[(int)x][(int)(y - yPlane * speed)] == 0) {
				y -= yPlane * speed;
			}
		}
		if (right) {
			if (map[(int)(x + xPlane * speed)][(int)y] == 0) {
				x += xPlane * speed;
			}
			if (map[(int)x][(int)(y + yPlane * speed)] == 0) {
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
		if (player.getReload() > 0) {
			player.setReload(player.getReload() - DELAY);
			if (player.getReload() <= 0) {
				player.getBuild().setAmmo(player.getBuild().getMaxAmmo());
			}
		}

		mouseMoved();
		mousePressed();

		for (int i = 0; i < player.getBullets().size(); i++) {
			Bullet bullet = player.getBullets().get(i);
			bullet.setX(bullet.getX() + bullet.getXDir() * 0.1);
			bullet.setY(bullet.getY() + bullet.getYDir() * 0.1);

			double bx = bullet.getX();
			double by = bullet.getY();

			if (map[(int)(bx)][(int)(by)] != 0 || bx < 0 || bx > map.length || by < 0 || by > map[0].length) {
				System.out.println("hit wall");
				player.getBullets().remove(i);
				i--;

			} else {
				int j = 0;
				boolean hit = false;
				while (!hit && j < players.size()) {
					if (bullet.collides(players.get(j))) {
						client.println("hit " + players.get(j).getName() + " " + player.getBuild().getDamage() + " " + player.getName());
						player.getBullets().remove(i);
						i--;

						System.out.println("hit " + players.get(j).getName());
						hit = true;
					}
					j++;
				}
			}
		}
	}

	private void mouseMoved() {
		// get current x position of the cursor
		int curx = (int)(MouseInfo.getPointerInfo().getLocation().getX());

		// change rotational value according to how much the mouse has moved
		// in relation to the center of the screen
		player.setRotation(-(curx - SCREEN_WIDTH / 2) / 3500.0); // 1000 is an arbitrary value for tweaking sensitivity

		// use robot to move mouse back to the center of the screen
		player.getRobot().mouseMove((curx + SCREEN_WIDTH / 2) / 2, SCREEN_HEIGHT / 2);
	}

	private void mousePressed() {
		if (player.getClickedLeft() && System.currentTimeMillis() - player.getLastFired() > player.getBuild().getFireRate() && player.getReload() <= 0) {
			Bullet bullet = new Bullet(player.getX(), player.getY(), player.getXDir(), player.getYDir());
			player.getBullets().add(bullet);
			player.getBuild().setAmmo(player.getBuild().getAmmo() - 1);
			if (player.getBuild().getAmmo() <= 0) {
				player.setReload(1000);
			}
			player.setLastFired(System.currentTimeMillis());
		}
	}

	//----------INNER CLASSES----------//
	// Mouse motion listener
	private class GameMouseListener implements MouseListener {

		@Override
		public void mousePressed(MouseEvent e) {
			if (player.inGame()) {
				int button = e.getButton();
				if (button == MouseEvent.BUTTON1) {
					player.setClickedLeft(true);
				} else if (button == MouseEvent.BUTTON3) {
					player.setClickedRight(true);
				}
			} else {
				double x = e.getPoint().getX();
				double y = e.getPoint().getY();
				if (display.getExit().clicked(x, y)) {
					client.disconnect();
					dispose();
				} else if (display.getPrevBuild().clicked(x, y) || display.getNextBuild().clicked(x, y)) {
					if (display.getPrevBuild().clicked(x, y)) {
						if (display.getCurBuild() > 0) {
							display.setCurBuild(display.getCurBuild() - 1);
						} else {
							display.setCurBuild(3);
						}
					} else if (display.getNextBuild().clicked(x, y)) {
						if (display.getCurBuild() < 3) {
							display.setCurBuild(display.getCurBuild() + 1);
						} else {
							display.setCurBuild(0);
						}
					}
				}	
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (player.inGame()) {
				int button = e.getButton();
				if (button == MouseEvent.BUTTON1) {
					player.setClickedLeft(false);
				} else if (button == MouseEvent.BUTTON3) {
					player.setClickedRight(false);
				}
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
	private class GameKeyListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {}

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_W) {
				player.setForward(true);
			} else if (key == KeyEvent.VK_A) {
				player.setLeft(true);
			} else if (key == KeyEvent.VK_S) {
				player.setBack(true);
			} else if (key == KeyEvent.VK_D) {
				player.setRight(true);
			} else if (key == KeyEvent.VK_R) {
				player.setReload(1000);
				// spawnPlayer();
			} else if (key == KeyEvent.VK_ESCAPE) {
				player.setRotation(0);
				player.setInGame(!player.inGame());

				cursorShown = !cursorShown;
				toggleCursor(cursorShown);
			} else if (key == KeyEvent.VK_Z) {
				System.out.println("this " + " xy " + player.getX() + " " + player.getY());
				for (int i = 0; i < players.size(); i++) {
					Player p = players.get(i);
					System.out.println(p.getName() + " xy " + p.getX() + " " + p.getY());
				}
			} else if (key == KeyEvent.VK_H) {
				player.getBuild().setHealth(player.getBuild().getHealth() - 50);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_W) {
				player.setForward(false);
			} else if (key == KeyEvent.VK_A) {
				player.setLeft(false);
			} else if (key == KeyEvent.VK_S) {
				player.setBack(false);
			} else if (key == KeyEvent.VK_D) {
				player.setRight(false);
			}
		}
	}
}