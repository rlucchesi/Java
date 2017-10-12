import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
import java.util.Random;
 
/**
 * CPSC 1181 Assignment #6: INHERITANCE AND POLYMORPHISM
 * This class represents a vehicle. A vehicle has a position corresponding
 * to the upper left hand corner of the enclosing rectangle, it has a width
 * and a height, and it has a color. The width and the height are of the 
 * enclosing rectangle (bounding box) anchored by its upper left hand 
 * corner.
 * @author Rafael Lucchesi
 * @Version March 1st 2017
 */
public abstract class Vehicle
{
   /*
   * The default value for the width and height of the vehicle's enclosing square.
   */
   public static final int DEFAULT_VEHICLE_SIDE = 200;

   /*
   * The default color of the vehicle.
   */
   public static final Color DEFAULT_COLOR = Color.RED;
   
   /*
    * The variable indicates the maximum number of attempts made when trying to place a vehicle.
    */
   public static final int MAXIMUM_ATTEMPT_DRAW = 200;
   
   /*
    * The Random number generator variable.
    */
   private static final Random NUMBER_GENERATOR = new Random();
   
   /*
    * Instance variables
    */
   private int xCoord;
   private int yCoord;
   private int width;
   private int height;
   private Color color;
   private int displacementX;
   private int displacementY;
   
   /**
   * Sets the vehicle's bounding rectangle's sides to the
   * <code><a href="Vehicle.html#DEFAULT_VEHICLE_SIDE">DEFAULT_VEHICLE_SIDE</a></code>,
   * the vehicles'color to 
   * <code><a href="Vehicle.html#DEFAULT_COLOR">DEFAULT_COLOR</a></code>,
   * and the upper left hand corner of the bounding rectangle to the origin.
   */
   public Vehicle()
   {
	   this.xCoord = 0;
	   this.yCoord = 0;
	   this.color = DEFAULT_COLOR;
	   this.width = DEFAULT_VEHICLE_SIDE;
	   this.height = DEFAULT_VEHICLE_SIDE;
	   this.displacementX = 1;
	   this.displacementY = 1;
   }
   
   /**
    * Sets the vehicle's bounding rectangle to be of dimensions w x h with
    * (x,y) as upper left hand corner and the vehicle's colour to c. 
    * @param x the x-coordinate of the enclosing rectangle's upper left hand corner
    * @param y the y-coordinate of the enclosing rectangle's upper left hand corner 
    * @param c the colour of the vehicle
    * @param w the width of the enclosing rectangle
    * @param h the height of the enclosing rectangle
    * @param speed the displacement speed for the animation
    */
   public Vehicle(int x, int y, Color c, int w, int h, int speed)
   {
	   this.xCoord = x;
	   this.yCoord = y;
	   this.color = c;
	   this.width = w;
	   this.height = h;
	   this.displacementX = speed;
	   this.displacementY = speed;
   }
   
   /**
   * Draws the vehicle onto the graphics context using the 
   * previously set colour and position of the enclosing rectangle.
   * @param g2 the graphics context
   */
   public abstract void draw(Graphics2D g2);

   /**
   * Sets the position of the vehicle by setting the upper left hand corner 
   * of the bounding box of the vehicle to be the point (x,y).
   * @param x the x-coordinate of the enclosing rectangle's upper left hand corner
   * @param y the y-coordinate of the enclosing rectangle's upper left hand corner 
   */
   public void setPosition(int x, int y)
   {
	   this.xCoord = x;
	   this.yCoord = y;
   }
   
   /**
    * Assessor method for the Vehicle's x-coordinate position
    * @return An integer holding the x-coordinate.
    */
   public int getPositionX() {
	   return this.xCoord;
   }
   
   /**
    * Assessor method for the Vehicle's y-coordinate position
    * @return An integer holding the y-coordinate.
    */
   public int getPositionY() {
	   return this.yCoord;
   }
   
   /**
   * Sets the colour of the vehicle.
   * @param c a specified Color
   */
   public void setColor(Color c)
   {
	   this.color = c;
   }
   
   /**
   * @return the colour of the vehicle.
   */
   public Color getColor()
   {
	   return this.color;
   }

   /**
   * @return the string consisting of the name of the class, the upper left hand corner of the 
   * enclosing rectangle, its color, and the dimensions of the enclosing rectangle
   */
   @Override
   public String toString()
   {
	   return "[" + getClass() + ": " + this.xCoord + ", " + this.yCoord + ", " + this.color + ", " + this.width + ", " + this.height + ", " + this.displacementX + ", " + this.displacementY + "]";
   }
   
   /**
   * Determines if two vehicles are equal.
   * @return true if obj is equal to this object based on the vehicle's
   * position (the position is defined by the upper left hand corner of the 
   * enclosing rectangle), by the dimensions of the enclosing rectangle, 
   * and by the vehicle's color
   */
   @Override 
   public boolean equals(Object obj) {
	   if (obj == null) {
		   return false;
	   }
	   if (obj instanceof Vehicle) {
		   // compare position
		   if (((Vehicle) obj).getPositionX() == this.getPositionX() && ((Vehicle) obj).getPositionY() == this.getPositionY()) {
			   // Dimensions
			   if (((Vehicle) obj).getWidth() == this.getWidth() && ((Vehicle) obj).getHeight() == this.getHeight()) {
				   //  Color
				   if (((Vehicle) obj).getColor() == this.getColor()) {
					   return true;
				   }
			   }
		   }
	   }
	   return false;
   }
   
   /**
    * Method used to determine if two vehicles are overlapping.
    * @param aVehicle Another vehicle object
    * @return True if colliding, false otherwise
    */
   public boolean isOverlapping(Vehicle aVehicle) {
	   Rectangle vehicle1 = new Rectangle(this.xCoord, this.yCoord, this.getWidth(), this.getHeight());
	   Rectangle testVehicle = new Rectangle(aVehicle.getPositionX(), aVehicle.getPositionY(), aVehicle.getWidth(), aVehicle.getHeight());
	   return vehicle1.intersects(testVehicle);
   }
   
   /**
    * Assessor method for the Vehicle's width
    */
   public int getWidth() {
	   return this.width;
   }
   
   /**
    * Assessor method for the Vehicle's height
    */
   public int getHeight() {
	   return this.height;
   }
   
   /**
    * Mutator method for the Vehicle's displacement in the X direction
    * @param dx The parameter determines the speed
    */
   public void setDisplacementX (int dx) {
	   this.displacementX = dx;
   }
   
   /**
    * Mutator method for the Vehicle's displacement in the Y direction
    * @param dy The parameter determines the speed
    */
   public void setDisplacementY (int dy) {
	   this.displacementY = dy;
   }
   
   /**
    * Assessor method for the Vehicle's displacement in the X direction
    * @return
    */
   public int getDisplacementX () {
	   return this.displacementX;
   }
   
   /**
    * Assessor method for the Vehicle's displacement in the Y direction
    * @return
    */
   public int getDisplacementY () {
	   return this.displacementY;
   }
   

   // Static methods bellow
   
   /**
    * The position of each vehicle is to be generated randomly
    * @return An integer array of size 2, which holds the x-coordinate at index 0 and y-coordinate at index 1. 
    */
   public static int[] xyGenerator (int xLimit, int yLimit) {
	   int[] xyCoordinate = new int[2];
	   
	   for (int i = 0; i < xyCoordinate.length; i++) {
		   switch (i) {
		   		case 0: xyCoordinate[i] = NUMBER_GENERATOR.nextInt(xLimit);
		   				break;
		   		case 1: xyCoordinate[i] = NUMBER_GENERATOR.nextInt(xLimit);
		   				break;
		   }
	   }
	   return xyCoordinate;
   }

   /**
    * The random generator for the vehicles' color
    * @return
    */
   public static Color colorGenerator() {
	   return new Color(NUMBER_GENERATOR.nextInt(256), NUMBER_GENERATOR.nextInt(256), NUMBER_GENERATOR.nextInt(256));
   }
   
   /**
    * Determines which vehicle to be generated.
    * @return 0 == Bicycle, 1 == Truck, 2 == Car
    */
   public static int typeGenerator () {
	   return NUMBER_GENERATOR.nextInt(3);
   }
}