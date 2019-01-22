/**
 * [Soldier].java
 * Soldier is a subclass of Build
 * that is most well rounded
 * @author      Yili Liu
 * @since       Dec.18.2018
 */
package player.builds;

public class Soldier extends Build {

	/**
	 * Soldier
	 * constructor
	 */
	public Soldier() {
		// set build values
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


