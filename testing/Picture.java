package testing;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

public class Picture {
	Point[] points;
	Texture texture;

	public Picture(Point[] points, Texture texture) {
		this.points = points;
		this.texture = texture;
	}

	public void draw() {
		texture.bind();

		GL11.glBegin(7);
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f((float)(points[0].x), (float)(points[0].y), (float)(points[0].z));
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f((float)(points[1].x), (float)(points[1].y), (float)(points[1].z));
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f((float)(points[2].x), (float)(points[2].y), (float)(points[2].z));
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f((float)(points[3].x), (float)(points[3].y), (float)(points[3].z));
		GL11.glEnd();
	}
}
