package player.builds;
import player.Player;

public class Assassin extends Player {
	
	// constructor
	public Assassin(String name, double x, double y, double xd, double yd, double xp, double yp) {
		super(name, x, y, xd, yd, xp, yp);
		init();
		// this.name = "Assassin";
	}
	
	public Assassin(String name) {
		super(name);
		init();
	}
	
	private void init() {
		this.maxHealth = 1900;
		this.health = maxHealth;
		this.speed = 0.085;
		this.damage = 520;
		this.fireRate = 360;
		this.maxAmmo = 6;
		this.ammo = maxAmmo;
		this.zoom = 0.00001;
	}
}
