import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MainPanel extends JPanel implements ActionListener {

	// Ratio between resolution from image to display.
	private static final int RATIO = 2;

	// Threads to draw the image.
	private static final int THREADS = 64;

	// Dimensions of display
	public static final int W = 1920;
	public static final int H = 1080;

	// Dimensions of image
	public static final int WIDTH = W / RATIO;
	public static final int HEIGHT = H / RATIO;

	// FOV values.
	public static final double VIEW_RATIO = 0.5;
	public static final double VIEW_RATIO_V = VIEW_RATIO * HEIGHT / WIDTH;
	public static final double MIN_DIST = 5;

	// Where light originates (this is a vector).
	private static final double LIGHT_X = 1 / Math.sqrt(18);
	private static final double LIGHT_Y = 1 / Math.sqrt(18);
	private static final double LIGHT_Z = 4 / Math.sqrt(18);
	public static final Point LIGHT = new Point(
			LIGHT_X / Math.sqrt(LIGHT_X * LIGHT_X + LIGHT_Y * LIGHT_Y + LIGHT_Z * LIGHT_Z),
			LIGHT_Y / Math.sqrt(LIGHT_X * LIGHT_X + LIGHT_Y * LIGHT_Y + LIGHT_Z * LIGHT_Z),
			LIGHT_Z / Math.sqrt(LIGHT_X * LIGHT_X + LIGHT_Y * LIGHT_Y + LIGHT_Z * LIGHT_Z));
	public static final double LIGHT_STRENGTH = 100;

	// Used to draw the display.
	private BufferedImage screen;
	private DrawableObject[] objects;
	private UpdateThread[] threads;

	// Player's orientation and position
	private double h;
	private double v;
	private double x;
	private double y;
	private double z;
	private double xv, yv, zv;

	// For moving mouse
	private Robot robot;

	// For gathering data on time.
	private long startTime;
	private long avgTime;
	private long samples;

	// TEMP: properties of rotation objects
	private Point[] radii;
	private Point[] starts;
	private Point[] times;
	private Point[] negs;
	private double[] radius;

	// MainPanel Constructor
	public MainPanel() {

		screen = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		objects = new DrawableObject[13];

		// temp testing of different objects
		objects[10] = new Sphere(new Point(200, 0, 0), 50, new Color(0, 150, 150));
		objects[11] = new Texture(new Point(400, 0, 0), new Point(400, 0, 50), new Point(400, 50, 0));
		objects[12] = new Triangle3D(new Point(100, 0, 0), new Point(100, 0, 50), new Point(100, 50, 0),
				new Point(150, 0, 0), new Color[] { Color.white, Color.blue, Color.red, Color.yellow });

		// Temp creation of rotating spheres
		radii = new Point[10];
		starts = new Point[10];
		times = new Point[10];
		negs = new Point[10];
		radius = new double[10];

		// initialize each sphere
		for (int i = 0; i < 10; i++) {
			double radiusx = 50 + Math.random() * 100;
			double radiusy = 50 + Math.random() * 100;
			double radiusz = 50 + Math.random() * 100;
			radii[i] = new Point(radiusx, radiusy, radiusz);
			double startx = Math.random() * 2 * Math.PI;
			double starty = Math.random() * 2 * Math.PI;
			double startz = Math.random() * 2 * Math.PI;
			starts[i] = new Point(startx, starty, startz);
			double timex = Math.random() * 60 + 20;
			double timey = Math.random() * 60 + 20;
			double timez = Math.random() * 60 + 20;
			times[i] = new Point(timex, timey, timez);
			int negx = Math.random() > 0.5 ? 1 : -1;
			int negy = Math.random() > 0.5 ? 1 : -1;
			int negz = Math.random() > 0.5 ? 1 : -1;
			negs[i] = new Point(negx, negy, negz);
			radius[i] = Math.random() * 25 + 5;
		}

		Timer t = new Timer(1000 / 60, this);
		new GameListener(this);
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		threads = new UpdateThread[THREADS];
		robot.mouseMove(getWidth() / 2, getHeight() / 2);
		t.start();
		robot.mouseMove(getWidth() / 2, getHeight() / 2);

		// Problems with 0.
		h = 0.0000001;
		v = 0.0000001;
		x = 0.0000001;
		y = 0.0000001;
		z = 0.0000001;
		samples = 0;
	}

	private void test() {
		startTime = System.nanoTime();
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
	}

	public void paintComponent(Graphics g) {
		g.drawImage(screen, 0, 0, getWidth(), getHeight(), null);

		// Display information
		g.setColor(Color.cyan);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.drawString("x: " + String.format("%.2f", x), 0, 20);
		g.drawString("y: " + String.format("%.2f", y), 0, 40);
		g.drawString("z: " + String.format("%.2f", z), 0, 60);
		g.drawString("Horizontal Angle: " + String.format("%.2f", (h * 180 / Math.PI) % 360), 0, 80);
		g.drawString("Vertical Angle: " + String.format("%.2f", v * 180 / Math.PI), 0, 100);
	}

	public void actionPerformed(ActionEvent e) {
		samples++;

		// update sphere
		for (int i = 0; i < 10; i++) {

			objects[i] = new Sphere(
					new Point(
							200 + negs[i].getX() * radii[i].getX()
									* Math.cos(samples * Math.PI / times[i].getX() + starts[i].getX()),
							negs[i].getY() * radii[i].getY()
									* Math.sin(samples * Math.PI / times[i].getY() + starts[i].getY()),
							negs[i].getZ() * radii[i].getZ()
									* Math.cos(samples * Math.PI / times[i].getZ() + starts[i].getZ())),
					radius[i], new Color(150, 0, 150));
		}

		// Arrow keys change angle
		if (GameListener.keyboard[38])
			v -= 0.07;
		if (GameListener.keyboard[40])
			v += 0.07;
		if (GameListener.keyboard[39])
			h += 0.07;
		if (GameListener.keyboard[37])
			h -= 0.07;

		// mouse movement changes angle
		h -= (GameListener.x - getWidth() / 2) * 0.002;
		v -= (GameListener.y - getHeight() / 2) * 0.002;
		robot.mouseMove(getWidth() / 2, getHeight() / 2);

		// up (17 is shift)
		if (GameListener.keyboard[32]) {
			if (GameListener.keyboard[17])
				zv += 100;
			else
				zv += 20;
		}
		// down
		if (GameListener.keyboard[16]) {
			if (GameListener.keyboard[17])
				zv -= 100;
			else
				zv -= 20;
		}
		// forward
		if (GameListener.keyboard[87]) {
			if (GameListener.keyboard[17]) {
				xv += Math.cos(h) * 100;
				yv += Math.sin(h) * 100;
			} else {
				xv += Math.cos(h) * 20;
				yv += Math.sin(h) * 20;
			}
		}
		// back
		if (GameListener.keyboard[83]) {
			if (GameListener.keyboard[17]) {
				xv -= Math.cos(h) * 100;
				yv -= Math.sin(h) * 100;
			} else {
				xv -= Math.cos(h) * 20;
				yv -= Math.sin(h) * 20;
			}
		}
		// right
		if (GameListener.keyboard[68]) {
			if (GameListener.keyboard[17]) {
				xv -= Math.cos(h + Math.PI / 2) * 100;
				yv -= Math.sin(h + Math.PI / 2) * 100;
			} else {
				xv -= Math.cos(h + Math.PI / 2) * 20;
				yv -= Math.sin(h + Math.PI / 2) * 20;
			}
		}
		// left
		if (GameListener.keyboard[65]) {
			if (GameListener.keyboard[17]) {
				xv += Math.cos(h + Math.PI / 2) * 100;
				yv += Math.sin(h + Math.PI / 2) * 100;
			} else {
				xv += Math.cos(h + Math.PI / 2) * 20;
				yv += Math.sin(h + Math.PI / 2) * 20;
			}
		}

		// decay on speed.
		xv *= 0.8;
		yv *= 0.8;
		zv *= 0.8;

		// set bounds for vertical angle.
		if (v > Math.PI / 2)
			v = Math.PI / 2;
		if (v < -Math.PI / 2)
			v = -Math.PI / 2;
		x += xv;
		y += yv;
		z += zv;
		Point p = new Point(x, y, z);

		// Draw the image
		for (int i = 0; i < THREADS; i++) {
			threads[i] = new UpdateThread(screen, objects, i * WIDTH / THREADS, 0, WIDTH / THREADS, HEIGHT, p, h, v);
		}
		test();
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (Exception e1) {
				System.out.println("Something went wrong");
			}
		}
		repaint();

		// Collect data.
		long currentTime = (System.nanoTime() - startTime);
		avgTime -= avgTime * 1 / samples;
		avgTime += currentTime * 1 / samples;
		System.out.println("Overall took " + currentTime + " nanoseconds to draw");
	}

	// Main Driver for program.
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setLocation(0, 0);
		frame.setSize(W, H);
		frame.setUndecorated(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new MainPanel());
		frame.setVisible(true);
	}
}
