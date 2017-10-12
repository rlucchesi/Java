import java.util.InputMismatchException;

/**
 * Define at least one “custom exception” class that you define yourself and use it in your program
 * @author Rafael Lucchesi
 * @version March 22nd 2017
 */
public class IllegalOperatorException extends InputMismatchException {
	
	/**
	 * Basic constructor to customize error messages.
	 * @param message The error message.
	 */
	public IllegalOperatorException(String message) {
		super(message);
	}
}