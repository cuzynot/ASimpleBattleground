
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import Graphics.Camera;
import Graphics.Screen;

public class Game extends JFrame implements Runnable{

	// run
	public static void main (String[] args){
		new Game();
	}

	// Camera
	public Camera camera;

	// Screen
	public Screen screen;

	private static final long serialVersionUID = 1L;
	public int mapWidth = 15;
	public int mapHeight = 15;

	// game thread
	private Thread thread;
	private boolean running;

	// images
	private BufferedImage image;
	public int[] pixels;
	public static int[][] map = {
			{1,1,1,1,1,1,1,1,2,2,2,2,2,2,2},
			{1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
			{1,0,3,3,3,3,3,0,0,0,0,0,0,0,2},
			{1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
			{1,0,3,0,0,0,3,0,2,2,2,0,2,2,2},
			{1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
			{1,0,3,3,0,3,3,0,2,0,0,0,0,0,2},
			{1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
			{1,1,1,1,1,1,1,1,4,4,4,0,4,4,4},
			{1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
			{1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
			{1,0,0,2,0,0,1,4,0,3,3,3,3,0,4},
			{1,0,0,0,0,0,1,4,0,3,3,3,3,0,4},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
			{1,1,1,1,1,1,1,4,4,4,4,4,4,4,4}
	};

	// constructor
	public Game (){
		thread = new Thread(this);
		image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

		// init jframe
		setSize(640, 480);
		setResizable(false);
		setTitle("3D Test");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.black);
		setLocationRelativeTo(null);
		setVisible(true);

		// init Camera
		camera = new Camera(4.5, 4.5, 1, 0, 0, -0.66);
		addKeyListener(camera);
		addMouseMotionListener(camera);

		// init Screen
		screen = new Screen(map, 640, 480); // screen = new Screen(map, mapWidth, mapHeight, textures, 640, 480);


		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		getContentPane().setCursor(blankCursor);


		start();
	}

	// start and stop
	private synchronized void start() {
		running = true;
		thread.start();
	}
	public synchronized void stop() {
		running = false;
		try {
			thread.join(); // waits for thread to die
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy(); // used when rendering so the screen updates are smoother

		if(bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		bs.show();
	}

	// required for implementation
	public void run() {
		long last = System.nanoTime();
		final double ns = 1000000000.0 / 60.0; //60 times per second
		double delta = 0;
		requestFocus();

		while(running) {
			long now = System.nanoTime();
			delta += ((now - last) / ns);
			last = now;

			while (delta >= 1){ //Make sure update is only happening 60 times a second
				//handles all of the logic restricted time
				camera.update(map);
				screen.update(camera, pixels);
				delta--;
			}

			render(); //displays to the screen unrestricted time
		}

	}
}