package player.builds;

public class Build {

	protected int ammo;
	protected int maxAmmo;
	protected double health;
	protected double maxHealth;
	protected double damage;
	protected double speed;
	protected double fireRate;
	protected double zoom;

	// getters
	public int getAmmo() {
		return ammo;
	}

	public int getMaxAmmo() {
		return maxAmmo;
	}

	public double getHealth() {
		return health;
	}

	public double getMaxHealth() {
		return maxHealth;
	}

	public double getDamage() {
		return damage;
	}

	public double getSpeed() {
		return speed;
	}

	public double getFireRate() {
		return fireRate;
	}

	public double getZoom() {
		return zoom;
	}

	// setters
	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}

	public void setHealth(double health) {
		this.health = health;
	}
}
