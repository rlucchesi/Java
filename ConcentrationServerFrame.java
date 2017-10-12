import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * The second JFrame will serve as a tool for a human to be the “pretend server”. The human will select
 * commands from the combo box to simulate the server actions.
 * @author Rafael Lucchesi
 * @version March 29th 2017
 */
public class ConcentrationServerFrame extends JFrame implements NetworkProtocol {
	private ConcentrationClientFrame clientA;
	
	private JComboBox<String> boxCommandMenu;
	private String[] comboBoxItemsList = {	"-- Server Command Menu --",
											"Client - Set ID and Connection (clientId)",
											"Client - Set Turn (clientId / boolean)",
											"Client - Set Winner (clientId / boolean)", 
											"Card - Disable Card (clientId / cardNum, boolean)",
											"Card - Update Label (clientId / cardNum, labelMessage)", 
											"Card - Turn Card (clientId / cardNum)", 
											"Update - Score Label (clientId / scoreNum)", 
											"Update - Communication Field (clientID / String)",
											"Game - Test Client's Guess (end of client's turn)", 
											"Game - Set GameOver (clientID / boolean)"};
	
	private JLabel labelSelectPlayer, labelInput;
	private JTextField fieldSelectPlayer, fieldInput;
	private JButton buttonSend;
	private JPanel panelServer;
	
	private String serverToClient;
	
	/**
	 * Server Constructor
	 */
	public ConcentrationServerFrame () {
		boxCommandMenuInitializer();
		labelInput();
		textFieldInitializer();
		buttonInitializer();
		panelInitializer();
	}
	
	private void boxCommandMenuInitializer() {
		boxCommandMenu = new JComboBox<String>(comboBoxItemsList);
		boxCommandMenu.setSelectedIndex(0);
	}
	
	private void labelInput() {
		labelSelectPlayer = new JLabel("Player: ");
		labelSelectPlayer.setHorizontalAlignment(JTextField.RIGHT);
		labelInput = new JLabel("Argument: ");
		labelInput.setPreferredSize(new Dimension(80, 25));
		labelInput.setHorizontalAlignment(JTextField.RIGHT);
	}
	
	private void textFieldInitializer() {
		fieldSelectPlayer = new JTextField();
		fieldSelectPlayer.setPreferredSize(new Dimension(160, 25));
		fieldInput = new JTextField();
		fieldInput.setPreferredSize(new Dimension(160, 25));
	}
	
	private void buttonInitializer() {
		buttonSend = new JButton("Send");
		buttonSend.setPreferredSize(new Dimension(80, 25));
		buttonSend.addActionListener(e -> executeServerCommand());
	}
	
	private void panelInitializer() {
		panelServer = new JPanel(new GridLayout(2, 1));
		panelServer.setBorder(new EmptyBorder(10, 10, 10, 10));
		JPanel panelInput = new JPanel();
		panelInput.add(labelSelectPlayer);
		panelInput.add(fieldSelectPlayer);
		panelInput.add(labelInput);
		panelInput.add(fieldInput);
		panelInput.add(buttonSend);
		
		panelServer.add(boxCommandMenu);
		panelServer.add(panelInput);
		
		getContentPane().add(panelServer);
	}
	
	/**
	 * Used to enable control over the Client's frame
	 * @param aPlayer
	 */
	public void addPlayer(ConcentrationClientFrame aPlayer) {
		this.clientA = aPlayer;
	}
	
	/**
	 * Reads and interprets inputs from the Server frame and send them to the client's frame.
	 * The necessary input is described in the title between parenthesis.
	 */
	public void executeServerCommand() {
		String command = (String) boxCommandMenu.getSelectedItem();

		switch (command) {
			case "Client - Set ID and Connection (clientId)":
				this.clientA.setClientId(this.fieldSelectPlayer.getText());
				this.serverToClient = "Server -> Client" + this.fieldSelectPlayer.getText() + ": ";
				System.err.println(serverToClient + "Set ID = " + this.fieldSelectPlayer.getText());
				break;
				
			case "Client - Set Turn (clientId / boolean)":
				if (this.fieldSelectPlayer.getText().equals(this.clientA.getClientId())) {
					if (fieldInput.getText().equals("true")) {
						this.clientA.setClientTurn(true);
					} else {
						this.clientA.setClientTurn(false);
					}
					System.err.println(serverToClient + "Set client's turn = " + (fieldInput.getText().equals("true")));
				}
				break;
				
			case "Client - Set Winner (clientId / boolean)":
				if (this.fieldSelectPlayer.getText().equals(this.clientA.getClientId())) {
					if (fieldInput.getText().equals("true")) {
						this.clientA.setWinner(true);
					} else {
						this.clientA.setWinner(false);
					}
					System.err.println(serverToClient + "Set client as Winner = " + (fieldInput.getText().equals("true")));
				}
				break;
				
			case "Card - Disable Card (clientId / cardNum, boolean)":
				if (this.fieldSelectPlayer.getText().equals(this.clientA.getClientId())) {
					// [0] == cardNum, [1] == boolean
					String[] treatedArgument = multiArgument(fieldInput.getText());
					this.clientA.disableCard(Integer.parseInt(treatedArgument[0]), (treatedArgument[1].equals("true")));
					System.err.println(serverToClient + "Disabled Card number " + treatedArgument[0] + " -- set to " + treatedArgument[1]);
				}
				break;

			case "Card - Update Label (clientId / cardNum, labelMessage)":
				if (this.fieldSelectPlayer.getText().equals(this.clientA.getClientId())) {
					// [0] == cardNum, [1] == cardValue
					String[] treatedArgument = multiArgument(fieldInput.getText());
					this.clientA.setFrontCardLabel(Integer.parseInt(treatedArgument[0]), treatedArgument[1]);
					System.err.println("Server Update Card number " + treatedArgument[0] + " with the content: " + treatedArgument[1]);
				}
				break;
				
			case "Card - Turn Card (clientId / cardNum)":
				if (this.fieldSelectPlayer.getText().equals(this.clientA.getClientId())) {
					int cardNum = Integer.parseInt(fieldInput.getText());
					if (this.clientA.isMyTurn()) {
						if (!this.clientA.isFirstCardFlipped() && !this.clientA.isSecondCardFlipped()) {
							this.clientA.flipCard(cardNum);
							this.clientA.setFirstCardFlipped(true);
							this.clientA.setFirstCardPosition(cardNum);
						} else if (this.clientA.isFirstCardFlipped() && !this.clientA.isSecondCardFlipped()) {
							this.clientA.flipCard(cardNum);
							this.clientA.setSecondCardFlipped(true);
							this.clientA.setSecondCardPosition(cardNum);
						}
						System.err.println(serverToClient + "Turn Card number " + cardNum);
					} else {
						this.clientA.flipCard(cardNum);
						System.err.println(serverToClient + "Turn Card number " + cardNum);
					}
				}
				break;
				
			case "Update - Score Label (clientId / scoreNum)":
				if (this.fieldSelectPlayer.getText().equals(this.clientA.getClientId())) {
					int score = Integer.parseInt(this.fieldInput.getText());
					this.clientA.setLabelScore(score);
					System.err.println(serverToClient + "Update Score Label -- Score: " + score);
				}
				break;
				
			case "Update - Communication Field (clientID / String)":
				if (this.fieldSelectPlayer.getText().equals(this.clientA.getClientId())) {
					this.clientA.setfieldCommunication(this.fieldInput.getText());
					System.err.println(serverToClient + "Update Communication Field: " + this.fieldInput.getText());
				}
				break;
				
			case "Game - Test Client's Guess (end of client's turn)":
				if (this.fieldSelectPlayer.getText().equals(this.clientA.getClientId())) {
					if (this.clientA.isSecondCardFlipped()) {
						this.clientA.setFirstCardFlipped(false);
						this.clientA.setSecondCardFlipped(false);
						if (this.clientA.areCardsTurnedEqual()) {
							this.clientA.setLabelScore((this.clientA.getClientScore()+1));
							this.clientA.disableCard(this.clientA.getFirstCardPosition(), true);
							this.clientA.disableCard(this.clientA.getSecondCardPosition(), true);
							System.err.println(serverToClient + "Cards Matched, Player scored!");
						} else {
							this.clientA.flipCard(this.clientA.getFirstCardPosition());
							this.clientA.flipCard(this.clientA.getSecondCardPosition());
							System.err.println(serverToClient + "Cards did not match");
						}
					}
					this.clientA.setClientTurn(false);
					System.err.println(serverToClient + "Set client's turn = " + false);
				}
				break;
			
			case "Game - Set GameOver (clientID / boolean)":
				if (this.fieldSelectPlayer.getText().equals(this.clientA.getClientId())) {
					if (fieldInput.getText().equals("true")) {
						this.clientA.setGameOver(true);
					} else {
						this.clientA.setGameOver(false);
					}
					System.err.println(serverToClient + "Game Over set to " + (fieldInput.getText().equals("true")));
				}
				break;
			default: return;
		}
		
	}
	
	/**
	 * Used to treat longer inputs
	 * @param aStr
	 * @return
	 */
	private String[] multiArgument(String aStr) {
		String[] output = new String[2];
		for (int i = 0; i < aStr.length(); i++) {
			if (aStr.charAt(i) == ',') {
				output[0] = aStr.substring(0, i).trim();
				output[1] = aStr.substring(i + 1).trim();
			}
			
		}
		return output;
	}
}
