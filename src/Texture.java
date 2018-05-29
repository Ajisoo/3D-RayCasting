import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

/*
 * Another DrawableObject, this time a triangle. Has no color, but can be extended.
 */

public class Texture extends DrawableObject {

	private Point p1;
	private Point p2;
	private Point p3;

	/*
	 * Ix = f Px1 + g Px2 + h Px3 Iy = f Py1 + g Py2 + h Py3 Iz = f Pz1 + g Pz2
	 * + h Pz3
	 *
	 * If all three (Ix, Iy, Iz) are larger than or equal to zero, the point is
	 * inside (or on the boundary of) the triangle. If any is negative, it's
	 * outside.
	 */

	// Found by solving the equation shown above after finding position on the
	// plane created
	// by these 3 points.
	public double isHit(double x, double y, double z, double xv, double yv, double zv) {
		Point plane = Calc.crossProduct(p2.sub(p1), p3.sub(p1));
		plane.norm();
		double d = Calc.dotProduct(plane, p1); // ax + by + cz = d

		double t = (d - plane.getX() * x - plane.getY() * y - plane.getZ() * z)
				/ (plane.getX() * xv + plane.getY() * yv + plane.getZ() * zv);

		if (t <= 0) {
			return -1;
		}

		Point onPlane = new Point(x + t * xv, y + t * yv, z + t * zv);
		double[][] result = null;
		try {
			result = new Matrix(new double[][] { { p1.getX(), p2.getX(), p3.getX() },
					{ p1.getY(), p2.getY(), p3.getY() }, { p1.getZ(), p2.getZ(), p3.getZ() } }).solve(
							new Matrix(new double[][] { { onPlane.getX() }, { onPlane.getY() }, { onPlane.getZ() } }))
							.getData();
		} catch (Exception e) {
			String toPrint = "Position: " + new Point(x, y, z) + "\n" + "Velocity: " + new Point(xv, yv, zv) + " T:" + t
					+ "\n" + "onPlane: " + onPlane + "\n" + "plane: " + plane;
			System.out.println(toPrint);
			System.exit(1);
		}

		if (result[0][result[0].length - 1] < 0 || result[1][result[1].length - 1] < 0
				|| result[2][result[2].length - 1] < 0) {
			return -1;
		}
		return t;
	}

	public Texture(Point p1, Point p2, Point p3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;

		// to prevent unsolvable matrices
		if (p1.getX() == p2.getX() && p1.getX() == p3.getX()) {
			p3.setX(p3.getX() + 0.00001);
		}
		if (p1.getY() == p2.getY() && p1.getY() == p3.getY()) {
			p3.setY(p3.getY() + 0.00001);
		}
		if (p1.getZ() == p2.getZ() && p1.getZ() == p3.getZ()) {
			p3.setZ(p3.getZ() + 0.00001);
		}
	}

	public Texture(Point p1, Point p2, Point p3, BufferedImage image) {
		this(p1, p2, p3);
	}

	public Point getP1() {
		return p1;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
	}

	public Point getP2() {
		return p2;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
	}

	public Point getP3() {
		return p3;
	}

	public void setP3(Point p3) {
		this.p3 = p3;
	}

	// distance from point to plane
	public double dist(Point p) {
		Point plane = Calc.crossProduct(p2.sub(p1), p3.sub(p1));
		plane.norm();
		double d = Calc.dotProduct(plane, p1); // ax + by + cz = d
		double d2 = Calc.dotProduct(plane, p);

		return Math.abs(d2 - d);
	}

	// Default color
	public Color getColor(double x, double y, double z) {
		return Color.yellow;
	}

	// Default filter
	public Color filterColor(Color c) {
		return c;
	}

}
