import java.awt.Color;
import java.awt.image.BufferedImage;

/* 
 * These are the threads that draw the image using the objects.
 * Each has a specific section of the image to draw, done by rayCasting
 * each pixel and seeing what each ray hits.
 */

public class UpdateThread extends Thread {

	private BufferedImage screen;
	private int x;
	private int y;
	private int width;
	private int height;
	private DrawableObject[] objects;
	private Point playerPos;
	private double h, v;

	public UpdateThread(BufferedImage screen, DrawableObject[] objects, int x, int y, int width, int height,
			Point player, double h, double v) {
		this.screen = screen;
		this.objects = objects;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.playerPos = player;
		this.h = h;
		this.v = v;

	}

	public void run() {

		Color[][] colors = new Color[height][width];

		double cosH = Math.cos(h);
		double sinH = Math.sin(h);
		double cosV = Math.cos(v);
		double sinV = Math.sin(v);

		for (int i = x; i < x + width; i++) {
			for (int j = y; j < y + height; j++) {

				double ratio = ((double) i - (double) MainPanel.WIDTH / 2) / ((double) MainPanel.WIDTH / 2)
						* (double) MainPanel.VIEW_RATIO; // -0.5 to 0.5 -> 30
															// degrees left or
															// right
				double ratioV = ((double) j - (double) MainPanel.HEIGHT / 2) / ((double) MainPanel.HEIGHT / 2)
						* (double) MainPanel.VIEW_RATIO_V;

				// sets up vector from pixel.
				double tempDist = Calc.dist(1, ratio, ratioV);
				double xv = 1 / tempDist;
				double yv = -ratio / tempDist;
				double zv = -ratioV / tempDist;

				double txv = xv * cosV - zv * sinV;
				zv = xv * sinV + zv * cosV;

				xv = txv * cosH - yv * sinH;
				yv = txv * sinH + yv * cosH;

				// finds what color this specific pixel should be.
				colors[j - y][i - x] = drawFromVector(new Point(xv, yv, zv));
			}
		}

		// final setting of colors onto screen
		// Could be improved with just accessing directly,
		// but probably doesn't have a big impact on the time.
		int[] finalColors = new int[height * width];
		for (int i = 0; i < colors.length; i++) {
			for (int j = 0; j < colors[i].length; j++) {
				finalColors[i * colors[i].length + j] = colors[i][j].getRGB();
			}
		}
		updateImage(finalColors);
	}

	private Color drawFromVector(Point v) {
		double t = Double.MAX_VALUE;
		int indexOfClosest = -1;
		for (int i = 0; i < objects.length; i++) {
			double temp = objects[i].isHit(playerPos, v);
			if (temp > MainPanel.MIN_DIST && temp < t) {
				t = temp;
				indexOfClosest = i;
			}
		}
		if (indexOfClosest == -1) {
			return Color.black;
		}
		// Now we know which object was hit first, and we are finding what color
		// it is.

		Point onObject = new Point(playerPos.getX() + t * v.getX(), playerPos.getY() + t * v.getY(),
				playerPos.getZ() + t * v.getZ());
		Color colorToDraw = objects[indexOfClosest].getColor(onObject);

		// Now to filter the color through all objects hit.
		DrawableObject[] objectsHit = new DrawableObject[objects.length];
		int size = 0;

		for (int i = 0; i < objects.length; i++) {
			// from on the object in direction of the light to see what is
			// blocking the light
			double temp = objects[i].isHit(onObject, MainPanel.LIGHT);
			if (temp != -1) {
				if (i == indexOfClosest) {
					return objects[i].filterColor(colorToDraw);
				}
				objectsHit[size] = objects[i];
				size++;
			}
		}

		// Might possibly not be accurate because not sorted by distance.
		for (int i = 0; i < size; i++) {
			colorToDraw = objectsHit[i].filterColor(colorToDraw);
		}

		return colorToDraw;
	}

	private void updateImage(int[] colors) {
		screen.setRGB(x, y, width, height, colors, 0, width);
	}
}
