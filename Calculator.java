import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * “Exceptional calculator” to do integer arithmetic.
 * @author Rafael Lucchesi
 * @version March 22nd 2017
 */
public class Calculator {
	private int operand1;
	private int operand2;
	private String operator;
	
	/**
	 * The method assigns the string received as parameter to the calculator's private variables. 
	 * The String parameter is divided and later assigned into operand1, operator and operand2 to be later used
	 * to calculate the arithmetic operation.
	 * @param input String containing the operands and the operator.
	 * @throws InputMismatchException In case of operand type being different from integer or if more then two
	 * operands and a operator is used.
	 * @throws NoSuchElementException In case of a missing operator/operand.
	 */
	public void setInput(String input) throws InputMismatchException, NoSuchElementException {
		/*
		 * The strategy here was to treat each component of the operation separately, in order to produce
		 * specific error messages. 
		 */
		try (Scanner scanner = new Scanner(input)) {
			try {
				operand1 = scanner.nextInt();
			} catch (InputMismatchException e) {
				throw new InputMismatchException("The first operand is not an integer");
			} catch (NoSuchElementException e) {
				throw new NoSuchElementException("The first operand is missing");
			}
				
			try {
				operator = scanner.next();
			} catch (NoSuchElementException e) {
				throw new NoSuchElementException("The operator and second operand are missing");
			}

			try {
				operand2 = scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("The second operand is not an integer");
				throw new InputMismatchException("The second operand is not an integer");
			} catch (NoSuchElementException e) {
				throw new NoSuchElementException("The second operand is missing");
			}

			/*
			 * InputMismatchException thrown if the string has "extra" data.
			 * NoSuchElementException is expected if the input string is correctly formated.
			 */
			try {
				scanner.next();
				throw new InputMismatchException();
			} catch (InputMismatchException e) {
				throw new InputMismatchException("More than just 2 operands and an operator");
			} catch (NoSuchElementException e) {
				return;
			}
		}
	}

	/**
	 * Method used to conduct the operation determined by the user. This method is only called when
	 * the instance variables were instantiated properly.
	 * @return The output of the arithmetic operation.
	 * @throws IllegalOperatorException In case of a operator not expected by the calculator.
	 * @throws ArithmeticException In case of a division or module by 0.
	 */
	public int calculate() throws IllegalOperatorException, ArithmeticException {
		int output;
		switch (operator) {
			case "+": output = operand1 + operand2; break;
			case "-": output = operand1 - operand2; break;
			case "*": output = operand1 * operand2; break;
			case "/": output = operand1 / operand2; break;
			case "%": output = operand1 % operand2; break;
			case "^": output = (int) Math.pow(operand1, operand2); break;
			default: throw new IllegalOperatorException("Illegal Operator \"" + operator + "\"");
		}
		return output;
	}
}