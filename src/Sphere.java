import java.awt.Color;

/*
 * A drawableObject to draw in 3D space, defined by a center and radius (and color)
 */

public class Sphere extends DrawableObject {

	private Point center;
	private double radius;
	private Color c;

	public Sphere(Point center, double radius, Color c) {
		this.center = center;
		this.radius = radius;
		this.c = c;
	}

	// Implementation of isHit from drawableObject
	// Done by solving the quadratic for t, and returning the positive one.
	public double isHit(double x, double y, double z, double xv, double yv, double zv) {
		Point relativePos = new Point(x - center.getX(), y - center.getY(), z - center.getZ());

		double a = (xv * xv) + (yv * yv) + (zv * zv);
		double b = 2 * (xv * relativePos.getX() + yv * relativePos.getY() + zv * relativePos.getZ());
		double c = relativePos.getX() * relativePos.getX() + relativePos.getY() * relativePos.getY()
				+ relativePos.getZ() * relativePos.getZ() - radius * radius;

		double t1 = (-b + Math.sqrt(b * b - 4 * a * c)) / 2 / a;
		double t2 = (-b - Math.sqrt(b * b - 4 * a * c)) / 2 / a;
		double t = 0;

		if (t1 > 1) {
			if (t2 > 1) {
				if (t2 < t1) {
					t = t2;
				} else {
					t = t1;
				}
			} else {
				t = t1;
			}
		} else {
			if (t2 > 1) {
				t = t2;
			} else {
				return -1;
			}
		}

		return t;
	}

	// Gets the color of sphere and comparing normal of position of sphere to
	// the light vector
	public Color getColor(double x, double y, double z) {
		Point relativePos = new Point(x - center.getX(), y - center.getY(), z - center.getZ());
		double p = Calc.dotProduct(relativePos, MainPanel.LIGHT) / Calc.dist(relativePos);

		return new Color((int) (Math.min(255, Math.max(0, c.getRed() + p * MainPanel.LIGHT_STRENGTH))),
				(int) (Math.min(255, Math.max(0, c.getGreen() + p * MainPanel.LIGHT_STRENGTH))),
				(int) (Math.min(255, Math.max(0, c.getBlue() + p * MainPanel.LIGHT_STRENGTH))));
	}

	// Filters a color (makes it darker).
	public Color filterColor(Color c) {
		return new Color(Math.max(c.getRed() * 9 / 10, 0), Math.max(c.getGreen() * 9 / 10, 0),
				Math.max(c.getBlue() * 9 / 10, 0));
	}

}
