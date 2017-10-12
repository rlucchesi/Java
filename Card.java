import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Card Class used in the Concentration Game.
 * @author Rafael Lucchesi
 * @version April 05th 2017
 */
public class Card extends JButton implements GameConstants {
	public static int WIDTH = 160;
	public static int HEIGHT = 217;
	
	final int FONT_SIZE = 24;
	final Font font = new Font("SansSerif", Font.BOLD, FONT_SIZE);
	public static String SUIT_MESSAGE = "CPSC 1181";
	
	// Client's window where the card is displayed
	private Player myFrame;
	
	// Cards properties
	private int listPosition;
	private int cardFront;
	private boolean isFlipped;
	private boolean disabled;
	private boolean isImagePrefered;
	private String userDefinedImgPath;
	private ImageIcon iconSuit;
	private ImageIcon iconFront;
	
	/**
	 * Card constructor used to define its properties, some of them according to the parameters received.
	 * @param aMyFrame - Window where the card is inserted
	 * @param argImg - If the user specified through line arguments where the images are to be found
	 * @param argText - If the user wants to display Images instead of Text
	 */
	public Card(Player aMyFrame, String argImgPath, boolean argDisplayImg) {
		this.myFrame = aMyFrame;
		this.isImagePrefered = argDisplayImg;
		this.userDefinedImgPath = argImgPath;
		
		if (this.isImagePrefered) {
			if (this.userDefinedImgPath == null) {
				this.iconSuit = new ImageIcon(getClass().getResource("images/cardClass/suit.png"));
			} else {
				this.iconSuit = new ImageIcon(getClass().getResource(this.userDefinedImgPath+"/suit.png"));
			}
			setIcon(this.iconSuit);
		} else {
			this.setText(SUIT_MESSAGE);
		}
		
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setFont(font);
		this.addActionListener(e -> requestFlip());
		this.addActionListener(e -> this.myFrame.soundEffect("click"));
	}

	/**
	 * This method only tests if: 1- the card is matched (disabled), 2- the client is connected,
	 * 3- it is the client's turn, 4- the client is not attempting to turn a third card.
	 * If all of them are satisfactory, then it displays a message "requesting" the card to the server.
	 * The actual flip method is the serverFlip()!
	 */
	private void requestFlip() {
		if (!this.disabled) {
			if (myFrame.amIConnected()) {
				if (myFrame.isMyTurn()) {
					if (!myFrame.isSecondCardFlipped()) {
						if (!this.isFlipped) {
							this.myFrame.requestFlip(this.listPosition);
						}
					} else {
						myFrame.setfieldCommunication("Illegal turn, can't flip a third card");
					}
				} else { // myOwnerTurn
					myFrame.setfieldCommunication("It is the opponent's turn");
				}
			} else { // isConnectedToServer
				myFrame.setfieldCommunication("The game has not started");
			}
		}
	}
	
	/**
	 * Actually flips the card in the Client's Frame
	 */
	protected void flipCard() {
		if (!this.disabled) {	
			if (isFlipped) {
				this.isFlipped = false;
				if (this.isImagePrefered) {
					setIcon(this.iconSuit);
				} else {
					this.setText(SUIT_MESSAGE);
				}
			} else {
				this.isFlipped = true;
				if (this.isImagePrefered) {
					setIcon(this.iconFront);
				} else {
					this.setText("" + this.cardFront);
				}
			}
		}
	}
	
	/**
	 * Resets the Card variables
	 */
	protected void resetCard() {
		isFlipped = false;
		disabled = false;
		
		if (this.isImagePrefered) {
			setIcon(this.iconSuit);
		} else {
			this.setText(SUIT_MESSAGE);
		}
	}
	
	/**
	 * Sets the Card position, so it knows where it is in the ArrayList
	 * @param aPosition
	 */
	protected void setListPosition(int aPosition) {
		this.listPosition = aPosition;
	}
	
	/**
	 * Disables/Enables the Card
	 * @param aBool
	 */
	protected void disable (boolean aBool){
		this.disabled = aBool;
	}
	
	/**
	 * Defines the face card value
	 * @param aCardValue
	 */
	protected void setFrontCard(int aCardValue) {
		this.cardFront = aCardValue;
	}
	
	/**
	 * Returns the face card value
	 * @return
	 */
	protected int getFrontCard() {
		return this.cardFront;
	}
	
	/**
	 * Method used to update each card image. To be used after the face card value is defined
	 */
	protected void setFrontCardIcon() {
		if (this.userDefinedImgPath == null) {
			this.iconFront = new ImageIcon(getClass().getResource("images/cardClass/0" + this.cardFront + ".png"));
		} else {
			this.iconFront = new ImageIcon(getClass().getResource(this.userDefinedImgPath+"/0" + this.cardFront + ".png"));
		}
	}
}