package Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture {
	public int[] pixels; // pixels of texture
	public String loc; // path to texture
	public final int SIZE; // size of texture
	
	public static int testing;
	
	// constructor
	public Texture (String location, int size) {
		loc = location;
		SIZE = size;
		pixels = new int[SIZE * SIZE];
		load();
	}
	
	private void load (){
		try {
			BufferedImage image = ImageIO.read(new File(loc)); // reads texture from folder
			int w = image.getWidth();
			int h = image.getHeight();
			image.getRGB(0, 0, w, h, pixels, 0, w); // takes texture data and stores it in pixels[]
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
