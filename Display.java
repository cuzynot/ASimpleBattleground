import java.awt.Color;
import java.util.ArrayList;

public class Display {
	private int[][] map;
	private int width, height;

	private final int DARK_GRAY;
	private final int GRAY;
	private final int BLACK;
	private final double PLAYER_WIDTH;

	private Color color;
	private int colorRGB;
	private int darkerColorRGB;

	// constructor
	public Display(int[][] m, int w, int h, Color c) {
		DARK_GRAY = Color.DARK_GRAY.getRGB();
		GRAY = Color.GRAY.getRGB();
		BLACK = Color.BLACK.getRGB();
		PLAYER_WIDTH = 0.5;

		map = m;
		width = w;
		height = h;
		color = c;
		colorRGB = color.getRGB();
		darkerColorRGB = color.darker().getRGB();
	}

	// recalculates how the screen should look to the user based on their position in the map
	// returns the updated array of pixels to the Game class
	public void update (Player player, int[] pixels, ArrayList<Player> players) {

		// clear screen
		for (int i = 0; i < pixels.length / 2; i++){
			pixels[i] = DARK_GRAY;
		}
		for (int i = pixels.length / 2; i < pixels.length; i++){
			pixels[i] = GRAY;
		}

		// loops through every vertical bar on the screen and casts a ray 
		// to figure out what wall should be on the screen at that vertical bar
		for (int x = 0; x < width; x++){
			double cameraX = 2 * x / (double)(width) - 1; // x-coordinate of the current vertical stripe on the camera plane

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

				for (int i = 0; i < players.size(); i++) {
					Player p = players.get(i);
					if (p.getX() > mapX - PLAYER_WIDTH && p.getX() < mapX + PLAYER_WIDTH && p.getY() > mapY - PLAYER_WIDTH && p.getY() < mapY + PLAYER_WIDTH) {
						if (dist(player, p) < closestDist) {
							closestDist = dist(player, p);
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

			// now calculate the height of the wall based on the distance from the camera
			int lineHeight;
			if (perpWallDist > 0) {
				lineHeight = Math.abs((int)(height / perpWallDist));
			} else {
				lineHeight = height;
			}

			// calculate lowest and highest pixel to fill in current stripe
			int drawStart = -lineHeight / 2 + height / 2;
			if (drawStart < 0){
				drawStart = 0;
			}

			int drawEnd = lineHeight / 2 + height / 2;
			if (drawEnd >= height){
				drawEnd = height - 1;
			}

			// calculate y coordinate on texture
			for (int i = drawStart; i < drawEnd; i++) {
				// int texi = (((i*2 - height + lineHeight) << 6) / lineHeight) / 2;

				if (side == 0) {
					pixels[x + i * width] = colorRGB;
				} else {
					pixels[x + i * width] = darkerColorRGB; // make i sides darker
				}
			}
			
			// if a player is in sight
			if (closestDist != Double.MAX_VALUE) {
				double sub = height / closestDist / 2;
				if (sub > height / 2) {
					sub = height / 2;
				}
				for (int i = (int)(height / 2 - sub); i < (int)(height / 2 + sub); i++) {
					pixels[x + i * width] = BLACK;
				}
			}
		}
	}

	private double dist(Player a, Player b) {
		return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
	}
}
