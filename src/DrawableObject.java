import java.awt.Color;

/*
 * This class is an abstract class laying out the framework to build objects
 * that can be drawn in 3 dimensions.
 */

public abstract class DrawableObject {
	
	// An object can by hit by a vector with a starting location, this function returns the smallest
	// positive distance from x,y,z to the object. Returns -1 if not hit.
	public abstract double isHit(double x, double y, double z, double xv, double yv, double zv);
	
	// Same as above, but using the Point class
	public double isHit(Point p, Point v){
		return isHit(p.getX(), p.getY(), p.getZ(), v.getX(), v.getY(), v.getZ());
	}
	
	// Returns the color of the Drawable object at point x,y,z (assumed to be on the object)
	public abstract Color getColor(double x, double y, double z);
	
	// Same as above, but using the Point class
	public Color getColor(Point p){
		return getColor(p.getX(), p.getY(), p.getZ());
	}
	
	// Filters a color when a beam of light flows through this object
	public abstract Color filterColor(Color c);
	
	/*
	 * Possibilities in the future:
	 * 		Have filterColor take x,y,z like getColor in order to more accurately filter a color.
	 */
}
