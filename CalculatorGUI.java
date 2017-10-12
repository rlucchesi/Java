import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * In the GUI, have a JTextField so that the user can enter the expression to be computed.
 * Have a JButton that when clicked, calculates the expression typed in the JTextField. In addition, 
 * allow for the calculation to be made when the user hits “return” in the JTextField.
 * Display graphically the result of the calculation or display the error message.
 * Allow for the computation to be done when the user hits “return” (or “Enter”) on the 
 * JTextField and when the user clicks on a button that says “calculate”. 
 * You must pass a lambda expression to the method addActionListener of the JButton and to the 
 * addActionListener of the JTextField.
 * @author Rafael Lucchesi
 * @version March 22nd 2017
 */
public class CalculatorGUI extends JFrame {
	private static final int FRAME_WIDTH = 400;
	private static final int FRAME_HEIGHT = 250;

	private static final String WELCOME_OUTPUTFIELD = "\nExceptional Calculator - Integer Arithmetic\nOperations accepted: + - * / % ^";
	private static final String WELCOME_INPUTFIELD = "Sample operation: 2 + 2";
	private static final String TOOLTIP = "Operations accepted: + - * / % ^";
        
	private Calculator intCalculator;
	private JTextPane outputField;
	private JTextField inputField;
	private JButton button;
	
	/**
	 * Constructor used to setup the Calculator Graphical User Interface and instantiate
	 * one Exceptional calculator.
	 */
	public CalculatorGUI() {
		intCalculator = new Calculator();
		createOutputField();
		createInputField();
		createCalculateButton();
		createCalculatorPanel();

		setSize(FRAME_WIDTH, FRAME_HEIGHT);
	}

	/**
	 * Method used to setup the Output JTextPane field.
	 */
	private void createOutputField() {
		int FONT_SIZE = 14;
		Font outputFont;
		
		outputField = new JTextPane();
		outputFont = new Font("SansSerif", Font.BOLD, FONT_SIZE);
		
		/*
		 * The next four lines were obtained in the following URL. They give JTextPane a center alignment.
		 * http://stackoverflow.com/questions/3213045/centering-text-in-a-jtextarea-or-jtextpane-horizontal-text-alignment
		 */
		StyledDocument doc = outputField.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		outputField.setFont(outputFont);
		outputField.setText(WELCOME_OUTPUTFIELD);
		outputField.setEditable(false);
	}

	/**
	 * Method used to setup the Input JTextField field, determining the lambda function which responds to interactions.
	 */
	private void createInputField() {
		final int FIELD_WIDTH = 10;

		inputField = new JTextField(FIELD_WIDTH);
		inputField.setHorizontalAlignment(JTextField.CENTER);
		inputField.setText(WELCOME_INPUTFIELD);

		// Lambda function responsible for the keyboard interaction with the JTextField
		inputField.addActionListener(lambda -> userAction());
	}
	
	/**
	 * Method used to setup the Calculate Button, determining the lambda function which responds to the button click.
	 */
	private void createCalculateButton() {
		button = new JButton("Calculate"); 
		button.setToolTipText(TOOLTIP);

		// Lambda function responsible for the mouse interaction with the button
		button.addActionListener(lambda -> userAction());
	}
	
	/**
	 * Method used to setup the Calculator GUI Panel.
	 */
	private void createCalculatorPanel() {
		JPanel calculatorPanel = new JPanel();
		calculatorPanel.setLayout(new GridLayout(3, 1));
		calculatorPanel.add(outputField);
		calculatorPanel.add(inputField);
		calculatorPanel.add(button);
		add(calculatorPanel);
	}
	
	/**
	 * Method used to display the Error messages.
	 * @param aOutput
	 */
	private void setOutputField(String aStr) {
		// Change background color if an error message is printed.
		outputField.setBackground(Color.LIGHT_GRAY);
		outputField.setText("\n" + aStr);
	}
	
	/**
	 * Method used to display the computation output.
	 * @param aOutput
	 */
	private void setOutputField(int aOutput) {
		// Change background color to white to print a computation.
		outputField.setBackground(Color.WHITE);
		outputField.setText("\n" + aOutput);
	}
	
	/**
	 * Method to be invoked by lambda functions: Button clicks or Keyboard interaction.
	 * If any of the Exceptions are caught, the appropriate error message is sent to the output field.
	 */
	private void userAction() {
		try {
			intCalculator.setInput(inputField.getText());
			setOutputField(intCalculator.calculate());
		} catch (InputMismatchException e) {
			setOutputField(e.getMessage());
		} catch (NoSuchElementException e) {
			setOutputField(e.getMessage());
		} catch (ArithmeticException e) {
			setOutputField(e.getMessage());
		}
	}
}