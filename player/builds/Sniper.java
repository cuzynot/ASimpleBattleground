package player.builds;
import player.Player;

public class Sniper extends Player {
	
	// constructor
	public Sniper(String name, double x, double y, double xd, double yd, double xp, double yp) {
		super(name, x, y, xd, yd, xp, yp);
		init();
	}
	
	public Sniper(String name) {
		super(name);
		init();
	}
	
	private void init() {
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
