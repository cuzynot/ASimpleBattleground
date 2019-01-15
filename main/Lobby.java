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

public class Lobby {

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

	private JFrame frame;
	private LobbyPanel panel;

	private Field ip;
	private Field port;
	private Field name;
	private int curString;
	private int counter;
	private Color color;
	
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
		ip = new Field(SCREEN_WIDTH / 3, SCREEN_HEIGHT / 2 - SCREEN_HEIGHT / 16, SCREEN_WIDTH * 2/ 3, SCREEN_HEIGHT / 2);
		port = new Field(SCREEN_WIDTH / 3, SCREEN_HEIGHT / 2, SCREEN_WIDTH * 2/ 3, SCREEN_HEIGHT / 2 + SCREEN_HEIGHT / 16);
		name = new Field(SCREEN_WIDTH / 3, SCREEN_HEIGHT / 2 + SCREEN_HEIGHT / 16, SCREEN_WIDTH * 2/ 3, SCREEN_HEIGHT / 2 + SCREEN_HEIGHT / 8);
		curString = 0;
		counter = 0;
		color = new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));

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
		//frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// make frame fullscreen for macs(?)
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
		gd.setFullScreenWindow(frame);

		image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData(); // links pixels to image 

		int[][] map = {
				{1, 1, 1, 1, 1},
				{1, 0, 0, 0, 1},
				{1, 0, 0, 0, 1},
				{1, 0, 0, 0, 1},
				{1, 1, 1, 1, 1},
		};
		Display d = new Display(map, SCREEN_WIDTH, SCREEN_HEIGHT, color);
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
		private final static int MAX_LENGTH = 25;
		private int x1;
		private int y1;
		private int x2;
		private int y2;
		private String s;

		Field(int x1, int y1, int x2, int y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			s = "";
		}

		// setters
		public void setString(String s) {
			this.s = s;
		}

		// getters
		public int getX1() {
			return x1;
		}
		public int getY1() {
			return y1;
		}
		public int getX2() {
			return x2;
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
				frame.dispose();
				frame.removeKeyListener(lkl);
				frame.removeMouseListener(lml);
				game = new Game(client);
			} else if (key != KeyEvent.VK_SPACE) {
				// } else if ((key != KeyEvent.VK_SHIFT) && (key != KeyEvent.VK_CONTROL) && (key != KeyEvent.VK_ALT)){
				char c = e.getKeyChar();
				if (s.length() < Field.MAX_LENGTH) {
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
			// double x = e.getPoint().getX();
			double y = e.getPoint().getY();

			if (y >= ip.getY1() && y < ip.getY2()) {
				curString = 0;
			} else if (y >= port.getY1() && y < port.getY2()) {
				curString = 1;
			} else if (y >= name.getY1() && y < name.getY2()) {
				curString = 2;
			} // else if (x >= )

			counter = 0;
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
		private int ipStringY;
		private int portStringY;
		private int nameStringY;
		private Font logo;

		private final int OFFSET;

		LobbyPanel() {
			//			int r = 255 - color.getRed();
			//			int g = 255 - color.getGreen();
			//			int b = 255 - color.getBlue();
			//			alt = new Color(r, g, b);
			OFFSET = 10;
			logo = new Font("Helvetica", Font.BOLD | Font.ITALIC, SCREEN_WIDTH / 15);
			fields = new Font("Lora", Font.PLAIN, SCREEN_WIDTH / 40);
			ipStringY = (ip.getY1() + ip.getY2()) / 2 + fields.getSize() / 2;
			portStringY = (port.getY1() + port.getY2()) / 2 + fields.getSize() / 2;
			nameStringY = (name.getY1() + name.getY2()) / 2 + fields.getSize() / 2;
		}

		public void paintComponent(Graphics g) {
			// super.paintComponent(g);

			// draw the background rotating image
			g.drawImage(image, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);

			// draw the text fields' bounds
			g.setColor(Color.RED);
			g.drawLine(ip.getX1(), ip.getY1(), ip.getX2(), ip.getY1());
			g.setColor(Color.YELLOW);
			g.drawLine(port.getX1(), port.getY1(), port.getX2(), port.getY1());
			g.setColor(Color.BLUE);
			g.drawLine(name.getX1(), name.getY1(), name.getX2(), name.getY1());
			g.setColor(Color.GREEN);
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
			g.fillOval(SCREEN_WIDTH / 2, (int)(SCREEN_HEIGHT / 1.5), SCREEN_WIDTH / 50, SCREEN_WIDTH / 50);

			// draw the ip, port and name strings
			g.setFont(fields);
			g.setColor(Color.WHITE);
			g.drawString(ip.getString(), ip.getX1(), ipStringY);
			g.drawString(port.getString(), port.getX1(), portStringY);
			g.drawString(name.getString(), name.getX1(), nameStringY);
			//			// centred
			//			g.drawString(ip.getString(), SCREEN_WIDTH / 2 - ip.getString().length() * fields.getSize() / 4, ipStringY);
			//			g.drawString(port.getString(), SCREEN_WIDTH / 2 - port.getString().length() * 15, portStringY);
			//			g.drawString(name.getString(), SCREEN_WIDTH / 2 - name.getString().length() * 15, nameStringY);

			// draw logo
			g.setFont(logo);
			g.drawString("A SIMPLE BATTLEGROUND", SCREEN_WIDTH / 32, SCREEN_HEIGHT / 5);

			if (counter < SCREEN_WIDTH / 2 - ip.getX1()) {
				counter += 40;
			}

			//			try {
			//				Thread.sleep(20);
			//			} catch (InterruptedException e) {
			//				e.printStackTrace();
			//			}
			//			repaint();
		}
	}
}
