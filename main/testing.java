package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import graphics.Button;

public class testing {

	public static void main(String[] args) {
		new testing();
	}

	private JFrame frame;

	testing() {
		LobbyKeyListener lkl = new LobbyKeyListener();
		Panel p = new Panel();
		p.repaint();

		int SCREEN_WIDTH = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
		int SCREEN_HEIGHT = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight());

		// configure frame
		frame = new JFrame();
		frame.add(p);
		frame.addKeyListener(lkl);
		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);;
	}

	private class LobbyKeyListener implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_ENTER) {
				// new Frame2();
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {}
		@Override
		public void keyReleased(KeyEvent e) {}
	}


	private class Panel extends JPanel {

		private Font font;
		private Font logo;
		private Button exit;
		private Button prevBuild;
		private Button nextBuild;
		// images
		private int SCREEN_WIDTH;
		private int SCREEN_HEIGHT;
		private int CROSSHAIR_LENGTH;
		private Color color = Color.RED;

		private BufferedImage image;
		private BufferedImage assassinWeapon;
		private BufferedImage guardWeapon;
		private BufferedImage sniperWeapon;
		private BufferedImage soldierWeapon;
		private BufferedImage assassinScope;
		private BufferedImage guardScope;
		private BufferedImage sniperScope;
		private BufferedImage soldierScope;
		private BufferedImage assassinIcon;
		private BufferedImage guardIcon;
		private BufferedImage sniperIcon;
		private BufferedImage soldierIcon;
		private int[] pixels;

		// constructor
		public Panel() {

			try {
				assassinWeapon = ImageIO.read(new File("res/assassinWeapon.png"));
				guardWeapon = ImageIO.read(new File("res/guardWeapon.png"));
				sniperWeapon = ImageIO.read(new File("res/sniperWeapon.png"));
				soldierWeapon = ImageIO.read(new File("res/soldierWeapon.png"));
				assassinScope = ImageIO.read(new File("res/assassinScope.png"));
				guardScope = ImageIO.read(new File("res/guardScope.png"));
				sniperScope = ImageIO.read(new File("res/sniperScope.png"));
				soldierScope = ImageIO.read(new File("res/soldierScope.png"));
				assassinIcon = ImageIO.read(new File("res/assassinIcon.png"));
				guardIcon = ImageIO.read(new File("res/guardIcon.png"));
				sniperIcon = ImageIO.read(new File("res/sniperIcon.png"));
				soldierIcon = ImageIO.read(new File("res/soldierIcon.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			SCREEN_WIDTH = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
			SCREEN_HEIGHT = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight());

			CROSSHAIR_LENGTH = SCREEN_WIDTH / 50;

			font = new Font("Lora", Font.PLAIN, SCREEN_WIDTH / 50);
			logo = new Font("Helvetica", Font.BOLD | Font.ITALIC, SCREEN_WIDTH / 15);

			//quit = new Button(SCREEN_WIDTH / 2 - SCREEN_HEIGHT / 16, SCREEN_HEIGHT / 2 - SCREEN_HEIGHT / 32, SCREEN_WIDTH / 2 + SCREEN_HEIGHT / 16, SCREEN_HEIGHT / 2 + SCREEN_HEIGHT / 32, "QUIT");
			exit = new Button(SCREEN_WIDTH - SCREEN_HEIGHT / 32, 0, SCREEN_WIDTH, SCREEN_HEIGHT / 32, "");
			prevBuild = new Button(SCREEN_WIDTH * 3 / 7 - SCREEN_WIDTH / 100, SCREEN_HEIGHT * 58 / 100 - SCREEN_WIDTH / 100, SCREEN_WIDTH * 3 / 7 + SCREEN_WIDTH / 100, SCREEN_HEIGHT * 58 / 100 + SCREEN_WIDTH / 100, "<");
			nextBuild = new Button(SCREEN_WIDTH * 4 / 7 - SCREEN_WIDTH / 100, SCREEN_HEIGHT * 58 / 100 - SCREEN_WIDTH / 100, SCREEN_WIDTH * 4 / 7 + SCREEN_WIDTH / 100, SCREEN_HEIGHT * 58 / 100 + SCREEN_WIDTH / 100, ">");

			try {
				assassinIcon = ImageIO.read(new File("res/assassinIcon.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void paintComponent(Graphics g) {
			g.drawImage(soldierScope, SCREEN_WIDTH / 2 - SCREEN_WIDTH / 8, SCREEN_HEIGHT / 2, SCREEN_WIDTH / 4, SCREEN_HEIGHT / 2, null);
			//			g.drawImage(soldierWeapon, SCREEN_WIDTH * 3 / 7, SCREEN_HEIGHT / 2, SCREEN_WIDTH * 4 / 7, SCREEN_HEIGHT / 2, null);


			// draw crosshair
			g.setColor(Color.WHITE);
			g.drawLine(SCREEN_WIDTH / 2 - CROSSHAIR_LENGTH, SCREEN_HEIGHT / 2, SCREEN_WIDTH / 2 + CROSSHAIR_LENGTH, SCREEN_HEIGHT / 2);
			g.drawLine(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 - CROSSHAIR_LENGTH, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 + CROSSHAIR_LENGTH);
			repaint();
		}
	}


}
