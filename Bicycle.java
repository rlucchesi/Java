import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;

/**
 * CPSC 1181 Assignment #6: INHERITANCE AND POLYMORPHISM
 * Bicycle Class.
 * @author Rafael Lucchesi
 * @Version March 1st 2017
 */
public class Bicycle extends Vehicle {

	public static final int WIDTH = 80;
	public static final int HEIGHT = 40;
	
	/**
	 * Bicycle Constructor.
	 */
	public Bicycle (int x, int y, int speed) {
		super(x, y, Vehicle.colorGenerator(), WIDTH, HEIGHT, speed);
	}
	 
	/**
	 * The class has an instance method draw so that the car can be drawn on a JComponent using the Graphics2D.
	 * @param g2
	 */
	@Override
	public void draw(Graphics2D g2) {
		int[] xCoordPolygon = new int[] {this.getPositionX() + this.getWidth() / 10, this.getPositionX() + this.getWidth(), this.getPositionX() + this.getWidth() / 4};
		int[] yCoordPolygon = new int[] {this.getPositionY() + this.getHeight() * 4 / 5, this.getPositionY() + this.getHeight() * 4 / 5, this.getPositionY()};
		
		// Color to draw the Tire
		g2.setColor(Color.BLACK);
		// Rear Tire
		g2.fillArc(this.getPositionX() + this.getWidth() * 4 / 5, this.getPositionY() + getHeight() * 3 / 5, getWidth() / 5, getWidth() / 5, 0, 360);
		
		// Color of the Bicycle
		g2.setColor(this.getColor());
		// enclosing rectangle
		g2.draw(new Rectangle2D.Double(this.getPositionX(), this.getPositionY(), this.getWidth(), this.getHeight()));
		
		// Triangle representing the Bicycle frame 
		g2.fillPolygon(new Polygon(xCoordPolygon, yCoordPolygon, 3));
		
		// Color to draw the Tire
		g2.setColor(Color.BLACK);
		// Front Tire
		g2.fillArc(this.getPositionX() , this.getPositionY() + getHeight() * 3 / 5, getWidth() / 5, getWidth() / 5, 0, 360);
	}
}
