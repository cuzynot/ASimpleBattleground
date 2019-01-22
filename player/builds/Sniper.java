/**
 * [Sniper].java
 * Sniper is a subclass of Build
 * with more damage than the other builds
 * @author      Yili Liu
 * @since       Dec.18.2018
 */
package player.builds;

public class Sniper extends Build {

	/**
	 * Sniper
	 * constructor
	 */
	public Sniper() {
		// set build values
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


