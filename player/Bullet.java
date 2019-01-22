/**
 * [Bullet].java
 * Bullet is a class that holds
 * x, y, and x and y dirs of
 * a bullet
 * @author      Yili Liu
 * @since       Dec.18.2018
 */
package player;

public class Bullet implements Moveable {

	// init vars
	private double x;
	private double y;
	private double xDir;
	private double yDir;
	
	/**
	 * Bullet
	 * constructor
	 * @param x, x pos of the bullet
	 * @param y, y pos of the bullet
	 * @param xDir, x direction of the bullet
	 * @param yDir, y direction of the bullet
	 */
	public Bullet(double x, double y, double xDir, double yDir) {
		this.x = x;
		this.y = y;
		this.xDir = xDir;
		this.yDir = yDir;
	}
	
	/**
	 * collides
	 * this method returns a boolean value
	 * indicating whether or not this bullet
	 * has collided with a player
	 * @param p, player to be checked
	 * @return b, if there is a collision
	 */
	public boolean collides(Player p) {
		if (x > p.getX() - 0.5 && x < p.getX() + 0.5 && y > p.getY() - 0.5 && y < p.getY() + 0.5) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}
	
	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public double getXDir() {
		return xDir;
	}

	@Override
	public double getYDir() {
		return yDir;
	}
	
}
