import java.awt.Color;

public class Display {
	private int[][] map;
	private int mapWidth, mapHeight, width, height;

	private final int darkGray = Color.DARK_GRAY.getRGB();
	private final int gray = Color.GRAY.getRGB();
	private Color color;
	private int colorRGB;
	private int darkerColorRGB;

	// constructor
	public Display(int[][] m, int w, int h, Color c) {
		map = m;
		width = w;
		height = h;
		color = c;
		colorRGB = color.getRGB();
		darkerColorRGB = color.darker().getRGB();
	}

	// recalculates how the screen should look to the user based on their position in the map
	// returns the updated array of pixels to the Game class
	public void update (Player player, int[] pixels) {

		// clear screen
		for (int i = 0; i < pixels.length / 2; i++){
			pixels[i] = darkGray;
		}
		for (int i = pixels.length / 2; i < pixels.length; i++){
			pixels[i] = gray;
		}

		// loops through every vertical bar on the screen and casts a ray 
		// to figure out what wall should be on the screen at that vertical bar
		for (int x = 0; x < width; x++){
			double cameraX = 2 * x / (double)(width) - 1; // x-coordinate of the current vertical stripe on the camera plane

			// make a vector for the ray
			double rayDirX = player.getXDir() + player.getXPlane() * cameraX;
			double rayDirY = player.getYDir() + player.getYPlane() * cameraX;

			// Map position
			int mapX = (int) player.getX();
			int mapY = (int) player.getY();

			// length of ray from current position to next x or y-side
			double sideDistX;
			double sideDistY;

			// length of ray from one side to next in map
			double deltaDistX = Math.sqrt(1 + (rayDirY * rayDirY) / (rayDirX * rayDirX));
			double deltaDistY = Math.sqrt(1 + (rayDirX * rayDirX) / (rayDirY * rayDirY));
			double perpWallDist; // distance from the player to the first wall the ray collides with

			// direction to go in x and y
			int stepX, stepY;
			boolean hit = false; // was a wall hit
			int side=0; // was the wall vertical or horizontal

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

			// Loop to find where the ray hits a wall
			while(!hit) {
				// Jump to next square
				if (sideDistX < sideDistY){
					sideDistX += deltaDistX;
					mapX += stepX;
					side = 0;
				} else {
					sideDistY += deltaDistY;
					mapY += stepY;
					side = 1;
				}

				// Check if ray has hit a wall
				if(map[mapX][mapY] > 0) hit = true;
			}

			//Calculate distance to the point of impact
			if(side == 0){
				perpWallDist = Math.abs((mapX - player.getX() + (1 - stepX) / 2) / rayDirX);
			} else {
				perpWallDist = Math.abs((mapY - player.getY() + (1 - stepY) / 2) / rayDirY);    
			}

			//Now calculate the height of the wall based on the distance from the camera
			int lineHeight;
			if(perpWallDist > 0) lineHeight = Math.abs((int)(height / perpWallDist));
			else lineHeight = height;

			//calculate lowest and highest pixel to fill in current stripe
			int drawStart = -lineHeight / 2 + height / 2;
			if (drawStart < 0){
				drawStart = 0;
			}

			int drawEnd = lineHeight/2 + height/2;
			if (drawEnd >= height){
				drawEnd = height - 1;
			}

			// add a texture
			// int texNum = map[mapX][mapY] - 1;
			double wallX; // Exact position of where wall was hit
			if(side == 1) { // If its a y-axis wall
				wallX = (player.getX() + ((mapY - player.getY() + (1 - stepY) / 2) / rayDirY) * rayDirX);
			} else { // X-axis wall
				wallX = (player.getY() + ((mapX - player.getX() + (1 - stepX) / 2) / rayDirX) * rayDirY);
			}
			wallX -= Math.floor(wallX);

			// x coordinate on the texture
			int texX = (int)(wallX * (64));
			if(side == 0 && rayDirX > 0) texX = 64 - texX - 1;
			if(side == 1 && rayDirY < 0) texX = 64 - texX - 1;

			// calculate y coordinate on texture
			for(int y = drawStart; y < drawEnd; y++) {
				// int texY = (((y*2 - height + lineHeight) << 6) / lineHeight) / 2;

				if(side == 0) {
					pixels[x + y * width] = colorRGB;
				} else {
					pixels[x + y * width] = darkerColorRGB;//(color >> 1) & 8355711;//Make y sides darker
				}
			}
		}

		// return pixels;
	}
}
