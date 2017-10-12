import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * CPSC 1181 Assignment #6: INHERITANCE AND POLYMORPHISM
 * Car Class.
 * @author Rafael Lucchesi
 * @Version March 1st 2017
 */
public class Car extends Vehicle {

	public static final int WIDTH = 100;
	public static final int HEIGHT = 60;
	
	/**
	 * Car Constructor.
	 */
	public Car (int x, int y, int speed) {
		super(x, y, Vehicle.colorGenerator(), WIDTH, HEIGHT, speed);
	}
	
	/**
	 * The class has an instance method draw so that the car can be drawn on a JComponent using the Graphics2D.
	 * @param g2
	 */
	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(this.getColor());
		// enclosing rectangle
		g2.draw(new Rectangle2D.Double(this.getPositionX(), this.getPositionY(), this.getWidth(), this.getHeight()));
		
		// Rectangle refers to the lower part of the body
		g2.fill(new Rectangle2D.Double(this.getPositionX(), this.getPositionY() + this.getHeight() * 2 / 5, this.getWidth(), this.getHeight() * 2 / 5));
		// Rectangle refers to the upper part of the body
		g2.fill(new Rectangle2D.Double(this.getPositionX() + this.getWidth() / 4, this.getPositionY(), this.getWidth() * 2 / 4, this.getHeight() * 2 / 5));
		
		// Color to draw the details
		g2.setColor(Color.BLACK);
		// Front Tire
		g2.fillArc(this.getPositionX() + this.getWidth() / 6, this.getPositionY() + getHeight() * 3 / 5, getWidth() / 4, getWidth() / 4, 0, 360);
		// Rear Tire
		g2.fillArc(this.getPositionX() + this.getWidth() * 4 / 6, this.getPositionY() + getHeight() * 3 / 5, getWidth() / 4, getWidth() / 4, 0, 360);
		// Light
		g2.draw(new Rectangle2D.Double(this.getPositionX(), this.getPositionY() + this.getHeight() * 2 / 5, this.getWidth() / 12, this.getWidth() / 12));
		// Front Window 
		g2.draw(new Rectangle2D.Double(this.getPositionX() + 2 + this.getWidth() / 4, this.getPositionY() + 2, this.getWidth() / 5 , this.getHeight() * 2 / 5 - 2));
		// Rear Window
		g2.draw(new Rectangle2D.Double(this.getPositionX() + 4 + ( 9 * this.getWidth() / 20), this.getPositionY() + 2, this.getWidth() / 5 , this.getHeight() * 2 / 5 - 2));
	}
}
