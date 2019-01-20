package graphics;

public class Field extends Component {
	private final static int MAX_LENGTH = 25;

	public Field(int x1, int y1, int x2, int y2) {
		super(x1, y1, x2, y2);
		setString("");
	}

	// getters
	public static int getMaxLength() {
		return MAX_LENGTH;
	}
}