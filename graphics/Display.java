package graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import data_structures.SimpleLinkedList;
import player.Player;
import player.builds.Assassin;
import player.builds.Guard;
import player.builds.Sniper;
import player.builds.Soldier;

public class Display extends JPanel {

	private int[][] map;
	private final int SCREEN_WIDTH, SCREEN_HEIGHT;
	private final int CROSSHAIR_LENGTH;

	private static final int DARK_GRAY = Color.DARK_GRAY.getRGB();
	private static final int GRAY = Color.GRAY.getRGB();
	private static final int BLACK = Color.BLACK.getRGB();
	private static final double PLAYER_WIDTH = 0.5;

	private Color color;
	private int colorRGB;
	private int darkerColorRGB;
	private Font font;
	private Font logo;
	private Button exit;
	private Button prevBuild;
	private Button nextBuild;
	private int curBuild;
	private final int OFFSET;

	private Player player;
	private SimpleLinkedList<Player> players;

	// images
	private BufferedImage image;
	private static BufferedImage assassinWeapon;
	private static BufferedImage guardWeapon;
	private static BufferedImage sniperWeapon;
	private static BufferedImage soldierWeapon;
	private static BufferedImage assassinScope;
	private static BufferedImage guardScope;
	private static BufferedImage sniperScope;
	private static BufferedImage soldierScope;
	private static BufferedImage assassinIcon;
	private static BufferedImage guardIcon;
	private static BufferedImage sniperIcon;
	private static BufferedImage soldierIcon;
	private int[] pixels;

	// constructor
	public Display(int[][] map, int SCREEN_WIDTH, int SCREEN_HEIGHT, Color color, Player player, SimpleLinkedList<Player> players) {
		this.map = map;
		this.SCREEN_WIDTH = SCREEN_WIDTH;
		this.SCREEN_HEIGHT = SCREEN_HEIGHT;
		this.player = player;
		this.players = players;
		this.color = color;
		this.colorRGB = color.getRGB();
		this.darkerColorRGB = color.darker().getRGB();

		CROSSHAIR_LENGTH = SCREEN_WIDTH / 100;
		font = new Font("Lora", Font.PLAIN, SCREEN_WIDTH / 50);
		//quit = new Button(SCREEN_WIDTH / 2 - SCREEN_HEIGHT / 16, SCREEN_HEIGHT / 2 - SCREEN_HEIGHT / 32, SCREEN_WIDTH / 2 + SCREEN_HEIGHT / 16, SCREEN_HEIGHT / 2 + SCREEN_HEIGHT / 32, "QUIT");
		exit = new Button(SCREEN_WIDTH - SCREEN_HEIGHT / 32, 0, SCREEN_WIDTH, SCREEN_HEIGHT / 32, "");
		prevBuild = new Button(SCREEN_WIDTH * 3 / 7 - SCREEN_WIDTH / 100, SCREEN_HEIGHT * 58 / 100 - SCREEN_WIDTH / 100, SCREEN_WIDTH * 3 / 7 + SCREEN_WIDTH / 100, SCREEN_HEIGHT * 58 / 100 + SCREEN_WIDTH / 100, "<");
		nextBuild = new Button(SCREEN_WIDTH * 4 / 7 - SCREEN_WIDTH / 100, SCREEN_HEIGHT * 58 / 100 - SCREEN_WIDTH / 100, SCREEN_WIDTH * 4 / 7 + SCREEN_WIDTH / 100, SCREEN_HEIGHT * 58 / 100 + SCREEN_WIDTH / 100, ">");
		logo = new Font("Helvetica", Font.BOLD | Font.ITALIC, SCREEN_WIDTH / 15);
		OFFSET = 10;
		if (player.getBuild() instanceof Assassin) {
			curBuild = 0;
		} else if (player.getBuild() instanceof Guard) {
			curBuild = 1;
		} else if (player.getBuild() instanceof Sniper) {
			curBuild = 2;
		} else if (player.getBuild() instanceof Soldier) {
			curBuild = 3;
		}

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
		image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData(); // links pixels to image 
	}

	public BufferedImage getImage() {
		return image;
	}

	public void paintComponent(Graphics g) {
		if (player.inGame()) {
			// if the player is respawning
			if (player.getBuild().getHealth() > 0 && player.getRespawn() <= 0) {
				update();

				// draw the background image
				if (player.getClickedRight()) { // if the player is scoped in
					int zoomW = (int)(image.getWidth() * player.getBuild().getZoom());
					int zoomH = (int)(image.getHeight() * player.getBuild().getZoom());
					BufferedImage sub = image.getSubimage(zoomW, zoomH, image.getWidth() - zoomW * 2, image.getHeight() - zoomH * 2);
					g.drawImage(sub, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
				} else {
					g.drawImage(image, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
				}

				// draw weapon or scoped weapon
				if (player.getClickedRight()) {
					if (player.getBuild() instanceof Assassin) {
						g.drawImage(assassinScope, SCREEN_WIDTH / 4, SCREEN_HEIGHT / 2, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, null);
					} else if (player.getBuild() instanceof Guard) {
						g.drawImage(guardScope, SCREEN_WIDTH * 3 / 8, SCREEN_HEIGHT / 2, SCREEN_WIDTH / 4, SCREEN_HEIGHT / 2, null);
					} else if (player.getBuild() instanceof Sniper) {
						g.drawImage(sniperScope, 0, SCREEN_HEIGHT / 20, SCREEN_WIDTH, SCREEN_HEIGHT * 19 / 20, null);
					} else if (player.getBuild() instanceof Soldier) {
						g.drawImage(soldierScope, SCREEN_WIDTH * 3 / 8, SCREEN_HEIGHT / 2, SCREEN_WIDTH / 4, SCREEN_HEIGHT / 2, null);
					}
				} else {
					if (player.getBuild() instanceof Assassin) {
						g.drawImage(assassinWeapon, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, null);
					} else if (player.getBuild() instanceof Guard) {
						g.drawImage(guardWeapon, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, null);
					} else if (player.getBuild() instanceof Sniper) {
						g.drawImage(sniperWeapon, SCREEN_WIDTH * 3 / 7, SCREEN_HEIGHT / 2, SCREEN_WIDTH * 4 / 7, SCREEN_HEIGHT / 2, null);
					} else if (player.getBuild() instanceof Soldier) {
						g.drawImage(soldierWeapon, SCREEN_WIDTH * 3 / 7, SCREEN_HEIGHT / 2, SCREEN_WIDTH * 4 / 7, SCREEN_HEIGHT / 2, null);
					}
				}

				// draw health
				g.setColor(Color.GREEN);
				g.fillRect(0, SCREEN_HEIGHT * 19 / 20, (int)(player.getBuild().getHealth() * SCREEN_WIDTH / player.getBuild().getMaxHealth()), SCREEN_HEIGHT / 20);

				g.setColor(Color.WHITE);
				if (player.getReload() > 0) {
					// draw reloading arc
					g.fillArc(SCREEN_WIDTH / 2 - CROSSHAIR_LENGTH, SCREEN_HEIGHT / 2 - CROSSHAIR_LENGTH, CROSSHAIR_LENGTH * 2, CROSSHAIR_LENGTH * 2, 90, 360 * player.getReload() / 1000);
				} else if (!player.getClickedRight()) {
					// draw crosshair
					g.setColor(Color.WHITE);
					g.drawLine(SCREEN_WIDTH / 2 - CROSSHAIR_LENGTH, SCREEN_HEIGHT / 2, SCREEN_WIDTH / 2 + CROSSHAIR_LENGTH, SCREEN_HEIGHT / 2);
					g.drawLine(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 - CROSSHAIR_LENGTH, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 + CROSSHAIR_LENGTH);
				}

				// draw ammo count
				g.setFont(font);
				g.setColor(Color.WHITE);
				g.drawString(player.getBuild().getAmmo() + "/" + player.getBuild().getMaxAmmo(), SCREEN_WIDTH - SCREEN_WIDTH / 20, SCREEN_HEIGHT - SCREEN_WIDTH / 20);

				// draw players list
				g.setFont(font);
				g.setColor(Color.WHITE);
				if (players != null) {
					g.drawString(player.getName(), SCREEN_WIDTH * 3 / 4, SCREEN_HEIGHT / 20);
					g.drawString(Integer.toString(player.getScore()), SCREEN_WIDTH * 15 / 16, SCREEN_HEIGHT / 20);
					for (int i = 0; i < players.size(); i++) {
						// draw players' names and their respective scores
						g.drawString(players.get(i).getName(), SCREEN_WIDTH * 3 / 4, SCREEN_HEIGHT / 20 * (i + 2));
						g.drawString(Integer.toString(players.get(i).getScore()), SCREEN_WIDTH * 15 / 16, SCREEN_HEIGHT / 20 * (i + 2));
					}
				}

			} else {
				// draw background
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
				
				// draw respawn buffer
				g.setColor(Color.WHITE);
				g.fillArc(SCREEN_WIDTH / 2 - CROSSHAIR_LENGTH, SCREEN_HEIGHT / 2 - CROSSHAIR_LENGTH, CROSSHAIR_LENGTH * 2, CROSSHAIR_LENGTH * 2, 90, 360 * player.getRespawn() / 3000);
				
				// draw elimination message
				g.setColor(Color.WHITE);
				g.setFont(logo);
				g.drawString("ELIMINATED BY", 0, SCREEN_HEIGHT / 3);
				g.setColor(color);
				g.setFont(font);
				g.drawString(player.getEliminator(), 0, SCREEN_HEIGHT * 2 / 3);
			}
		} else {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

			// draw logo
			g.setColor(color);
			g.setFont(logo);
			g.drawString("A SIMPLE BATTLEGROUND", SCREEN_WIDTH / 32, SCREEN_HEIGHT / 5);

			// draw exit button
			g.setColor(color);
			g.fillRect(exit.getX1(), exit.getY1(), exit.getX2() - exit.getX1(), exit.getY2() - exit.getY1());
			g.setColor(Color.WHITE);
			g.drawLine(exit.getX1(), exit.getY1(), exit.getX2(), exit.getY2());
			g.drawLine(exit.getX2(), exit.getY1(), exit.getX1(), exit.getY2());

			// draw builds
			g.setColor(color);
			g.setFont(font);
			g.drawString(prevBuild.getString(), prevBuild.getX1(), prevBuild.getY2() - OFFSET);
			g.drawString(nextBuild.getString(), nextBuild.getX1(), nextBuild.getY2() - OFFSET);

			// draw build icons
			g.setColor(color);
			if (curBuild == 0) {
				g.drawImage(assassinIcon, SCREEN_WIDTH * 9 / 20, SCREEN_HEIGHT / 2, SCREEN_WIDTH / 10, SCREEN_WIDTH / 10, null);
				g.drawString("Assassin", SCREEN_WIDTH * 23 / 50, SCREEN_HEIGHT * 7 / 10);
			} else if (curBuild == 1) {
				g.drawImage(guardIcon, SCREEN_WIDTH * 9 / 20, SCREEN_HEIGHT / 2, SCREEN_WIDTH / 10, SCREEN_WIDTH / 10, null);
				g.drawString("  Guard ", SCREEN_WIDTH * 23 / 50, SCREEN_HEIGHT * 7 / 10);
			} else if (curBuild == 2) {
				g.drawImage(sniperIcon, SCREEN_WIDTH * 9 / 20, SCREEN_HEIGHT / 2, SCREEN_WIDTH / 10, SCREEN_WIDTH / 10, null);
				g.drawString("  Sniper", SCREEN_WIDTH * 23 / 50, SCREEN_HEIGHT * 7 / 10);
			} else if (curBuild == 3) {
				g.drawImage(soldierIcon, SCREEN_WIDTH * 9 / 20, SCREEN_HEIGHT / 2, SCREEN_WIDTH / 10, SCREEN_WIDTH / 10, null);
				g.drawString(" Soldier", SCREEN_WIDTH * 23 / 50, SCREEN_HEIGHT * 7 / 10);
			}
		}
		repaint();
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
						// check if player is respawning
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

	public Button getExit() {
		return exit;
	}
	
	public Button getPrevBuild() {
		return prevBuild;
	}
	
	public Button getNextBuild() {
		return nextBuild;
	}
	
	public int getCurBuild() {
		return curBuild;
	}
	
	public void setCurBuild(int curBuild) {
		this.curBuild = curBuild;
	}

	private double dist(Player a, Player b) {
		return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
	}
}
