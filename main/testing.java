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
		private Button exit;
		private Button prevBuild;
		private Button nextBuild;
		// images
		private BufferedImage assassinIcon;
		private int SCREEN_WIDTH;
		private int SCREEN_HEIGHT;
		private Color color = Color.RED;

		// constructor
		public Panel() {

			SCREEN_WIDTH = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
			SCREEN_HEIGHT = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight());

			font = new Font("Lora", Font.PLAIN, SCREEN_WIDTH / 50);
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
//			g.setColor(Color.WHITE);
//			g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

			// draw exit button
			g.setColor(color);
			g.fillRect(exit.getX1(), exit.getY1(), exit.getX2() - exit.getX1(), exit.getY2() - exit.getY1());
			g.setColor(Color.WHITE);
			g.drawLine(exit.getX1(), exit.getY1(), exit.getX2(), exit.getY2());
			g.drawLine(exit.getX2(), exit.getY1(), exit.getX1(), exit.getY2());

			// draw builds
			g.setColor(color);
			g.setFont(font);
			g.drawString(prevBuild.getString(), prevBuild.getX1(), prevBuild.getY2() - 10);
			g.drawString(nextBuild.getString(), nextBuild.getX1(), nextBuild.getY2() - 10);

			// draw build icons
			g.drawImage(assassinIcon, SCREEN_WIDTH * 9 / 20, SCREEN_HEIGHT / 2, SCREEN_WIDTH / 10, SCREEN_WIDTH / 10, null);
			g.setColor(color);
			g.setFont(font);

			//g.drawString("Assassin", 0, 100);
			g.drawString("Assassin", SCREEN_WIDTH * 23 / 50, SCREEN_HEIGHT * 7 / 10);

			repaint();
		}
	}


}
