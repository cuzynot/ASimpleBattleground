package player.builds;
import player.Player;

public class Soldier extends Player {
	
	// constructor
	public Soldier(String name, double x, double y, double xd, double yd, double xp, double yp) {
		super(name, x, y, xd, yd, xp, yp);
		init();
	}
	
	public Soldier(String name) {
		super(name);
		init();
	}
	
	private void init() {
		this.maxHealth = 2200;
		this.health = maxHealth;
		this.speed = 0.12;
		this.damage = 135;
		this.fireRate = 100;
		this.maxAmmo = 30;
		this.ammo = maxAmmo;
		this.zoom = 0.1;
	}
}
