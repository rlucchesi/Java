import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The first JFrame will be the Graphical User Interface that the human player will use to play the Concentration Game (memory game).
 * @author Rafael Lucchesi
 * @version April 05th 2017
 */
public class Player extends JFrame implements Runnable, GameConstants {
	// Window (Layout) properties
	private final int WIDTH = 800;
	private final int HEIGHT = 600;
	private Dimension labelDimension = new Dimension(WIDTH / 4, 40);
	private Dimension fieldDimension = new Dimension(WIDTH / 2, 80);
	
	// Layout Variables & Constants
	private JTextField fieldCommunication;
	private JLabel labelScore, labelUser;
	private JButton buttonRestart, buttonQuit;
	private JPanel panelClient;
	private final String FRAME_TITLE = "Concentration Game - Client";
	
	// Saved communication messages
	private final String[] MESSAGE_LIST = {	"Welcome", // -------------------------------------------- 0
											"Connecting...", // -------------------------------------- 1
											"Connected", // ------------------------------------------ 2
											"Waiting for another player...", // ---------------------- 3
											"Your turn", // ------------------------------------------ 4
											"Opponent's turn", // ------------------------------------ 5
											"You Win!", // ------------------------------------------- 6
											"You lose...", // ---------------------------------------- 7
											"Game Over. Thanks for playing!", // --------------------- 8
											"Quit requested to server", // --------------------------- 9
											"Cannot quit during opponent's turn", // ----------------- 10
											"Opponent quitted. Thanks  for playing!" // -------------- 11
											};

	// Client Variables & Constants
	private ArrayList<Card> cardList;
	private boolean[] cardsFlipped = new boolean[2];
	private int[] matchTestCardsPosition = new int[2];
	private int myScore;
	private char myID;
	private char winnerID = (int) 32;
	private boolean amIConnected;	
	private boolean isMyTurn;
	
	// Network Variables
	private Socket socket;
	private DataInputStream fromServer;
	private DataOutputStream toServer;
	
	// Argument Variables
	static String argHost;
	static String argImgPath;
	static boolean argImg;
	
	// Audio Variables
	File audioFile;
	AudioInputStream audioStream = null;
	AudioFormat format;
	DataLine.Info info;
	Clip audioClip;
	
	/**
	 * Client Frame Constructor
	 */
	public Player() {
		// Frame Attributes
		setSize(WIDTH, HEIGHT);
		setTitle(FRAME_TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		
		//Network
		networkInitializer();
		
		// Frame Methods
		cardInitializer();
		fieldCommunicationInitializer();
		labelScoreInitializer();
		buttonInitializer();
		panelInitializer();
		setLabelScore(this.myScore);
		setLabelUser(MESSAGE_LIST[1]);
		setfieldCommunication(MESSAGE_LIST[3]);
		
		startThread();
		
		setVisible(true);
	}
	
	private void networkInitializer() {
		if (argHost != null) {
			openConnection(argHost);
		} else {
			openConnection(null);
		}
	}
	
	private void cardInitializer() {
		cardList = new ArrayList<Card>();
		
		// Generate Cards and pass their positions in the array
		for (int i = 0; i < TOTAL_CARD_NUMBER; i++) {
			cardList.add(new Card(this, argImgPath, argImg));
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
		buttonRestart.setPreferredSize(labelDimension);
		buttonRestart.addActionListener(e -> restart());
		buttonRestart.addActionListener(e -> soundEffect("click"));
		
		buttonQuit = new JButton("Quit");
		buttonQuit.setPreferredSize(labelDimension);
		buttonQuit.addActionListener(e -> clientQuit());
		buttonQuit.addActionListener(e -> soundEffect("click"));
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
	/*
	 * Code based on the tutorial: "How to play back audio in Java with examples". Some changes were made (switch statement).
	 * http://www.codejava.net/coding/how-to-play-back-audio-in-java-with-examples
	 */ 
	protected void soundEffect(String when) {
		try {
			switch (when) {
			case "beginOfTurn":
				audioFile = new File("src/sounds/unlock.wav");
				break;
			case "click":
				audioFile = new File("src/sounds/9mm.wav");
				break;
			case "endOfGame":
				audioFile = new File("src/sounds/pacmanIntroMusic.wav");
				break;
			}
			
			// Create an AudioInputStream from a given sound file: 
			audioStream = AudioSystem.getAudioInputStream(audioFile);
			
			// Acquire audio format and create a DataLine.Info object: 
			format = audioStream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			
			//Obtain the Clip: 
			audioClip = (Clip) AudioSystem.getLine(info);
			
			// Open the AudioInputStream and start playing: 
			audioClip.open(audioStream);
			audioClip.start();
			
			//audioClip.close();
			//audioStream.close();
			
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method used to Restart the game by reconnecting the user to a new service.
	 */
	private void restart() {
		if (this.socket == null) {
			for (int i = 0; i < this.cardList.size(); i++) {
				this.cardList.get(i).resetCard();
			}
			
			setLabelUser(MESSAGE_LIST[1]);
			setfieldCommunication(MESSAGE_LIST[3]);
			//this.cardList = null;
			this.amIConnected = false;	
			this.isMyTurn = false;
			cardsFlipped = new boolean[2];
			matchTestCardsPosition = new int[2];
			
			winnerID = (int) 32;
			
			//cardInitializer();
			networkInitializer();
			startThread();
		}
		
	}
	
	/**
	 * Client Control Commands
	 */
	
	/**
	 * This method "connects" the client to the fakeServer, sets client ID, changes the client Label and starts the game. 
	 * @param aClientId
	 */
	private void setClientId(char aClientId) {
		this.myID = aClientId;
		this.setLabelUser(MESSAGE_LIST[2]);
		this.setfieldCommunication(MESSAGE_LIST[3]);
		this.amIConnected = true;
	}
	
	/**
	 * Sets the boolean variable which determines whether it is the client's turn.
	 * @param myTurn
	 */
	private void setClientTurn(boolean myTurn) {
		this.isMyTurn = myTurn;
		if (this.isMyTurn) {
			setfieldCommunication(MESSAGE_LIST[4]);
		} else {
			setfieldCommunication(MESSAGE_LIST[5]);
		}
	}
	
	/**
	 * Sets the communication Textfield with some argument text
	 * @param aMessage
	 */
	protected void setfieldCommunication(String aMessage) {
		fieldCommunication.setText(aMessage);
	}
	
	/**
	 * Sets the score label
	 * @param aScore
	 */
	private void setLabelScore(int aScore) {
		this.myScore = aScore;
		labelScore.setText("Score: " + this.myScore);
	}
	
	/**
	 * Sets the user label
	 * @param aMessage
	 */
	private void setLabelUser(String aMessage) {
		labelUser.setText(aMessage);
	}
	
	/**
	 * Sets the Front card Label
	 * @param aCardNum
	 * @param aStr
	 */
	private void setFrontCardLabel(int aCardNum, int aCardValue) {
		this.cardList.get(aCardNum).setFrontCard(aCardValue);
	}
	
	/**
	 * Sets the Front card Icon
	 * @param aCardNum
	 * @param aStr
	 */
	private void setFrontCardIcon(int aCardNum) {
		this.cardList.get(aCardNum).setFrontCardIcon();
	}
	
	/**
	 * Flips a card
	 * @param aCardNum
	 */
	private void flipCard(int aCardNum) {
		this.cardList.get(aCardNum).flipCard();
	}
	
	/**
	 * Disable card from being clicked or flipped
	 * @param aCardNum
	 * @param aBool
	 */
	private void disableCard(int aCardNum, boolean aBool) {
		this.cardList.get(aCardNum).disable(aBool);
	}
	
	/**
	 * Defines the client as Winner or Looser
	 * @param aWin
	 */
	private void setWinner(int clientID) {
		this.winnerID = (char) clientID;
		if (this.myID == this.winnerID) {
			setLabelUser(MESSAGE_LIST[6]);
		} else {
			setLabelUser(MESSAGE_LIST[7]);
		}
	}
	
	/**
	 * Returns a boolean about the user being connected
	 * @return
	 */
	protected boolean amIConnected() {
		return this.amIConnected;
	}
	
	/**
	 * Returns a boolean if it is the client's turn
	 * @return
	 */
	protected boolean isMyTurn() {
		return this.isMyTurn;
	}
	
	private void setFirstCardPosition (int cardNum) {
		this.matchTestCardsPosition[0] = cardNum;
	}

	private void setSecondCardPosition (int cardNum) {
		this.matchTestCardsPosition[1] = cardNum;
	}

	private void setFirstCardFlipped (boolean aBool) {
		this.cardsFlipped[0] = aBool;
	}
	
	private boolean isFirstCardFlipped () {
		return this.cardsFlipped[0];
	}
	
	private void setSecondCardFlipped (boolean aBool) {
		this.cardsFlipped[1] = aBool;
	}
	
	protected boolean isSecondCardFlipped () {
		return this.cardsFlipped[1];
	}
	
	/**
	 * This method is invoked when the client clicks a Card.
	 * @param aCardNum
	 */
	protected void requestFlip(int aCardNum) {
		try {
			this.toServer.writeInt(REQUESTCARD);
			this.toServer.writeInt(aCardNum);
			this.toServer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method invoked when the client requests the server to Quit the game. If the game is already done, then it just quits at once.
	 */
	private void clientQuit() {
		if (amIConnected && winnerID == 32) {
			if (this.isMyTurn) {
				try {
					this.toServer.writeInt(REQUESTQUIT);
					this.toServer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				setfieldCommunication("Quit requested to server");
			} else {
				setfieldCommunication("Cannot quit during opponent's turn");
			}
		} else {
			System.exit(1);
		}
	}
		
	/**
	 * Connects to the server using the GameConstants variables.
	 * @param argHost
	 */
	private void openConnection(String argHost) {
		try {
			if (argHost == null) {
				this.socket = new Socket(HOST, PORT);
			} else {
				this.socket = new Socket(argHost, PORT);
			}
			
			this.fromServer = new DataInputStream(this.socket.getInputStream());
			this.toServer = new DataOutputStream(this.socket.getOutputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts the thread to listen to Server commands.
	 */
	private void startThread() {
		new Thread(this).start();
	}
	
	/**
	 * Method used to close the connection when the game is done.
	 */
	private void closeConnection() {
		try {
			if (this.socket != null && !socket.isClosed()) {
				this.socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.socket = null;
		}
	}

	/**
	 * Method used to give enough time for clients to see the cards before they are turned.
	 */
	private void clientWait() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method used to listen and interpret server's commands.
	 * @throws IOException
	 */
	@Override
	public void run() {
		boolean game = true;
		int networkCommand, networkIntArg, networkIntArg2;
		
		try {
			while (game) {
				networkCommand = fromServer.readInt();
				System.out.println("Client Incoming: " + cmdToString(networkCommand));
				
				switch (networkCommand) {
					case SETID:
						networkIntArg = fromServer.readInt();
						// @Client - Sets client ID, status to connected and changes the client Label
						this.setClientId((char) networkIntArg);
						this.setTitle(FRAME_TITLE + " " + ((char) networkIntArg));
						break;
					
					case SETTURN:
						networkIntArg = fromServer.readInt();
						if (networkIntArg == FALSE) {
							this.setClientTurn(false);
						} else {
							this.setClientTurn(true);
							soundEffect("beginOfTurn");
						}
						break;
					
					case SETWINNER:
						networkIntArg = fromServer.readInt();
						this.setWinner((char) networkIntArg);
						soundEffect("endOfGame");
						clientWait();
						setfieldCommunication(MESSAGE_LIST[8]);
						game = false;
						break;
				
					case UPDATESCORELABEL:
						networkIntArg = fromServer.readInt();
						this.setLabelScore(networkIntArg);
						break;
						
					case UPDATECOMMFIELD:
						networkIntArg = fromServer.readInt();
						this.setfieldCommunication(MESSAGE_LIST[networkIntArg]);
						break;
						
					case DISABLECARD:
						networkIntArg = fromServer.readInt();
						this.disableCard(networkIntArg, true);
						break;
						
					case FLIPCARD:
						networkIntArg = fromServer.readInt();
						if (this.isMyTurn()) {
							if (!this.isFirstCardFlipped() && !this.isSecondCardFlipped()) {
								this.flipCard(networkIntArg);
								this.setFirstCardFlipped(true);
								this.setFirstCardPosition(networkIntArg);
							} else if (this.isFirstCardFlipped() && !this.isSecondCardFlipped()) {
								this.flipCard(networkIntArg);
								this.setSecondCardFlipped(true);
								this.setSecondCardPosition(networkIntArg);
							}
							// If it is the second Card being flipped 
							if (this.isSecondCardFlipped()) {
								this.setFirstCardFlipped(false);
								this.setSecondCardFlipped(false);
								// send server to test for match
								this.toServer.writeInt(CONCLUDETURN);
								this.toServer.flush();
							}
						} else {
							this.flipCard(networkIntArg);
						}
						break;
						
					case WAIT:
						clientWait();
						break;
						
					case UPDATECARDLABEL:
						networkIntArg = fromServer.readInt();
						networkIntArg2 = fromServer.readInt();
						setFrontCardLabel(networkIntArg, networkIntArg2);
						break;
						
					case UPDATECARDIMAGE:
						networkIntArg = fromServer.readInt();
						setFrontCardIcon(networkIntArg);
						break;
						
					case ACKQUIT:
						game = false;
						break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
			audioClip.close();
			try {
				audioStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Line Argument help method
	 */
	private static void help() {
		System.out.println("Usage: GameServer [-option] <argument>");
		System.out.println("You can concatenate more then one option with its respective argument. Options include:");
		System.out.println();
		System.out.println("   -img\t\t\t\tsets the use of text as default");
		System.out.println("   -imgPath <directory path>\tto give the directory relative to where the images are. Ex: images/cardClass");
		System.out.println("   -server <host address>\tto pass the address of the server");
		System.out.println("   -help\t\t\tprints this help message");
		System.out.println();
	}
	
	/**
	 * Main program deals with line arguments, if there are any.
	 * @param args
	 */
	public static void main (String[] args) {
		try {
			for (int i = 0; i < args.length; i += 2) {
				switch (args[i]) {
					case "-server":
						argHost = args[i + 1];
						break;
					case "-img":
						argImg = true;
						i--;
						break;
					case "-imgPath":
						argImgPath = args[i + 1];
						break;
					default:
						help();
						return;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			help();
			return;
		}
		System.out.println(argImg);
		new Player();
	}
}
