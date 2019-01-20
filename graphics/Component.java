package graphics;

public class Component {
	
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private String s;
	
	public Component(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public void setString(String s) {
		this.s = s;
	}
	
	// getters
	public boolean clicked(double x, double y) {
		if (x >= x1 && x < x2 && y >= y1 && y < y2) {
			return true;
		} else {
			return false;
		}
	}
	public int getX1() {
		return x1;
	}
	public int getY1() {
		return y1;
	}
	public int getX2() {
		return x2;
	}
	public int getY2() {
		return y2;
	}
	public String getString() {
		return s;
	}
}
