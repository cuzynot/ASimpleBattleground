package builds;

public class Build {
	
	// protected so its subclasses can access them
	protected String name;
	protected double health;
	protected double speed;
	protected double damage;
	protected double fireRate;
	protected double zoom;
	protected int magSize;
	
	// getters
	public String getName() {
		return name;
	}
	
	public double getHealth() {
		return health;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public double getDamage() {
		return damage;
	}
	
	public double getFireRate() {
		return fireRate;
	}

	public double getZoom() {
		return zoom;
	}
	
	public int getMagSize() {
		return magSize;
	}
	
	
}
