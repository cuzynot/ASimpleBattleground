package player.builds;
import player.Player;

public class Guard extends Player {
	
	// constructor
	public Guard(String name, double x, double y, double xd, double yd, double xp, double yp) {
		super(name, x, y, xd, yd, xp, yp);
		init();
	}
	
	public Guard(String name) {
		super(name);
		init();
	}
	
	private void init() {
		this.maxHealth = 4700;
		this.health = maxHealth;
		this.speed = 0.10;
		this.damage = 225;
		this.fireRate = 517;
		this.maxAmmo = 5;
		this.ammo = maxAmmo;
		this.zoom = 0.00001;
	}
}
