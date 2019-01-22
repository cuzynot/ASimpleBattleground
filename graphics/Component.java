/**
 * [Component].java
 * Button is a superclass.
 * desgined to hold corners, and a string
 * of a graphical component
 * @author      Yili Liu
 * @since       Dec.18.2018
 */
package graphics;

public class Component {
	
	// init vars
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private String s;
	
	/**
	 * Component 
	 * constructor
	 * @param x1, x of top left corner of the component
	 * @param y1, y of top left corner of the component
	 * @param x2, x of bottom right corner of the component
	 * @param y2, y of bottom right corner of the component
	 */
	public Component(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	/**
	 * setString
	 * this method sets the string of a graphical component
	 * @param s, the new string of a component
	 */
	public void setString(String s) {
		this.s = s;
	}
	
	/**
	 * clicked
	 * this method takes in a point and determines if
	 * the component is clicked
	 * @param x, x of mouse
	 * @param y, y of mouse
	 * @return b, if this component is clicked
	 */
	public boolean clicked(double x, double y) {
		if (x >= x1 && x < x2 && y >= y1 && y < y2) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * getX1
	 * this method returns the x of the top left corner
	 * of this component
	 * @return x1, x of the top left corner of this component
	 */
	public int getX1() {
		return x1;
	}
	
	/**
	 * getY1
	 * this method returns the y of the top left corner
	 * of this component
	 * @return y1, y of the top left corner of this component
	 */
	public int getY1() {
		return y1;
	}
	
	/**
	 * getX2
	 * this method returns the x of the bottom right corner
	 * of this component
	 * @return x2, x of the bottom right corner of this component
	 */
	public int getX2() {
		return x2;
	}
	
	/**
	 * getY2
	 * this method returns the x of the bottom right corner
	 * of this component
	 * @return y2, y of the bottom right corner of this component
	 */
	public int getY2() {
		return y2;
	}
	
	/**
	 * getString
	 * this method returns the string contained
	 * in this component
	 * @return s, string contained in this component
	 */
	public String getString() {
		return s;
	}
}
