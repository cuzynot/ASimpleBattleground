/**
 * [Assassin].java
 * Assassin is a subclass of Build
 * with more speed than the other builds
 * @author      Yili Liu
 * @since       Dec.18.2018
 */
package player.builds;

public class Assassin extends Build {

	/**
	 * Assassin
	 * constructor
	 */
	public Assassin() {
		// set build values
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
