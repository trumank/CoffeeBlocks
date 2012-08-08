package geometry;

public class Rectangle {
	public float x;
	public float y;
	public float w;
	public float h;
	
	public Rectangle(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public Rectangle(double x, double y, double w, double h) {
		this.x = (float) x;
		this.y = (float) y;
		this.w = (float) w;
		this.h = (float) h;
	}
	
	public Point origin() {
		return new Point(x, y);
	}
}
