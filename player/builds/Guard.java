/**
 * [Guard].java
 * Guard is a subclass of Build
 * with more health than the other builds
 * @author      Yili Liu
 * @since       Dec.18.2018
 */
package player.builds;

public class Guard extends Build {

	/**
	 * Guard
	 * constructor
	 */
	public Guard() {
		// set build values
		this.maxHealth = 4700;
		this.health = maxHealth;
		this.speed = 0.05;
		this.damage = 225;
		this.fireRate = 517;
		this.maxAmmo = 5;
		this.ammo = maxAmmo;
		this.zoom = 0.1;
	}
}

