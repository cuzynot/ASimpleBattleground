package player.builds;

public class Assassin extends Build {

	// constructor
	public Assassin() {
		this.maxHealth = 1900;
		this.health = maxHealth;
		this.speed = 0.085;
		this.damage = 520;
		this.fireRate = 360;
		this.maxAmmo = 6;
		this.ammo = maxAmmo;
		this.zoom = 0.1;
	}
}
