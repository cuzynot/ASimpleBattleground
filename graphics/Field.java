package graphics;

public class Field extends Component {
	private final int MAX_LENGTH;

	public Field(int x1, int y1, int x2, int y2, int MAX_LENGTH) {
		super(x1, y1, x2, y2);
		this.MAX_LENGTH = MAX_LENGTH;
		setString("");
	}
	
	// setters

	// getters
	public int getMaxLength() {
		return MAX_LENGTH;
	}
}