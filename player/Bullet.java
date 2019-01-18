package player;

public class Bullet implements Moveable {

	private double x;
	private double y;
	private double xDir;
	private double yDir;
	
	public Bullet(double x, double y, double xDir, double yDir) {
		this.x = x;
		this.y = y;
		this.xDir = xDir;
		this.yDir = yDir;
	}
	
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
