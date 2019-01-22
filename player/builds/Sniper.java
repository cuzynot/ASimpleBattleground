package player.builds;

public class Sniper extends Build {

	// constructor
	public Sniper() {
		this.maxHealth = 2100;
		this.health = maxHealth;
		this.speed = 0.055;
		this.damage = 1200;
		this.fireRate = 1000;
		this.maxAmmo = 5;
		this.ammo = maxAmmo;
		this.zoom = 0.35;
	}
}
