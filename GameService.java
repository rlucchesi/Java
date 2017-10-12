import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import javax.swing.JTextArea;

/**
 * GameService Class used to manage the game played by the clients, providing their turns and moderating the match.
 * @author Rafael Lucchesi
 * @version April 05th 2017
 */
public class GameService implements Runnable, GameConstants {
	// Network Variables
	private Socket[] clientSocketList;
	private DataOutputStream[] toAllClients;
	private DataInputStream fromClient;
	private DataOutputStream toClient;
	
	// Game Variables && Constants
	private char[] clientID;
	private int[] cardList;
	private int[] clientScore;
	private int[] currentGameGuesses = {-1, -1};
	private int indexBeingServed;
	private int winnerID;
	private int firstIndexToScore;
	private int numDisabledCards;
	private boolean[] clientTurn;
	private boolean[] cardDisabled;
	
	// Log Area
	private JTextArea textAreaLog;
	
	/**
	 * Game Service Constructor where the game variables are initialized.
	 * @param aClientList - Array containing sockets from all clients using the service.
	 * @param aTextArea - Log area.
	 */
	public GameService (Socket[] aClientList, JTextArea aTextArea) {
		this.textAreaLog = aTextArea;
		
		// Initialize Network Variables
		this.clientSocketList = aClientList;
		this.toAllClients = new DataOutputStream[this.clientSocketList.length];
		
		// Initialize client's variables
		this.clientID = new char[this.clientSocketList.length];
		this.clientScore = new int[this.clientSocketList.length];
		this.clientTurn = new boolean[this.clientSocketList.length];
		this.cardDisabled = new boolean[TOTAL_CARD_NUMBER];
		
		// Generate random Card order
		this.cardList = cardRandomizer(TOTAL_CARD_NUMBER / 2);
		
		// Set Winner to NoOne
		this.winnerID = -1;
		this.firstIndexToScore = -1;
		this.numDisabledCards = 0;
		
	}
	
	@Override
	public void run() {
		startService();
	}
	
	/**
	 * Method used to serve each client, alternating which one to communicate with.
	 */
	private void startService() {
		// @Server - Determine client ID for all players
		for (int i = 0; i < this.clientID.length; i++) {
			this.clientID[i] = (char) ('A' + i);
		}
		
		try {		
			// @Server - Generate Array of Client OutStream Sockets to talk to all clients
			for (int i = 0; i < this.clientSocketList.length; i++) {
				this.toAllClients[i] = new DataOutputStream(this.clientSocketList[i].getOutputStream());
				
				// @Client - Set clients ID
				this.toAllClients[i].writeInt(SETID);
				this.toAllClients[i].writeInt((int) this.clientID[i]);
			
				// @Client - Set all Clients Turn to false
				this.toAllClients[i].writeInt(SETTURN);
				this.toAllClients[i].writeInt(FALSE);
				
				// @Client - Set all Clients Score to 0
				this.toAllClients[i].writeInt(UPDATESCORELABEL);
				this.toAllClients[i].writeInt(0);
				

				// Update everycardLabel/imageIcon for each player ... ie define local card labels
				for (int j = 0; j < this.cardList.length; j++) {
					this.toAllClients[i].writeInt(UPDATECARDLABEL);
					this.toAllClients[i].writeInt(j);
					this.toAllClients[i].writeInt(this.cardList[j]);
					this.toAllClients[i].writeInt(UPDATECARDIMAGE);
					this.toAllClients[i].writeInt(j);
				}
				
				// make sure commands are sent to client
				this.toAllClients[i].flush();
			}
			
			/*
			 * The loop will only become false when the game is over (no more cards or one of the clients quits)
			 */
			while (this.winnerID < 0) {
				/*
				 * Loop responsible for alternating between clients, taking their inputs in turns.
				 */
				for (this.indexBeingServed = 0; this.indexBeingServed < this.clientSocketList.length && this.winnerID < 0; this.indexBeingServed++) {
					// Define Current Client DataStreams
					this.fromClient = new DataInputStream(clientSocketList[this.indexBeingServed].getInputStream());
					this.toClient = new DataOutputStream(clientSocketList[this.indexBeingServed].getOutputStream());
					report("Client " + this.indexBeingServed + " being served");
						
					// @Server - Set turn
					this.clientTurn[this.indexBeingServed] = true;
						
					// @Client - Set turn
					this.toClient.writeInt(SETTURN);
					this.toClient.writeInt(TRUE);
						// make sure commands are sent to client
					this.toClient.flush();
					
					serve();
				}
			}
			
			// Broadcast the Winner to all clients if there was no quitters
			if (this.numDisabledCards == TOTAL_CARD_NUMBER) {
				broadcast(SETWINNER, this.winnerID);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Disconnect clients and end the thread
			closeAllConnections();
		}
	}
	
	/**
	 * Method used to listen and interpret clients' commands.
	 * @throws IOException
	 */
	private void serve() throws IOException {
		int clientCommand, clientIntArg;
		boolean waitingForInput = false;
		
		if (this.winnerID < 0) {
			waitingForInput = true;
		}
		
		while (waitingForInput) {
			clientCommand = fromClient.readInt();
			System.out.println("Service Incoming: " + cmdToString(clientCommand));

			switch (clientCommand) {
				case REQUESTCARD:
					clientIntArg = fromClient.readInt();

					// @Server - Keep track of cards being played 
					if (currentGameGuesses[0] == -1) {
						this.currentGameGuesses[0] = clientIntArg;
					} else if (currentGameGuesses[1] == -1) {
						this.currentGameGuesses[1] = clientIntArg;
					}
							
					//@Client - Flip cards on both clients
					broadcast(FLIPCARD, clientIntArg);
					break;
							
				case CONCLUDETURN:
					// @Client -- Give clients some time to see their cards
					broadcast(WAIT, null);
													
					// @Client - Set turn to false
					this.toClient.writeInt(SETTURN);
					this.toClient.writeInt(FALSE);

					if (isValidGuess()) {
						// To be used to untie a match
						if (this.firstIndexToScore < 0) {
							this.firstIndexToScore = this.indexBeingServed;
						}
							
						this.toClient.writeInt(UPDATESCORELABEL);
						// @Client && @Server -- Update Client's Score
						this.toClient.writeInt(++this.clientScore[this.indexBeingServed]);		
						
						// @Client - Disable guessed cards
						this.numDisabledCards += 2;
						broadcast(DISABLECARD, this.currentGameGuesses[0]);
						broadcast(DISABLECARD, this.currentGameGuesses[1]);
							
						// @Server - Disable guessed cards
						this.cardDisabled[this.currentGameGuesses[0]] = true;
						this.cardDisabled[this.currentGameGuesses[1]] = true;

					} else {
						// Flip cards from unsuccessful guess
						broadcast(FLIPCARD, this.currentGameGuesses[0]);
						broadcast(FLIPCARD, this.currentGameGuesses[1]);
					}

					// @Server - Clean guess Array
					for (int i = 0; i < this.currentGameGuesses.length; i++) {
						this.currentGameGuesses[i] = -1;
					}
					// @Server - End of turn
					waitingForInput = false;
					break;
							
				case REQUESTQUIT:
					report("User " + this.indexBeingServed + " requested quit");
					// @Server - set current player score to -1
					this.clientScore[this.indexBeingServed] = -1;
					// @Server - Determine winner and broadcast it to all users
					this.winnerID = clientID[determineWinnerIndex()];
					broadcast(SETWINNER, this.winnerID);
					broadcast(UPDATECOMMFIELD, 11);
					this.toClient.writeInt(UPDATECOMMFIELD);
					this.toClient.writeInt(8); // String: "Game Over. Thanks for playing!"
					this.toClient.flush();
					// @Server - Send WAIT to all clients
					broadcast(WAIT, null);
					// @Client - Reply ACK;
					this.toClient.writeInt(ACKQUIT);
					this.toClient.flush();
					waitingForInput = false;
					break;
			}
		}

		// Check if Game is over (but there were no quitters) and determine the winner
		if (this.clientScore[this.indexBeingServed] > -1) {
			if (this.numDisabledCards == TOTAL_CARD_NUMBER && this.winnerID < 0) {
				this.winnerID = clientID[determineWinnerIndex()];
			}				
		}
	}
	
	/**
	 * Determines the winner. In case of a draw, the first client to make a correct guess wins.
	 * @return The client's index in the clientID Array
	 */
	private int determineWinnerIndex() {
		int output;
		
		if (this.firstIndexToScore != -1) {
			output = this.firstIndexToScore;
		} else {
			output = 0;
		}
		for (int i = 0; i < this.clientScore.length; i++) {
			if (this.clientScore[output] < this.clientScore[i]) {
				output = i;
			}
		}
		return output;
	}
	
	/**
	 * The method generates the solicited amount of number pairs. The method generates a random position and 
 	 * inserts a number. If the position is in use, another will be generated.  
	 * @param numOfPairs to generate
	 * @return a array containing pairs of numbers
	 */
	private int[] cardRandomizer(int numOfPairs) {
		Random generator = new Random();
		int[] output;
		boolean done;
		int randomPosition, numIterations;

		// Avoid wrong input
		if (numOfPairs <= 0) {
			numIterations = 2;
		} else {
			numIterations = numOfPairs * 2;
		}
		
		output = new int[numIterations];
		
		for (int i = 1; i <= numIterations; i++) {
			done = false;
			do {
				randomPosition = generator.nextInt(numIterations);
				if (output[randomPosition] == 0) {
					done = true;
				}
			} while (!done);
			
			if (i <= numIterations / 2) {
				output[randomPosition] = i;
			} else {
				output[randomPosition] = i - (numIterations / 2);
			}
		}
		return output;
	}
	
	/**
	 * Tests the guess of the client to determine if there was a score
	 * @return
	 */
	private boolean isValidGuess() {
		return (this.cardList[this.currentGameGuesses[0]] == this.cardList[this.currentGameGuesses[1]]);
	}
	
	/**
	 * Used to put messages in the server Log Area
	 * @param aMessage
	 */
	private void report(String aMessage) {
		this.textAreaLog.append(aMessage + "\r\n");
	}
	
	/**
	 * Method use to broadcast a command to all clients.
	 * @param command - Command from the GameConstants Protocol
	 * @param arg - Argument to be broadcasted
	 * @throws IOException - The caller deals with the Exception
	 */
	private void broadcast (int command, Integer arg) throws IOException {
		for (int i = 0; i < this.toAllClients.length; i++) {
			this.toAllClients[i].writeInt(command);
			if (arg != null) {
				this.toAllClients[i].writeInt(arg);
			}
			this.toAllClients[i].flush();
		}
	}

	/**
	 * Method used to close connection with all users (after the game is done).
	 */
	private void closeAllConnections () {
		try {
			for (int i = 0; i < this.clientSocketList.length; i++) {
				if (this.clientSocketList[i] != null && !this.clientSocketList[i].isClosed()) {
					this.clientSocketList[i].close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			for (int i = 0; i < this.clientSocketList.length; i++) {
				this.clientSocketList[i] = null;
			}
		}
	}
}
