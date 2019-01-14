package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;
import javax.swing.JPanel;

import graphics.Display;

public class Lobby extends JFrame {

	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;
	private final double ROTATE_SPEED;

	//	private int counter;
	//	private boolean side;
	
	private Client client;
	private Game game;

	private BufferedImage image;
	private int[] pixels;
	private Player player;

	private LobbyPanel panel;

	private Field ip;
	private Field port;
	private Field name;
	private int curString;

	public static void main(String[] args) {
		new Lobby();
	}

	Lobby() {
		// set var values
		SCREEN_WIDTH = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
		SCREEN_HEIGHT = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		ROTATE_SPEED = 0.02;
		ip = new Field(SCREEN_HEIGHT / 2 - SCREEN_HEIGHT / 16, SCREEN_HEIGHT / 2);
		port = new Field(SCREEN_HEIGHT / 2, SCREEN_HEIGHT / 2 + SCREEN_HEIGHT / 16);
		name = new Field(SCREEN_HEIGHT / 2 + SCREEN_HEIGHT / 16, SCREEN_HEIGHT / 2 + SCREEN_HEIGHT / 8);
		curString = 0;
		Color color = new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));

		// make new panel
		panel = new LobbyPanel();

		// configure frame
		this.add(panel);
		this.addKeyListener(new LobbyKeyListener());
		this.addMouseListener(new LobbyMouseListener());
		//this.setUndecorated(true);
		this.setVisible(true);
		this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// make frame fullscreen for macs(?)
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		gd.setFullScreenWindow(this);

		image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData(); // links pixels to image 

		int[][] map = {
				{1, 1, 1, 1, 1},
				{1, 0, 0, 0, 1},
				{1, 0, 0, 0, 1},
				{1, 0, 0, 0, 1},
				{1, 1, 1, 1, 1},
		};
		int r = 255 - color.getRed();
		int g = 255 - color.getGreen();
		int b = 255 - color.getBlue();
		Color alt = new Color(r, g, b);
		Display d = new Display(map, SCREEN_WIDTH, SCREEN_HEIGHT, alt);
		player = new Player("null", 2.5, 2.5, 1, 0, 0, -1, "soldier");

		while (true) {
			update();
			d.update(player, pixels, null);
			panel.repaint();

			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void update() {
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

	private class Field {
		private int y1;
		private int y2;
		private String s;

		Field(int y1, int y2) {
			this.y1 = y1;
			this.y2 = y2;
			s = "";
		}

		public void setString(String s) {
			this.s = s;
		}

		public int getY1() {
			return y1;
		}
		public int getY2() {
			return y2;
		}
		public String getString() {
			return s;
		}
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
				System.out.println("connecting to " + ip.getString() + " " + port.getString() + " " + name.getString());
				client = new Client(ip.getString(), Integer.parseInt(port.getString()), name.getString());
				game = new Game(client);
			} else {
				// } else if ((key != KeyEvent.VK_SHIFT) && (key != KeyEvent.VK_CONTROL) && (key != KeyEvent.VK_ALT)){
				char c = e.getKeyChar();
				if (curString == 0) {
					ip.setString(s + c);
					ip.setString(ip.getString().toLowerCase());
				} else if (curString == 1) {
					port.setString(s + c);
					port.setString(port.getString().toLowerCase());
				} else if (curString == 2) {
					name.setString(s + c);
					name.setString(name.getString().toLowerCase());
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

			if (y >= ip.getY1() && y < ip.getY2()) {
				curString = 0;
			} else if (y >= port.getY1() && y < port.getY2()) {
				curString = 1;
			} else if (y >= name.getY1() && y < name.getY2()) {
				curString = 2;
			} // else if (x >= )
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
		private Font font;
		private int ipStringY;
		private int portStringY;
		private int nameStringY;

		LobbyPanel() {
			font = new Font("Arial", Font.BOLD, SCREEN_WIDTH / 32);
			ipStringY = (ip.getY1() + ip.getY2()) / 2 + font.getSize() / 2;
			portStringY = (port.getY1() + port.getY2()) / 2 + font.getSize() / 2;
			nameStringY = (name.getY1() + name.getY2()) / 2 + font.getSize() / 2;
		}

		public void paintComponent(Graphics g) {
			// draw the background rotating image
			g.drawImage(image, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);

			// draw the text fields' bounds
			g.setColor(Color.RED);
			g.drawLine(SCREEN_WIDTH / 3, ip.getY1(), SCREEN_WIDTH * 2 / 3, ip.getY1());
			g.setColor(Color.YELLOW);
			g.drawLine(SCREEN_WIDTH / 3, port.getY1(), SCREEN_WIDTH * 2 / 3, port.getY1());
			g.setColor(Color.BLUE);
			g.drawLine(SCREEN_WIDTH / 3, name.getY1(), SCREEN_WIDTH * 2 / 3, name.getY1());
			g.setColor(Color.GREEN);
			g.drawLine(SCREEN_WIDTH / 3, name.getY2(), SCREEN_WIDTH * 2 / 3, name.getY2());

			// draw current text field
			g.setColor(Color.WHITE);
			if (curString == 0) {
				g.fillRect(SCREEN_WIDTH / 3, ip.getY1(), SCREEN_WIDTH / 3, ip.getY2() - ip.getY1());
			} else if (curString == 1) {
				g.fillRect(SCREEN_WIDTH / 3, port.getY1(), SCREEN_WIDTH / 3, port.getY2() - port.getY1());
			} else if (curString == 2) {
				g.fillRect(SCREEN_WIDTH / 3, name.getY1(), SCREEN_WIDTH / 3, name.getY2() - name.getY1());
			}
			
			// draw the enter button
			g.fillOval(SCREEN_WIDTH / 2, (int)(SCREEN_HEIGHT / 1.5), SCREEN_WIDTH / 50, SCREEN_WIDTH / 50);

			// draw the ip, port and name strings
			g.setFont(font);
			g.setColor(Color.BLACK);
			g.drawString(ip.getString(), SCREEN_WIDTH / 2 - ip.getString().length() * font.getSize() / 4, ipStringY);
			g.drawString(port.getString(), SCREEN_WIDTH / 2 - port.getString().length() * font.getSize() / 4, portStringY);
			g.drawString(name.getString(), SCREEN_WIDTH / 2 - name.getString().length() * font.getSize() / 4, nameStringY);
		}
	}
}
