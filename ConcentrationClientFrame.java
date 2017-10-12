import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

/**
 * The first JFrame will be the Graphical User Interface that the human player will use to play the game.
 * @author Rafael Lucchesi
 * @version March 29th 2017
 */
public class ConcentrationClientFrame extends JFrame implements NetworkProtocol {
	private final int WIDTH = 800;
	private final int HEIGHT = 600;
	
	private JTextField fieldCommunication;
	private JLabel labelScore, labelUser;
	private JButton buttonRestart, buttonQuit;
	private JPanel panelClient;
	
	private Dimension labelDimension = new Dimension(WIDTH / 4, 40);
	private Dimension fieldDimension = new Dimension(WIDTH / 2, 80);
	
	private final String MESSAGE_WELCOME = "Welcome";
	public final String MESSAGE_WAIT = "Opponent's turn";
	public final String MESSAGE_PLAY = "Your turn";
	public final String MESSAGE_CONNECTING = "Connecting...";
	public final String MESSAGE_WAITING = "Waiting for another player...";
	public final String MESSAGE_CONNECTED = "Connected";
	public final String MESSAGE_WINNER = "You Win!";
	public final String MESSAGE_LOSER = "You lose...";
	public final String MESSAGE_GAMEOVER = "Game Over\nThanks for playing!";
	
	private final int TOTAL_CARD_NUMBER = 4;
	private ArrayList<Card> cardList;
	
	private String clientId;
	private int clientScore;
	private boolean gameOver;
	
	private boolean isConnectedToServer;	
	private boolean myTurn;
	
	private boolean[] cardsFlipped = new boolean[2];
	private int[] matchTestCardsPosition = new int[2];
	
	private boolean winner;
	
	/**
	 * Client Frame Constructor
	 */
	public ConcentrationClientFrame() {
		setSize(WIDTH, HEIGHT);
		
		// Client
		this.clientScore = 0;
		this.gameOver = true;
		
		// Frame Methods
		cardInitializer();
		fieldCommunicationInitializer();
		labelScoreInitializer();
		buttonInitializer();
		panelInitializer();
		setLabelScore(this.clientScore);
		setLabelUser(MESSAGE_CONNECTING);
		setfieldCommunication(MESSAGE_WELCOME);
	}
	
	private void cardInitializer() {
		cardList = new ArrayList<Card>();
		
		// Generate Cards and add Coordinates
		for (int i = 0; i < TOTAL_CARD_NUMBER; i++) {
			cardList.add(new Card(this));
			cardList.get(i).setListPosition(i);
		}
	}
	
	private void fieldCommunicationInitializer() {
		final int FONT_SIZE = 22;
		Font font = new Font("SansSerif", Font.BOLD, FONT_SIZE);
		
		fieldCommunication = new JTextField();
		fieldCommunication.setPreferredSize(fieldDimension);
		fieldCommunication.setBorder(BorderFactory.createLineBorder(Color.black));
		fieldCommunication.setEditable(false);
		fieldCommunication.setFont(font);
		fieldCommunication.setHorizontalAlignment(JTextField.CENTER);
	}
	
	private void labelScoreInitializer() {
		final int FONT_SIZE = 20;
		Font font = new Font("SansSerif", Font.PLAIN, FONT_SIZE);
		
		// Score Label
		labelScore = new JLabel();
		labelScore.setPreferredSize(labelDimension);
		labelScore.setFont(font);
		labelScore.setHorizontalAlignment(JTextField.CENTER);
		/*
		 * from Tutorial on "How to use Borders"
		 * https://docs.oracle.com/javase/tutorial/uiswing/components/border.html
		 */
		labelScore.setBorder(BorderFactory.createLineBorder(Color.black));

		// Client Label
		labelUser = new JLabel();
		labelUser.setPreferredSize(labelDimension);
		labelUser.setBorder(BorderFactory.createLineBorder(Color.black));
		labelUser.setFont(font);
		labelUser.setHorizontalAlignment(JTextField.CENTER);
		
	}
	
	private void buttonInitializer() {
		buttonRestart = new JButton("Restart");
		// restartButton.addActionListener(e -> restart());
		buttonRestart.setPreferredSize(labelDimension);
		
		buttonQuit = new JButton("Quit");
		buttonQuit.setPreferredSize(labelDimension);
		buttonQuit.addActionListener(e -> clientQuit());
	}
	
	private void panelInitializer() {
		JPanel clientHUDButtons = new JPanel(new GridLayout(1, 2));
		clientHUDButtons.add(buttonRestart, BorderLayout.CENTER);
		clientHUDButtons.add(buttonQuit, BorderLayout.CENTER);
		
		JPanel clientHUDRight = new JPanel(new GridLayout(2, 1));
		JPanel clientHUDRightLabels = new JPanel(new GridLayout(1, 2));
		clientHUDRightLabels.add(labelScore);
		clientHUDRightLabels.add(labelUser);
		clientHUDRight.add(clientHUDRightLabels);
		clientHUDRight.add(clientHUDButtons);
		
		JPanel clientHUD = new JPanel(new GridLayout(1, 2));
		clientHUD.add(fieldCommunication);
		clientHUD.add(clientHUDRight);
		
		JPanel cardPanel = new JPanel(new GridLayout(2, 4));
		for (Card card: cardList) {
			cardPanel.add(card);
		}
		
		panelClient = new JPanel(new BorderLayout());
		panelClient.add(cardPanel, BorderLayout.CENTER);
		panelClient.add(clientHUD, BorderLayout.SOUTH);
		
		getContentPane().add(panelClient);
		
	}
	
	
	/**
	 * Client Control Commands
	 */
	
	/**
	 * This method "connects" the client to the fakeServer, sets client ID, changes the client Label and starts the game. 
	 * @param aClientId
	 */
	public void setClientId(String aClientId) {
		this.clientId = aClientId;
		this.setGameOver(false);
		this.setLabelUser(MESSAGE_CONNECTED);
		this.setfieldCommunication(MESSAGE_WAITING);
		this.isConnectedToServer = true;
	}
	
	/**
	 * Sets the boolean variable which determines whether it is the client's turn.
	 * @param myTurn
	 */
	public void setClientTurn(boolean myTurn) {
		if (!this.gameOver) {
			this.myTurn = myTurn;
			if (this.myTurn) {
				setfieldCommunication(MESSAGE_PLAY);
			} else {
				setfieldCommunication(MESSAGE_WAIT);
			}
		}
	}
	
	/**
	 * Returns the client Score
	 * @return
	 */
	public int getClientScore() {
		return this.clientScore;
	}
	
	/**
	 * Returns the client Id
	 * @return
	 */
	public String getClientId() {
		return this.clientId;
	}
	
	public void setfieldCommunication(String aMessage) {
		fieldCommunication.setText(aMessage);
	}
	
	/**
	 * Sets the communication Textfield with delay of 3000 to make it easier for the user to read
	 * @param aStr
	 */
	public void setfieldCommunicationWDelay(String aMessage) {
		fieldCommunication.setText(aMessage);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			System.out.println("Sleeping failed");
		}
	}
	
	/**
	 * Sets the score label
	 * @param aScore
	 */
	public void setLabelScore(int aScore) {
		this.clientScore = aScore;
		labelScore.setText("Score: " + this.clientScore);
	}
	
	/**
	 * Sets the user label
	 * @param aMessage
	 */
	public void setLabelUser(String aMessage) {
		labelUser.setText(aMessage);
	}
	
	/**
	 * Sets the Front card Label
	 * @param aCardNum
	 * @param aStr
	 */
	public void setFrontCardLabel(int aCardNum, String aMessage) {
		if (!this.gameOver) {
			this.cardList.get(aCardNum).setFrontCard(aMessage);
		}
	}
	
	/**
	 * Flips a card
	 * @param aCardNum
	 */
	public void flipCard(int aCardNum) {
		if (!this.gameOver) {
			this.cardList.get(aCardNum).serverFlip();
		}
	}
	
	/**
	 * Disable card from being clicked or fliped
	 * @param aCardNum
	 * @param aBool
	 */
	public void disableCard(int aCardNum, boolean aBool) {
		this.cardList.get(aCardNum).disable(aBool);
	}
	
	/**
	 * Tests whether two cards are equal
	 * @return
	 */
	public boolean areCardsTurnedEqual() {
		boolean output = (this.cardList.get(matchTestCardsPosition[0]).getFrontCard().equals(this.cardList.get(matchTestCardsPosition[1]).getFrontCard()));
		if (output) {
			setfieldCommunicationWDelay("SCORE!!!");
		} else {
			setfieldCommunicationWDelay("The Cards did not match");
		}
		return (this.cardList.get(matchTestCardsPosition[0]).getFrontCard().equals(this.cardList.get(matchTestCardsPosition[1]).getFrontCard()));
	}
	
	/**
	 * Defines the client as Winner or Looser
	 * @param aWin
	 */
	public void setWinner(boolean aWin) {
		if (!this.gameOver) {
			if (aWin) {
				setfieldCommunicationWDelay(MESSAGE_WINNER);
			} else {
				setfieldCommunicationWDelay(MESSAGE_LOSER);
			}
			this.winner = aWin;
		}
	}
	
	/**
	 * Returns a boolean about the user being connected
	 * @return
	 */
	public boolean isConnectedToServer() {
		return this.isConnectedToServer;
	}
	
	/**
	 * Returns a boolean if it is the client's turn
	 * @return
	 */
	public boolean isMyTurn() {
		return this.myTurn;
	}
	
	/**
	 * Sets Game Over
	 * @param aBool
	 */
	public void setGameOver(boolean aBool) {
		if (aBool) {
			setfieldCommunication(MESSAGE_GAMEOVER);
		}
		this.gameOver = aBool;
	}
	
	public void setFirstCardPosition (int cardNum) {
		this.matchTestCardsPosition[0] = cardNum;
	}

	public int getFirstCardPosition () {
		return this.matchTestCardsPosition[0];
	}
	
	public void setSecondCardPosition (int cardNum) {
		this.matchTestCardsPosition[1] = cardNum;
	}

	public int getSecondCardPosition () {
		return this.matchTestCardsPosition[1];
	}
	
	public void setFirstCardFlipped (boolean aBool) {
		this.cardsFlipped[0] = aBool;
	}
	
	public boolean isFirstCardFlipped () {
		return this.cardsFlipped[0];
	}
	
	public void setSecondCardFlipped (boolean aBool) {
		this.cardsFlipped[1] = aBool;
	}
	
	public boolean isSecondCardFlipped () {
		return this.cardsFlipped[1];
	}
	
	public void clientQuit() {
		if (isConnectedToServer && !this.gameOver) {
			if (this.myTurn) {
				setfieldCommunication("Quit requested to server");
				System.out.println("Client" + this.clientId + " -> Server: Quit requested to server");
			} else {
				setfieldCommunication("Cannot quit during opponent's turn");
			}
		} else {
			quit();
		}
	}
	
	public void quit() {
		System.exit(0);
	}
}
