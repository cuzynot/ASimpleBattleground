/**
 * [Build].java
 * Build is a super class to
 * store configurable values
 * of a player
 * @author      Yili Liu
 * @since       Dec.18.2018
 */
package player.builds;

public class Build {

	// init vars
	protected int ammo;
	protected int maxAmmo;
	protected double health;
	protected double maxHealth;
	protected double damage;
	protected double speed;
	protected double fireRate;
	protected double zoom;

	/**
	 * getAmmo
	 * returns number of ammo in player
	 * @return ammo, number of ammo in player
	 */
	public int getAmmo() {
		return ammo;
	}

	/**
	 * getMaxAmmo
	 * returns size of mag in player
	 * @return maxAmmo, size of mag in player
	 */
	public int getMaxAmmo() {
		return maxAmmo;
	}

	/**
	 * getHealth
	 * returns cur health of player
	 * @return health, cur health of player
	 */
	public double getHealth() {
		return health;
	}

	/**
	 * getMaxHealth
	 * returns max health of player
	 * @return maxHealth, max health of player
	 */
	public double getMaxHealth() {
		return maxHealth;
	}

	/**
	 * getDamage
	 * returns damage each bullet does
	 * @return damage, damage each bullet does
	 */
	public double getDamage() {
		return damage;
	}

	/**
	 * getSpeed
	 * returns speed of player
	 * @return speed, speed of player
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * getFireRate
	 * returns fireRate of player
	 * @return fireRate, fireRate of player
	 */
	public double getFireRate() {
		return fireRate;
	}

	/**
	 * getZoom
	 * returns zoom of player
	 * @return zoom, zoom of player
	 */
	public double getZoom() {
		return zoom;
	}

	/**
	 * setAmmo
	 * takes in a new value
	 * and sets it as new number of ammo
	 * @param ammo, new number of ammo of player
	 */
	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}
	
	/**
	 * setHealth
	 * takes in a new value
	 * and sets it as new health
	 * @param health, new health of player
	 */
	public void setHealth(double health) {
		this.health = health;
	}
}
