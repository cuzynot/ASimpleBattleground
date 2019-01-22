package player.builds;

public class Soldier extends Build {

	// constructor
	public Soldier() {
		this.maxHealth = 2200;
		this.health = maxHealth;
		this.speed = 0.06;
		this.damage = 135;
		this.fireRate = 100;
		this.maxAmmo = 30;
		this.ammo = maxAmmo;
		this.zoom = 0.15;
	}
}
