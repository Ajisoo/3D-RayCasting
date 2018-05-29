/*
 * This file is used for static calculation equations involving 3D Geometry.
 * All functions have distance using the Point class or x,y,z values.
 */

public class Calc {
	
	// Dot Product of two vectors in (x,y,z)(x,y,z) form.
	public static double dotProduct(double x1, double y1, double z1, double x2, double y2, double z2){
		return x1*x2 + y1*y2 + z1*z2;
	}
	
	// Dot Product of two vectors in Point, Point form.
	public static double dotProduct(Point p1, Point p2){
		return p1.getX()*p2.getX()+p1.getY()*p2.getY()+p1.getZ()*p2.getZ();
	}
	
	// Cross Product of two vectors in (x,y,z)(x,y,z) form.
	public static Point crossProduct(double x1, double y1, double z1, double x2, double y2, double z2){
		return new Point(y1*z2-y2*z1,x2*z1-x1*z2,x1*y2-y1*x2);
	}
	
	// Cross Product of two vectors in Point, Point form.
	public static Point crossProduct(Point p1, Point p2){
		return crossProduct(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ());
	}
	
	// Distance between two points in (x,y,z)(x,y,z) form.
	public static double dist(double x1, double y1, double z1, double x2, double y2, double z2){
		return Math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2) + (z1 - z2)*(z1 - z2));
	}
	
	// Distance between two points in Point, Point form.
	public static double dist(Point p1, Point p2){
		return dist(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ());
	}
	
	// Distance from origin to point in (x,y,z) form.
	public static double dist(double x1, double y1, double z1){
		return Math.sqrt(x1*x1 + y1*y1 + z1*z1);
	}
	
	// Distance from origin to point in Point form.
	public static double dist(Point p1){
		return dist(p1.getX(), p1.getY(), p1.getZ());
	}
}
