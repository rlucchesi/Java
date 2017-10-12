import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JComponent;

/**
 * CPSC 1181 Assignment #6: INHERITANCE AND POLYMORPHISM
 * The VehicleComponent holds the vehicles and methods related to their animation.
 * Based on the course material "Big Java: Early Objects", 6th ed., chapter 10.9.
 * @author Rafael Lucchesi
 * @Version March 1st 2017
 */
public class VehicleComponent extends JComponent {
	/*
     * Instance variables
     */
	private ArrayList<Vehicle> vehicleList;
	private int numVehiclesToDraw;
	private int currentAttempt;				// Counts the number of attempts to draw a vehicle
	private int componentWidth;
	private int componentHeight;

	/**
	 * Constructor of the VehicleComponent.
	 * @param aNumVehicles The number of vehicles to be drawn
	 */
	public VehicleComponent(int aNumVehicles) {
		this.numVehiclesToDraw = aNumVehicles;
		this.vehicleList = new ArrayList<Vehicle>();
		this.currentAttempt = 0;
		this.componentWidth = getWidth();
		this.componentHeight = getHeight();
	}
	
	/**
	 * This is where each object is created and drawn on a JComponent using the Graphics2D.
	 */
	@Override
	public void paintComponent(Graphics g) {		
		int[] xyTestCoords = new int[2];	// Holds x- and y-coordinates to be tested for collision
		int currentVehicleType;				// Holds the randomly generated type of vehicle to be created: 0 = Bike, 1 = Truck, 2 = Car
		Vehicle newVehicle;					// Holds a vehicle object to be tested for overlap
		boolean overlap = false;
		Graphics2D g2 = (Graphics2D) g;
		int currentWidth = getWidth();
		int currentHeight = getHeight();
		
		if (this.componentWidth != currentWidth || this.componentHeight != currentHeight) {
			this.componentWidth = currentWidth;
			this.componentHeight = currentHeight;
			this.currentAttempt = 0;
		}
		
		/**
		 * Loop used to generate a new vehicle and place/discard it based on the test for overlap.
		 */
		while (this.currentAttempt < Vehicle.MAXIMUM_ATTEMPT_DRAW && this.vehicleList.size() < this.numVehiclesToDraw) {
			/*
			 * Generate a new vehicle
			 */
			currentVehicleType = Vehicle.typeGenerator();
			switch (currentVehicleType) {
				case 0: xyTestCoords = Vehicle.xyGenerator(getWidth() - Bicycle.WIDTH, getHeight() - Bicycle.HEIGHT);
						newVehicle = new Bicycle(xyTestCoords[0], xyTestCoords[1], 1);
						break;
				case 1: xyTestCoords = Vehicle.xyGenerator(getWidth() - Truck.WIDTH, getHeight() - Truck.HEIGHT);
						newVehicle = new Truck(xyTestCoords[0], xyTestCoords[1], 1);
						break;
				default:xyTestCoords = Vehicle.xyGenerator(getWidth() - Car.WIDTH, getHeight() - Car.HEIGHT);
						newVehicle = new Car(xyTestCoords[0], xyTestCoords[1], 1);
						break;
			}
			
			/*
			 * Overlap testing
			 */
			for (int i = 0; i < this.vehicleList.size() && !overlap; i++) {
				if (newVehicle.isOverlapping(this.vehicleList.get(i))) {
					overlap = true;
				}
			}
				
			/*
			 * Place/Discard vehicle
			 */
			if (!overlap) {
				this.currentAttempt = 0;
				this.vehicleList.add(newVehicle);
			} else {
				this.currentAttempt++;
			}
			overlap = false;
		}
		
		/*
		 * Draw the Vehicles generated previously
		 */
		for (int i = 0; i < this.vehicleList.size(); i++) {
			this.vehicleList.get(i).draw(g2);
		}
	}

	/**
	 * This method is responsible for moving the vehicles, check for collisions to the frame edges and other vehicles, and repaint all the instantiated objects.
	 * Hint from Gladys's former student's code: Anticipate the position of the car and test it.
	 */
	public void moveVehicle() {
		int x11, x12, y11, y12, x21, x22, y21, y22;			// variables of the format Coordinate_ObjectNum_Initial/Final to promote readability 
		Vehicle anticipatedVehicleArea;						// Vehicle with its x- and y-displacement computed to predict collisions 
		
		for (int i = 0; i < this.vehicleList.size(); i++) {
			x11 = this.vehicleList.get(i).getPositionX();
			x12 = this.vehicleList.get(i).getPositionX() + this.vehicleList.get(i).getWidth();
			y11 = this.vehicleList.get(i).getPositionY();
			y12 = this.vehicleList.get(i).getPositionY() + this.vehicleList.get(i).getHeight();
			
			anticipatedVehicleArea = this.vehicleList.get(i);
			anticipatedVehicleArea.setPosition(x11 + this.vehicleList.get(i).getDisplacementX(), y11 + this.vehicleList.get(i).getDisplacementY());
			
			/*
			 * Checks for collisions with the frame edges
			 */
			if (anticipatedVehicleArea.getPositionX() + anticipatedVehicleArea.getWidth() > getWidth()) {
				this.vehicleList.get(i).setDisplacementX(-1);
			}
			if (anticipatedVehicleArea.getPositionX() < 0) {
				this.vehicleList.get(i).setDisplacementX(1);
			}
			if (anticipatedVehicleArea.getPositionY() + anticipatedVehicleArea.getHeight() > getHeight()) {
				this.vehicleList.get(i).setDisplacementY(-1);
			}
			if (anticipatedVehicleArea.getPositionY() < 0) {
				this.vehicleList.get(i).setDisplacementY(1);
			}
			
			/*
			 * Checks for collision between objects
			 */
			for (int j = 0; j < this.vehicleList.size(); j++) {
				x21 = this.vehicleList.get(j).getPositionX();
				x22 = this.vehicleList.get(j).getPositionX() + this.vehicleList.get(j).getWidth();
				y21 = this.vehicleList.get(j).getPositionY();
				y22 = this.vehicleList.get(j).getPositionY() + this.vehicleList.get(j).getHeight();
				
				if (!this.vehicleList.get(i).equals(this.vehicleList.get(j))) {
					if (anticipatedVehicleArea.isOverlapping(this.vehicleList.get(j))) {
						if (Math.abs(x11 - x22) < 2) {
							// right
							this.vehicleList.get(i).setDisplacementX(this.vehicleList.get(i).getDisplacementX() * -1);
							this.vehicleList.get(j).setDisplacementX(this.vehicleList.get(j).getDisplacementX() * -1);
						}
						if (Math.abs(y11 - y22) < 2) {
							// top
							this.vehicleList.get(i).setDisplacementY(this.vehicleList.get(i).getDisplacementY() * -1);
							this.vehicleList.get(j).setDisplacementY(this.vehicleList.get(j).getDisplacementY() * -1);
						}
						if (Math.abs(x12 - x21) < 2) {
							// left
							this.vehicleList.get(i).setDisplacementX(this.vehicleList.get(i).getDisplacementX() * -1);
							this.vehicleList.get(j).setDisplacementX(this.vehicleList.get(j).getDisplacementX() * -1);
						}
						if (Math.abs(y12 - y21) < 2) {
							// bottom
							this.vehicleList.get(i).setDisplacementY(this.vehicleList.get(i).getDisplacementY() * -1);
							this.vehicleList.get(j).setDisplacementY(this.vehicleList.get(j).getDisplacementY() * -1);
						}
					} 
				}
			}
			
			/*
			 * Actual computation of the vehicle's displacement, and the repaint method call
			 */
			this.vehicleList.get(i).setPosition(this.vehicleList.get(i).getPositionX() + this.vehicleList.get(i).getDisplacementX(), this.vehicleList.get(i).getPositionY() + this.vehicleList.get(i).getDisplacementY());
			repaint();
		}
	}
}