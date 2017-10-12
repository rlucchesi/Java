import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * CPSC 1181 Assignment #6: INHERITANCE AND POLYMORPHISM
 * VehicleViewer is used display the VehicleFrame and to obtain from the user,
 * using the JOptionPane class, the number of vehicles to be drawn.
 * @author Rafael Lucchesi
 * @Version March 1st 2017
 */
public class VehicleViewer {
	/**
	 * Function used to interact with the user.
	 * @param message The message to be displayed inside the window box.
	 * @param title The title of the window box.
	 * @return A string with the input from the user.
	 */
	private static String userInputDialog(String message, String title) {
		String userInput = JOptionPane.showInputDialog(
				null,
				message,
				title,
				JOptionPane.QUESTION_MESSAGE);
		if (userInput == null) {
			return "0";		// avoid crashing
		}
		return userInput;
	}
		
	/**
	 * The User interaction, the Frame and the VehicleFrame are created here. 
	 * @param args
	 */
	public static void main (String[] args) {
		String userInput = userInputDialog("How many vehicles should be displayed (use an integer)", "Vehicle Viewer");
		
		JFrame frame = new VehicleFrame(userInput);
		frame.setTitle("Animated Vehicles");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}