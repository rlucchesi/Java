import javax.swing.JFrame;

/**
 * For Assignment #9, both JFrames will be started from the same main application program.
 * @author Rafael Lucchesi
 * @version March 29th 2017
 */
public class ConcentrationGame {

	public static void main(String[] args) {
		/*
		 * Concentration Client Frame 
		 */
		ConcentrationClientFrame frameClient = new ConcentrationClientFrame();
		frameClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameClient.setTitle("Concentration Game - Client");
	    // Initial Frame Position to the middle of the screen (user friendly)
		frameClient.setLocationRelativeTo(null);
		frameClient.setResizable(false);
		frameClient.setVisible(true);


	    /*
		 * Concentration Server Frame 
		 */
		ConcentrationServerFrame frameServer = new ConcentrationServerFrame();
		frameServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameServer.setTitle("Concentration Game - Server");
		frameServer.pack();
	    // Initial Frame Position to the middle of the screen (user friendly)
		frameServer.setLocationRelativeTo(null);
		frameServer.setResizable(false);
		frameServer.setVisible(true);
		
		frameServer.addPlayer(frameClient);
	}
}
