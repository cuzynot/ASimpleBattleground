/**
 * [Moveable].java
 * Moveable is an interface
 * for any moveable object
 * @author      Yili Liu
 * @since       Dec.18.2018
 */
package player;

public interface Moveable {
	
	
	/**
	 * setX
	 * sets the new x location of an obj
	 * @param x, new x pos of an obj
	 */
	void setX(double x);
	
	/**
	 * setY
	 * sets the new y location of an obj
	 * @param y, new y pos of an obj
	 */
	void setY(double y);
	
	/**
	 * getX
	 * returns the x pos of an obj
	 * @return double x, the x pos of an obj
	 */
	double getX();
	
	/**
	 * getY
	 * returns the y pos of an obj
	 * @return double y, the y pos of an obj
	 */
	double getY();
	
	/**
	 * getXDir
	 * returns the x dir of an obj
	 * @return double xDir, the x dir of an obj
	 */
	double getXDir();
	
	/**
	 * getYDir
	 * returns the y dir of an obj
	 * @return double yDir, the y dir of an obj
	 */
	double getYDir();
}
