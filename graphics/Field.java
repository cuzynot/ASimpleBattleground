/**
 * [Field].java
 * Field is a subclass of Component,
 * desgined to hold corners
 * of a text field for graphics
 * and can be updated by a user
 * @author      Yili Liu
 * @since       Dec.18.2018
 */
package graphics;

public class Field extends Component {
	// init vars
	// max length of the text field
	private final int MAX_LENGTH;

	/**
	 * Field 
	 * constructor
	 * @param x1, x of top left corner of the field
	 * @param y1, y of top left corner of the field
	 * @param x2, x of bottom right corner of the field
	 * @param y2, y of bottom right corner of the field
	 * @param MAX_LENGTH, the max length of the field
	 */
	public Field(int x1, int y1, int x2, int y2, int MAX_LENGTH) {
		super(x1, y1, x2, y2);
		this.MAX_LENGTH = MAX_LENGTH;
		setString("");
	}

	/**
	 * getMaxLength
	 * this method returns the maximum length of this field
	 * @return MAX_LENGTH, the maximum length of this field
	 */
	public int getMaxLength() {
		return MAX_LENGTH;
	}
}