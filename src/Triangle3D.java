import java.awt.Color;

/*
 * 3rd Dimension Simplex, created by 4 textures and 4 points.
 * Has an array of colors that coorespond to each side.
 */

public class Triangle3D extends DrawableObject {

	Texture[] textures;
	Color[] colors;

	public Triangle3D(Point p1, Point p2, Point p3, Point p4, Color[] colors) {
		textures = new Texture[4];
		textures[0] = new Texture(p1, p2, p3);
		textures[1] = new Texture(p1, p2, p4);
		textures[2] = new Texture(p1, p3, p4);
		textures[3] = new Texture(p2, p3, p4);
		this.colors = colors;

	}

	// Checks if each side is hit, returns closest.
	public double isHit(double x, double y, double z, double xv, double yv, double zv) {
		double min = -1;
		for (int i = 0; i < textures.length; i++) {
			double temp = textures[i].isHit(x, y, z, xv, yv, zv);
			if (temp > 1) {
				if (min == -1) {
					min = temp;
				} else {
					min = Math.min(min, temp);
				}
			}
		}
		return min;
	}

	// checks which side has this point, return that color
	public Color getColor(double x, double y, double z) {
		double min = Double.MAX_VALUE;
		int index = -1;
		for (int i = 0; i < textures.length; i++) {
			double temp = textures[i].dist(new Point(x, y, z));
			if (temp < min) {
				min = temp;
				index = i;
			}
		}
		return colors[index];
	}

	// Filter color (make it darker)
	public Color filterColor(Color c) {
		return new Color(Math.max(c.getRed() * 9 / 10, 0), Math.max(c.getGreen() * 9 / 10, 0),
				Math.max(c.getBlue() * 9 / 10, 0));
	}

}
