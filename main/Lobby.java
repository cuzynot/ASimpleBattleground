package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import graphics.Button;
import graphics.Display;
import graphics.Field;
import player.Player;
import player.builds.Soldier;

public class Lobby {

	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;
	private final double ROTATE_SPEED;

	//	private int counter;
	//	private boolean side;

	private Client client;
	// private Game game;

	private BufferedImage image;
	private Player player;

	private JFrame frame;
	private LobbyPanel panel;

	private Field ip;
	private Field port;
	private Field name;
	private Button enter;
	private Button exit;
	
	private int curString;
	private int counter;
	private Color color;
	private Color alt;
	private Display display;

	private LobbyKeyListener lkl;
	private LobbyMouseListener lml;

	public static void main(String[] args) {
		new Lobby();
	}

	Lobby() {
		// set var values
		SCREEN_WIDTH = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
		SCREEN_HEIGHT = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		ROTATE_SPEED = 0.02;
		ip = new Field(SCREEN_WIDTH / 3, SCREEN_HEIGHT / 2 - SCREEN_HEIGHT / 16, SCREEN_WIDTH * 2 / 3, SCREEN_HEIGHT / 2);
		port = new Field(SCREEN_WIDTH / 3, SCREEN_HEIGHT / 2, SCREEN_WIDTH * 2 / 3, SCREEN_HEIGHT / 2 + SCREEN_HEIGHT / 16);
		name = new Field(SCREEN_WIDTH / 3, SCREEN_HEIGHT / 2 + SCREEN_HEIGHT / 16, SCREEN_WIDTH * 2 / 3, SCREEN_HEIGHT / 2 + SCREEN_HEIGHT / 8);
		enter = new Button(SCREEN_WIDTH / 2 - SCREEN_HEIGHT / 16, (int)(SCREEN_HEIGHT / 1.2) - SCREEN_HEIGHT / 32, SCREEN_WIDTH / 2 + SCREEN_HEIGHT / 16, (int)(SCREEN_HEIGHT / 1.2) + SCREEN_HEIGHT / 32, "ENTER");
		exit = new Button(SCREEN_WIDTH - SCREEN_HEIGHT / 32, 0, SCREEN_WIDTH, SCREEN_HEIGHT / 32, "");
		curString = 0;
		counter = 0;
		color = new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
		alt = new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));

		int[][] map = {
				{1, 1, 1, 1, 1},
				{1, 0, 0, 0, 1},
				{1, 0, 0, 0, 1},
				{1, 0, 0, 0, 1},
				{1, 1, 1, 1, 1},
		};
		player = new Soldier("null", 2.5, 2.5, 1, 0, 0, -1);
		display = new Display(map, SCREEN_WIDTH, SCREEN_HEIGHT, color, player, null);
		image = display.getImage();
		
		// make new panel
		panel = new LobbyPanel();

		// listeners
		lkl = new LobbyKeyListener();
		lml = new LobbyMouseListener();

		// configure frame
		frame = new JFrame();
		frame.add(panel);
		frame.addKeyListener(lkl);
		frame.addMouseListener(lml);
		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		while (true) {
			updatePlayer();

			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void updatePlayer() {
		// camera rotation
		double xDir = player.getXDir();
		double yDir = player.getYDir();
		double xPlane = player.getXPlane();
		double yPlane = player.getYPlane();

		double oldxDir = xDir;
		xDir = xDir * Math.cos(ROTATE_SPEED) - yDir * Math.sin(ROTATE_SPEED);
		yDir = oldxDir * Math.sin(ROTATE_SPEED) + yDir * Math.cos(ROTATE_SPEED);

		double oldxPlane = xPlane;
		xPlane = xPlane * Math.cos(ROTATE_SPEED) - yPlane * Math.sin(ROTATE_SPEED);
		yPlane = oldxPlane * Math.sin(ROTATE_SPEED) + yPlane * Math.cos(ROTATE_SPEED);

		player.setXDir(xDir);
		player.setYDir(yDir);
		player.setXPlane(xPlane);
		player.setYPlane(yPlane);
	}
	
	private void enterGame() {
		System.out.println("connecting to " + ip.getString() + " " + port.getString() + " " + name.getString());
		client = new Client(ip.getString(), Integer.parseInt(port.getString()), name.getString());
		//				client = new Client("localhost", 5001, "asdfoaj");
		frame.dispose();
		Thread t = new Thread(new Runnable() {
			public void run() {
				new Game(client);
			}
		});
		t.start();
	}

	private class LobbyKeyListener implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();

			String s = "";
			if (curString == 0) {
				s = ip.getString();
			} else if (curString == 1) {
				s = port.getString();
			} else if (curString == 2) {
				s = name.getString();
			}

			if (key == KeyEvent.VK_BACK_SPACE) {
				if (s.length() > 0) {
					if (curString == 0) {
						ip.setString(s.substring(0, s.length() - 1));
					} else if (curString == 1) {
						port.setString(s.substring(0, s.length() - 1));
					} else if (curString == 2) {
						name.setString(s.substring(0, s.length() - 1));
					}
				}
			} else if (key == KeyEvent.VK_ENTER) {
				enterGame();
			} else if (key != KeyEvent.VK_SPACE) {
				// } else if ((key != KeyEvent.VK_SHIFT) && (key != KeyEvent.VK_CONTROL) && (key != KeyEvent.VK_ALT)){
				char c = e.getKeyChar();
				if (s.length() < Field.getMaxLength()) {
					if (curString == 0) {
						ip.setString((s + c).toLowerCase());
					} else if (curString == 1) {
						port.setString((s + c).toLowerCase());
					} else if (curString == 2) {
						name.setString((s + c).toLowerCase());
					}
				}
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {}
		@Override
		public void keyReleased(KeyEvent e) {}
	}

	private class LobbyMouseListener implements MouseListener {
		@Override
		public void mousePressed(MouseEvent e) {
			double x = e.getPoint().getX();
			double y = e.getPoint().getY();

			if (ip.clicked(x, y)) {
				counter = 0;
				curString = 0;
			} else if (port.clicked(x, y)) {
				counter = 0;
				curString = 1;
			} else if (name.clicked(x, y)) {
				counter = 0;
				curString = 2;
			} else if (enter.clicked(x, y)) {
				enterGame();
			} else if (exit.clicked(x, y)) {
				System.exit(0);
			}

		}

		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
	}

	private class LobbyPanel extends JPanel {
		private Font fields;
		private Font logo;
		private int ipStringY;
		private int portStringY;
		private int nameStringY;
		private final int OFFSET;

		LobbyPanel() {
			OFFSET = 10;
			logo = new Font("Helvetica", Font.BOLD | Font.ITALIC, SCREEN_WIDTH / 15);
			fields = new Font("Lora", Font.PLAIN, SCREEN_WIDTH / 40);
			ipStringY = (ip.getY1() + ip.getY2()) / 2 + fields.getSize() / 2;
			portStringY = (port.getY1() + port.getY2()) / 2 + fields.getSize() / 2;
			nameStringY = (name.getY1() + name.getY2()) / 2 + fields.getSize() / 2;
		}

		public void paintComponent(Graphics g) {
			display.update();

			// draw the background rotating image
			g.drawImage(image, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);

			// draw the text fields' bounds
			g.setColor(Color.WHITE);
			g.drawLine(ip.getX1(), ip.getY1(), ip.getX2(), ip.getY1());
			g.drawLine(port.getX1(), port.getY1(), port.getX2(), port.getY1());
			g.drawLine(name.getX1(), name.getY1(), name.getX2(), name.getY1());
			g.drawLine(name.getX1(), name.getY2(), name.getX2(), name.getY2());

			// draw current text field
			g.setColor(Color.WHITE);
			if (curString == 0) {
				g.fillRect(SCREEN_WIDTH / 2 - counter, ip.getY1() - OFFSET, counter * 2, OFFSET * 2);
				g.fillRect(SCREEN_WIDTH / 2 - counter, ip.getY2() - OFFSET, counter * 2, OFFSET * 2);
			} else if (curString == 1) {
				g.fillRect(SCREEN_WIDTH / 2 - counter, port.getY1() - OFFSET, counter * 2, OFFSET * 2);
				g.fillRect(SCREEN_WIDTH / 2 - counter, port.getY2() - OFFSET, counter * 2, OFFSET * 2);
			} else if (curString == 2) {
				g.fillRect(SCREEN_WIDTH / 2 - counter, name.getY1() - OFFSET, counter * 2, OFFSET * 2);
				g.fillRect(SCREEN_WIDTH / 2 - counter, name.getY2() - OFFSET, counter * 2, OFFSET * 2);
			}

			// draw the enter button
			// g.fillOval(SCREEN_WIDTH / 2, (int)(SCREEN_HEIGHT / 1.5), SCREEN_WIDTH / 50, SCREEN_WIDTH / 50);
			g.setColor(alt);
			g.setFont(fields);
			g.drawString(enter.getString(), enter.getX1(), enter.getY2() - OFFSET);
			
			// draw the exit button
			g.setColor(alt);
			g.fillRect(exit.getX1(), exit.getY1(), exit.getX2() - exit.getX1(),  exit.getY2() - exit.getY1());
			g.setColor(color);
			g.drawLine(exit.getX1(), exit.getY1(), exit.getX2(),  exit.getY2());
			g.drawLine(exit.getX2(), exit.getY1(), exit.getX1(),  exit.getY2());

			// draw the ip, port and name strings
			g.setColor(alt);
			if (curString == 0) {
				g.drawString(" ip address:", SCREEN_WIDTH / 10, ipStringY);
			} else if (curString == 1) {
				g.drawString("port number:", SCREEN_WIDTH / 10, portStringY);
			} else if (curString == 2) {
				g.drawString("   username:", SCREEN_WIDTH / 10, nameStringY);
			}
			g.drawString(ip.getString(), ip.getX1(), ipStringY);
			g.drawString(port.getString(), port.getX1(), portStringY);
			g.drawString(name.getString(), name.getX1(), nameStringY);

			// draw logo
			g.setColor(alt);
			g.setFont(logo);
			g.drawString("A SIMPLE BATTLEGROUND", SCREEN_WIDTH / 32, SCREEN_HEIGHT / 5);

			// increment counter
			if (counter < SCREEN_WIDTH / 2 - ip.getX1()) {
				counter += 40;
			}

			repaint();
		}
	}
}
