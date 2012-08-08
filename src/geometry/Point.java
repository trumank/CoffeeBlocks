package geometry;

public class Point {
	public float x;
	public float y;

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(double x, double y) {
		this.x = (float) x;
		this.y = (float) y;
	}
	
	public Point add(Point p) {
		return new Point(x + p.x, y + p.y);
	}
}
