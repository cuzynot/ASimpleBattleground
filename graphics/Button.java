/**
 * [Button].java
 * Button is a subclass of Component,
 * desgined to hold corners
 * of a button for graphics
 * @author      Yili Liu
 * @since       Dec.18.2018
 */
package graphics;

public class Button extends Component {

	/**
	 * Button 
	 * constructor
	 * @param x1, x of top left corner of the button
	 * @param y1, y of top left corner of the button
	 * @param x2, x of bottom right corner of the button
	 * @param y2, y of bottom right corner of the button
	 * @param s, string displayed for the button
	 */
	public Button(int x1, int y1, int x2, int y2, String s) {
		super(x1, y1, x2, y2);
		setString(s);
	}
}
