package graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import javax.swing.JPanel;

import player.Player;

public class Display extends JPanel {

	private int[][] map;
	private final int SCREEN_WIDTH, SCREEN_HEIGHT;
	private final int CROSSHAIR_LENGTH;

	private static final int DARK_GRAY = Color.DARK_GRAY.getRGB();
	private static final int GRAY = Color.GRAY.getRGB();
	private static final int BLACK = Color.BLACK.getRGB();
	private static final double PLAYER_WIDTH = 0.5;

	// private Color color;
	private int colorRGB;
	private int darkerColorRGB;
	private Font font;
	private Button quit;

	private Player player;
	private ArrayList<Player> players;

	// images
	private BufferedImage image;
	private int[] pixels;

	// constructor
	public Display(int[][] map, int SCREEN_WIDTH, int SCREEN_HEIGHT, Color color, Player player, ArrayList<Player> players) {
		this.map = map;
		this.SCREEN_WIDTH = SCREEN_WIDTH;
		this.SCREEN_HEIGHT = SCREEN_HEIGHT;
		this.player = player;
		this.players = players;
		this.colorRGB = color.getRGB();
		this.darkerColorRGB = color.darker().getRGB();

		CROSSHAIR_LENGTH = SCREEN_WIDTH / 100;
		font = new Font("Lora", Font.PLAIN, SCREEN_WIDTH / 50);

		image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData(); // links pixels to image 
	}

	public BufferedImage getImage() {
		return image;
	}

	public void paintComponent(Graphics g) {
		if (player.inGame()) {
			update();

			// draw the background image
			if (player.getClickedRight()) { // if the player is scoped in
				int zoomW = (int)(image.getWidth() * player.getZoom());
				int zoomH = (int)(image.getHeight() * player.getZoom());
				BufferedImage sub = image.getSubimage(zoomW, zoomH, image.getWidth() - zoomW * 2, image.getHeight() - zoomH * 2);
				g.drawImage(sub, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
			} else {
				g.drawImage(image, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
			}

			// draw crosshair
			g.setColor(Color.WHITE);
			g.drawLine(SCREEN_WIDTH / 2 - CROSSHAIR_LENGTH, SCREEN_HEIGHT / 2, SCREEN_WIDTH / 2 + CROSSHAIR_LENGTH, SCREEN_HEIGHT / 2);
			g.drawLine(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 - CROSSHAIR_LENGTH, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 + CROSSHAIR_LENGTH);

			// draw health
			g.setColor(Color.GREEN);
			g.fillRect(0, SCREEN_HEIGHT * 19 / 20, (int)(player.getHealth() * SCREEN_WIDTH / player.getMaxHealth()), SCREEN_HEIGHT / 20);

			// draw players list
			g.setFont(font);
			g.setColor(Color.BLACK);
			if (players != null) {
				for (int i = 0; i < players.size(); i++) {
					g.drawString(players.get(i).getName(), SCREEN_WIDTH * 9 / 10, SCREEN_HEIGHT / 20 * (i + 1));
				}
			}

			repaint();
		} else {
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
			
		}
	}

	// recalculates how the screen should look to the user based on their position in the map
	// returns the updated array of pixels to the Game class
	public void update() {
		// clear screen
		for (int i = 0; i < pixels.length / 2; i++){
			pixels[i] = DARK_GRAY;
		}
		for (int i = pixels.length / 2; i < pixels.length; i++){
			pixels[i] = GRAY;
		}

		// loops through every vertical bar on the screen and casts a ray 
		// to figure out what wall should be on the screen at that vertical bar
		for (int x = 0; x < SCREEN_WIDTH; x++){
			double cameraX = 2 * x / (double)(SCREEN_WIDTH) - 1; // x-coordinate of the current vertical stripe on the camera plane

			// make a vector for the ray
			double rayDirX = player.getXDir() + player.getXPlane() * cameraX;
			double rayDirY = player.getYDir() + player.getYPlane() * cameraX;

			// length of ray from one side to next in map
			double deltaDistX = Math.sqrt(1 + (rayDirY * rayDirY) / (rayDirX * rayDirX));
			double deltaDistY = Math.sqrt(1 + (rayDirX * rayDirX) / (rayDirY * rayDirY));

			// Map position
			int mapX = (int)player.getX();
			int mapY = (int)player.getY();
			// direction to go in x and y
			int stepX, stepY;
			// length of ray from current position to next x or y-side
			double sideDistX;
			double sideDistY;

			// figure out the step direction and initial distance to a side
			if (rayDirX < 0){
				stepX = -1;
				sideDistX = (player.getX() - mapX) * deltaDistX;
			} else {
				stepX = 1;
				sideDistX = (mapX + 1.0 - player.getX()) * deltaDistX;
			}

			if (rayDirY < 0){
				stepY = -1;
				sideDistY = (player.getY() - mapY) * deltaDistY;
			} else {
				stepY = 1;
				sideDistY = (mapY + 1.0 - player.getY()) * deltaDistY;
			}

			boolean hitWall = false; // was a wall hit
			int side = 0; // was the wall vertical or horizontal
			double closestDist = Double.MAX_VALUE; // closest distance to a player

			// Loop to find where the ray hits a wall
			while (!hitWall) {
				// Jump to next square
				if (sideDistX < sideDistY) {
					sideDistX += deltaDistX;
					mapX += stepX;
					side = 0;
				} else {
					sideDistY += deltaDistY;
					mapY += stepY;
					side = 1;
				}

				if (map[mapX][mapY] > 0) {
					hitWall = true;
				}

				if (players != null) {
					for (int i = 0; i < players.size(); i++) {
						Player p = players.get(i);
						if (p.getX() > mapX - PLAYER_WIDTH && p.getX() < mapX + PLAYER_WIDTH && p.getY() > mapY - PLAYER_WIDTH && p.getY() < mapY + PLAYER_WIDTH) {
							if (dist(player, p) < closestDist) {
								closestDist = dist(player, p);
							}
						}
					}
				}
			}

			double perpWallDist; // distance from the player to the first wall the ray collides with

			// calculate distance to the point of impact
			if (side == 0) {
				perpWallDist = Math.abs((mapX - player.getX() + (1 - stepX) / 2) / rayDirX);
			} else {
				perpWallDist = Math.abs((mapY - player.getY() + (1 - stepY) / 2) / rayDirY);    
			}

			// now calculate the SCREEN_HEIGHT of the wall based on the distance from the camera
			int lineHeight;
			if (perpWallDist > 0) {
				lineHeight = Math.abs((int)(SCREEN_HEIGHT / perpWallDist));
			} else {
				lineHeight = SCREEN_HEIGHT;
			}

			// calculate lowest and highest pixel to fill in current stripe
			int drawStart = -lineHeight / 2 + SCREEN_HEIGHT / 2;
			if (drawStart < 0){
				drawStart = 0;
			}

			int drawEnd = lineHeight / 2 + SCREEN_HEIGHT / 2;
			if (drawEnd >= SCREEN_HEIGHT){
				drawEnd = SCREEN_HEIGHT - 1;
			}

			// calculate y coordinate on texture
			for (int i = drawStart; i < drawEnd; i++) {
				// int texi = (((i*2 - SCREEN_HEIGHT + lineHeight) << 6) / lineHeight) / 2;

				if (side == 0) {
					pixels[x + i * SCREEN_WIDTH] = colorRGB;
				} else {
					pixels[x + i * SCREEN_WIDTH] = darkerColorRGB; // make i sides darker
				}
			}

			// if a player is in sight
			if (closestDist != Double.MAX_VALUE) {
				double sub = SCREEN_HEIGHT / closestDist / 2;
				if (sub > SCREEN_HEIGHT / 2) {
					sub = SCREEN_HEIGHT / 2;
				}
				for (int i = (int)(SCREEN_HEIGHT / 2 - sub); i < (int)(SCREEN_HEIGHT / 2 + sub); i++) {
					pixels[x + i * SCREEN_WIDTH] = BLACK;
				}
			}
		}
	}

	private double dist(Player a, Player b) {
		return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
	}
}
