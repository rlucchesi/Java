import javax.swing.JFrame;

/**
 * Write a Java Graphical User Interface (GUI) application program to do integer arithmetic by 
 * obtaining from the user an expression that is to be calculated. Your calculator should handle
 * Mathematical expressions consisting of two integer operands and a binary operator. There
 * must be at least one blank before and after the operator.
 * The result of the expression must be an integer (even for exponentiation). However, you may
 * ignore overflow (you don’t need to use long).
 * Your program should support unary pluses and unary minuses although you may assume that
 * there is no blank between a unary plus and the integer and similarly you may assume that there
 * is no blank between the integer and a unary minus.
 * @author Rafael Lucchesi
 * @version March 22nd 2017
*/
public class CalculatorViewer {
	public static void main(String[] args) {
		JFrame frame = new CalculatorGUI();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Exceptional Calculator");
		frame.setResizable(false);
		frame.setVisible(true);
	}
}