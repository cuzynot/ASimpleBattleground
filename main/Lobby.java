package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data_structures.SimpleLinkedList;
import graphics.Display;

public class Lobby {

	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;
	private final double ROTATE_SPEED;

	//	private int counter;
	//	private boolean side;

	private BufferedImage image;
	private int[] pixels;
	private Player player;

	// jcomponents
	private JFrame frame;
	private JPanel panel;

	private Field ip;
	private Field port;
	private Field name;
	private int curString;

	public static void main(String[] args) {
		new Lobby();
	}

	Lobby() {
		SCREEN_WIDTH = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
		SCREEN_HEIGHT = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		ROTATE_SPEED = 0.02;
		ip = new Field(SCREEN_HEIGHT / 2 - SCREEN_HEIGHT / 20, SCREEN_HEIGHT / 2);
		port = new Field(SCREEN_HEIGHT / 2 + 1, SCREEN_HEIGHT / 2 + SCREEN_HEIGHT / 20);
		name = new Field(SCREEN_HEIGHT / 2 + SCREEN_HEIGHT / 20 + 1, SCREEN_HEIGHT / 2 + SCREEN_HEIGHT / 10);
		curString = 0;
		Color color = new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
		// Font font = new Font("Arial", Font.BOLD, SCREEN_WIDTH / 100);

		panel = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(image, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);

				Font font = new Font("Arial", Font.BOLD, SCREEN_WIDTH / 20);
				g.setFont(font);

				g.setColor(Color.RED);
				g.drawLine(0, ip.getY1(), SCREEN_WIDTH, ip.getY1());
				g.drawLine(0, ip.getY2(), SCREEN_WIDTH, ip.getY2());
				g.setColor(Color.YELLOW);
				g.drawLine(0, port.getY1(), SCREEN_WIDTH, port.getY1());
				g.drawLine(0, port.getY2(), SCREEN_WIDTH, port.getY2());
				g.setColor(Color.BLUE);
				g.drawLine(0, name.getY1(), SCREEN_WIDTH, name.getY1());
				g.drawLine(0, name.getY2(), SCREEN_WIDTH, name.getY2());

				g.drawString(ip.getString(), SCREEN_WIDTH / 10, ip.getY1());
				g.drawString(port.getString(), SCREEN_WIDTH / 10, port.getY1());
				g.drawString(name.getString(), SCREEN_WIDTH / 10, name.getY1());
			}
		};

		frame = new JFrame();
		frame.add(panel);
		frame.addKeyListener(new LobbyKeyListener());
		frame.addMouseListener(new LobbyMouseListener());
		frame.setVisible(true);
		frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

	private class LobbyKeyListener implements KeyListener {
		@Override
		public void keyTyped (KeyEvent e) {
		}

		@Override
		public void keyPressed (KeyEvent e){
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
			} else if ((key != KeyEvent.VK_SHIFT) && (key != KeyEvent.VK_CONTROL) && (key != KeyEvent.VK_ALT)){
				char c = e.getKeyChar();
				if (curString == 0) {
					ip.setString(s + c);
				} else if (curString == 1) {
					port.setString(s + c);			
				} else if (curString == 2) {
					name.setString(s + c);			
				}
			}
		}

		@Override
		public void keyReleased (KeyEvent e){
		}
	}

	private class LobbyMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
		}

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
			}
			
			System.out.println("new cs " + curString);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
	}

	private class Field {
		private int y1;
		private int y2;
		private String s;

		Field(int y1, int y2) {//, int x2, int y2) {
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


	//	public void paintComponent(Graphics g) {
	//		g.setColor(Color.GRAY);
	//		g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT / 2);
	//		g.setColor(Color.DARK_GRAY);
	//		g.fillRect(0, SCREEN_HEIGHT / 2, SCREEN_WIDTH, SCREEN_HEIGHT / 2);
	//
	//		if (side) {
	//			g.setColor(Color.CYAN);
	//		} else {
	//			g.setColor(Color.CYAN.darker());
	//		}
	//
	//		for (int y1 = SCREEN_HEIGHT / 5 - SCREEN_WIDTH + counter; y1 < SCREEN_HEIGHT / 5; y1++) {
	//			g.drawLine(0, y1, counter, SCREEN_HEIGHT / 5);
	//		}
	//		for (int y1 = SCREEN_HEIGHT * 4 / 5; y1 < SCREEN_HEIGHT * 4 / 5 + SCREEN_WIDTH - counter; y1++) {
	//			g.drawLine(0, y1, counter, SCREEN_HEIGHT * 4 / 5);
	//		}
	//		g.fillRect(0, SCREEN_HEIGHT / 5, counter, SCREEN_HEIGHT * 3 / 5);
	//
	//		if (side) {
	//			g.setColor(Color.CYAN.darker());
	//		} else {
	//			g.setColor(Color.CYAN);
	//		}
	//		for (int y2 = SCREEN_HEIGHT / 5 - counter; y2 < SCREEN_HEIGHT / 5; y2++) {
	//			g.drawLine(counter, SCREEN_HEIGHT / 5, SCREEN_WIDTH, y2);
	//		}
	//		for (int y2 = SCREEN_HEIGHT * 4 / 5; y2 < SCREEN_HEIGHT * 4 / 5 + counter; y2++) {
	//			g.drawLine(counter, SCREEN_HEIGHT * 4 / 5, SCREEN_WIDTH, y2);
	//		}
	//		g.fillRect(counter, SCREEN_HEIGHT / 5, SCREEN_WIDTH - counter, SCREEN_HEIGHT * 3 / 5);
	//
	//		counter -= ROTATE_SPEED;
	//		if (counter <= 0) {
	//			counter += SCREEN_WIDTH;
	//			// side = !side;
	//		}
	//	}
}
