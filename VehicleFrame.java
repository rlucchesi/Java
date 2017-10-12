import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * CPSC 1181 Assignment #6: INHERITANCE AND POLYMORPHISM
 * This frame contains the moving vehicles.
 * Based on the course material "Big Java: Early Objects", 6th ed., chapter 10.9.
 * @author Rafael Lucchesi
 * @Version March 1st 2017
 */
public class VehicleFrame extends JFrame {
	/*
	 * Initial Frame size
	 */
	private static final int FRAME_WIDTH = 300;
	private static final int FRAME_HEIGHT = 400;

	private VehicleComponent scene;

	/**
	 * This class holds the actions to be executed during the frame's animation. 
	 */
	class TimerListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			scene.moveVehicle();
		}
	}

	/**
	 * The Timer and ActionListener are created in this Frame. "The Timer class (javax.swing package) is used to generate a sequence of action events,
	 * spaced at even time intervals that implements the ActionListener interface."
	 * @param aNumVehiclesToDraw The number of Vehicles to be drawn.
	 */
	public VehicleFrame(String aNumVehiclesToDraw) {
		final int DELAY = 100; // Milliseconds between timer ticks
		
		scene = new VehicleComponent(Integer.parseInt(aNumVehiclesToDraw));
		add(scene);

		setSize(FRAME_WIDTH, FRAME_HEIGHT);

		ActionListener listener = new TimerListener();
		
		Timer t = new Timer(DELAY, listener);
		t.start();
	}
}
