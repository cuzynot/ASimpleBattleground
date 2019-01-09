package testing;

import org.newdawn.slick.opengl.Texture;

public class Cube {
	double x;
	double y;
	double z;
	double side;
	Picture[] faces = new Picture[6];

	public Cube(double x, double y, double z, double side, Texture texture) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.side = side;

		Point[] up = new Point[4];
		up[0] = new Point(x, y + side, z);
		up[1] = new Point(x + side, y + side, z);
		up[2] = new Point(x + side, y + side, z + side);
		up[3] = new Point(x, y + side, z + side);
		faces[0] = new Picture(up, texture);

		Point[] down = new Point[4];
		down[0] = new Point(x, y, z);
		down[1] = new Point(x + side, y, z);
		down[2] = new Point(x + side, y, z + side);
		down[3] = new Point(x, y, z + side);
		faces[1] = new Picture(down, texture);

		Point[] front = new Point[4];
		front[2] = new Point(x, y, z);
		front[3] = new Point(x + side, y, z);
		front[0] = new Point(x + side, y + side, z);
		front[1] = new Point(x, y + side, z);
		faces[2] = new Picture(front, texture);

		Point[] right = new Point[4];
		right[2] = new Point(x + side, y, z);
		right[3] = new Point(x + side, y, z + side);
		right[0] = new Point(x + side, y + side, z + side);
		right[1] = new Point(x + side, y + side, z);
		faces[3] = new Picture(right, texture);

		Point[] behind = new Point[4];
		behind[2] = new Point(x, y, z + side);
		behind[3] = new Point(x + side, y, z + side);
		behind[0] = new Point(x + side, y + side, z + side);
		behind[1] = new Point(x, y + side, z + side);
		faces[4] = new Picture(behind, texture);

		Point[] left = new Point[4];
		left[2] = new Point(x, y, z + side);
		left[3] = new Point(x, y, z);
		left[0] = new Point(x, y + side, z);
		left[1] = new Point(x, y + side, z + side);
		faces[5] = new Picture(left, texture);
	}

	public void draw() {
		for (Picture p : faces) {
			p.draw();
		}
	}
}
