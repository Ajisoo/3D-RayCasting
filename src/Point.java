
/* 
 * My specific definition of a point in 3D Space
 */

public class Point {

	private double x;
	private double y;
	private double z;

	// Properties a point should have
	public Point add(Point p) {
		return new Point(x + p.getX(), y + p.getY(), z + p.getZ());
	}

	public Point sub(Point p) {
		return new Point(x - p.getX(), y - p.getY(), z - p.getZ());
	}

	public Point mult(double d) {
		return new Point(x * d, y * d, z * d);
	}

	// Makes it a unit vector with z being positive
	public void norm() {
		double div = Math.sqrt(x * x + y * y + z * z);
		x /= div;
		y /= div;
		z /= div;

		if (z < 0) {
			x *= -1;
			y *= -1;
			z *= -1;
		}
	}

	// Getters, setters, toString
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public String toString() {
		return "x: " + x + " y: " + y + " z: " + z;
	}
}
