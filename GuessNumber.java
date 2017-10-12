/**
 * Write a Java program that lets the user choose a level of difficulty for the game.
 * Subsequently, your program is:
 * 		to ask the user, with a graphical message box,
 * 		to think of a number to ask the user to perform operations on that number
 * 			(again the instructions are given graphically)
 * 		to ask the user to give to the program the result of the operations
 * 		to tell the user what his/her mystery number is
 * 		to ask the user whether the user wants to play again and to repeat the guessing
 *			game until the user chooses to stop the process
 *
 * @author Rafael Lucchesi
 * Version: Jan 10 2017
 */

import javax.swing.JOptionPane;
public class GuessNumber {
	
	/**
	 * The main method will keep the game running until the user hits CANCEL.
	 * @param args
	 */
	public static void main(String[] args) {
		boolean quit = false;
		int initialMenuChoice;
		
		do {
			initialMenuChoice = initialMenu("Select the level of difficulty or hit CLOSE (X) to quit", "Welcome to Guess Number Game!");
			switch (initialMenuChoice) {
				case -1:
					quit = true;
					break;
				case 0:
					runEasy();
					break;
				case 1:
					runMedium();
					break;
				case 2:
					runHard();
					break;
			}
			
			if (initialMenuChoice > -1) {
				if (!keepPlaying()) {
					displayMessage("Thank you for playing!");
					quit = true;
				}
			}
		
		} while (!quit);
	}
	
	/**
	 * This function is used to display a dialog box with the initial Menu, so the user can choose the level of difficulty.
	 * Source: http://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
	 * @param message // String to be displayed inside the dialog box.
	 * @param title // String to be displayed as the title of the dialog box 
	 * @return INT 0 = easy, 1 = medium, 2 = hard, -1 = close.
	 */
	private static int initialMenu (String message, String title) {
		Object[] level = {"Easy Mode",
		                    "Medium Mode",
		                    "Hard Mode"};
		int userChoice = JOptionPane.showOptionDialog(null,
		    message,
		    title,
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.PLAIN_MESSAGE,
		    null,
		    level,
		    level[2]);
		return userChoice;
	}

	/**
	 * This function will display a dialog window to interact with the user, expecting some type of user Input.
	 * @param message // String with the message to be displayed.
	 * @param title // String with the title of the window.
	 * @return userInput // String that the user has entered.
	 */
	private static String userInputDialog(String message, String title) {
		String userInput = JOptionPane.showInputDialog(
				null,
				message,
				title,
				JOptionPane.QUESTION_MESSAGE);
		return userInput;
	}
	
	/**
	 * This function will display a dialog window with a custom message.
	 * @param message // String with the message to be displayed.
	 */
	private static void displayMessage (String message) {
		JOptionPane.showMessageDialog(null, message);
	}
	
	/**
	 * This function will display a dialog window with the original number the user thought of.
	 * @param result // Int carrying the result of the calculation.
	 */
	private static void displayResult (int result) {
		displayMessage("The original number you thought of was: " + result);
	}
	
	/**
	 * This function will display a OKCANCEL dialog window for the user, along with a custom message.
	 * @param message // String with the message to be displayed.
	 * @param title // String with the title of the window.
	 * @return // OK = true, CANCEL = false.
	 */
	private static boolean okCancel (String message, String title) {
		int output = JOptionPane.showConfirmDialog(null, message,
		        title, JOptionPane.OK_CANCEL_OPTION);
		if (output == JOptionPane.OK_OPTION) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * This function runs the game in the Easy mode. The function isValidInput() is called to test the input and the calculations are done to determine the original number.
	 * When the user gets the right answer, the function displayResult() is called.
	 */
	private static void runEasy() {
		boolean quit = false;
		String calculatedInputStr;
		int result;
		
		do {
			calculatedInputStr = userInputDialog("Think of any integer, multiply it by 10 and enter the result bellow (hit CANCEL to go back to Menu)", "Easy Mode");
			
			if (isValidInput(calculatedInputStr, '0', '9')) {
				result = Integer.parseInt(calculatedInputStr);
				if (result % 10 == 0) {
					result /= 10;
					displayResult(result);
					quit = true;
				} else {
					displayMessage("ERROR: unexpected input. Check your calculations and try again.");
				}
			} else {
				if (calculatedInputStr == null) {
					quit = true;
				}
			}
		} while (!quit);
	}
	
	/**
	 * This function runs the game in the Medium mode. The function isValidInput() is called to test the input and the calculations are done to determine the original number.
	 * When the user gets the right answer, the function displayResult() is called.
	 */
	private static void runMedium() {
		boolean quit = false;
		String calculatedInputStr;
		int result;
		
		do {
			calculatedInputStr = userInputDialog("Think of any integer, multiply it by 18 and add 77 to it. Enter the result bellow (hit CANCEL to go back to Menu)", "Medium Mode");
			
			if (isValidInput(calculatedInputStr, '0', '9')) {
				result = Integer.parseInt(calculatedInputStr);
				if ((result - 77) % 18 == 0) {
					result = (result - 77) / 18;
					displayResult(result);
					quit = true;
				} else {
					displayMessage("ERROR: unexpected input. Check your calculations and try again.");
				}
			} else {
				if (calculatedInputStr == null) {
					quit = true;
				}
			}
		} while (!quit);
	}
	
	/**
	 * This function runs the game in the Hard mode. The function isValidInput() is called to test the input and the calculations are done to determine the original number.
	 * When the user gets the right answer, the function displayResult() is called.
	 */
	private static void runHard() {
		boolean quit = false;
		String calculatedInputStr;
		int result;
		
		do {
			calculatedInputStr = userInputDialog("Think of any integer, multiply it by 17, add 28, multiply again by 4 and subtract 28 from it all. Enter the result bellow (hit CANCEL to go back to Menu)", "Hard Mode");
			
			if (isValidInput(calculatedInputStr, '0', '9')) {
				result = Integer.parseInt(calculatedInputStr);
				if ((result - 84) % 68 == 0) {
					result = (result - 84) / 68;
					displayResult(result);
					quit = true;
				} else {
					displayMessage("ERROR: unexpected input. Check your calculations and try again.");
				}
			} else {
				if (calculatedInputStr == null) {
					quit = true;
				}
			}
		} while (!quit);
	}
	
	/**
	 * This function will test the input data, in order treat every possible value entered.
	 * @param input // String entered by the user.
	 * @param min // used to limit the range of inputs expected by the user.
	 * @param max // used to limit the range of inputs expected by the user.
	 * @return
	 */
	private static boolean isValidInput (String input, char min, char max) {
		if (input == null) {
			return false;
		} else if (input.equals("")) {
			displayMessage("ERROR: No input");
			return false;
		} else if (!isInteger(input, min, max)) {
			displayMessage("ERROR: Non-valid input data");
			return false;
		} else {
			return true;
		}
	}

	/**
	 * This function asks if the user wants to keep playing.
	 * @return OK = true, CANCEL = false.
	 */
	private static boolean keepPlaying() {
		boolean userChoice = okCancel("Would you like to keep playing?", "Message");
		return userChoice;
	}
	
	/**
	 * This function tests if every char of an input String is an Integer, dealing with signed numbers.
	 * OBS: The "null" input is treated prior to this function's call. 
	 * @param inputStr // String used as input.
	 * @param min // lower limit of the range.
	 * @param max // upper limit of the range.
	 * @return // true/false.
	 */
	private static boolean isInteger(String inputStr, char min, char max) {
		int initialPosition = 0;
		if (inputStr.charAt(0) == '+' || inputStr.charAt(0) == '-') {
			initialPosition = 1;
		}
		for (int i = initialPosition; i < inputStr.length(); i++) {
			if (inputStr.charAt(i) < min || inputStr.charAt(i) > max) {
				return false;
			}
		}
		return true;
	}
}