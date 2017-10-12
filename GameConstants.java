import java.io.DataInputStream;
import java.io.IOException;

/**
 * Protocol for the server / client Concentration Game.
 * @author Rafael Lucchesi
 * @version April 05th 2017
 */
public interface GameConstants {
	/**
	 * Port for communication
	 */
	int PORT = 1181;
	
	/**
	 * Server's Address. A line argument in the Player window can specify a different one.
	 */
	String HOST = "localhost";
	
	/**
	 * Total number of Cards to be used in each game.
	 */
	int TOTAL_CARD_NUMBER = 8;
	
	
	/**
	 * Boolean used in the protocol
	 */
	int TRUE = 0001;
	int FALSE = 0000;
	
	
	/**
	 * Message sent by the server to the user.
	 * SETID takes one integer argument.
	 * Usually the ID is a char casted into a integer
	 * Ex:	SETID 64
	 */ 
	int SETID = 1000;
	
	/**
	 * Message sent by the server to the user.
	 * SETTURN takes one integer argument to signal TRUE/FALSE.
	 * Ex:	SETTURN 1/0
	 */
	int SETTURN = 1001;
	
	/**
	 * Message sent by the server to the user.
	 * SETWINNER takes one integer argument.
	 * Usually the ID is a char casted into a integer
	 * Ex:	SETWINNER 65 
	 */
	int SETWINNER = 1003;
	
	/**
	 * 
	 */
	int UPDATESCORELABEL = 1004;	// command, intArg (int score);
	int UPDATECOMMFIELD = 1005;		// command, intArg (MESSAGE_LIST[] index);
	int ACKQUIT = 1009;				// command
	
	// Cards specifics
	int DISABLECARD = 1010;			// command, intArg (int cardNum);
	int FLIPCARD = 1011;			// command, intArg (int cardNum);
	int UPDATECARDLABEL = 1012;		// command, intArg (int cardNum), intArg2 (int cardLabel);
	int UPDATECARDIMAGE = 1015;		// command, intArg (int cardNum), intArg2 (int cardLabel);
	int CONCLUDETURN = 1013;		// command.
	int WAIT = 1014;				// command.
	
	
	
	
	/**
	 * Client Commands
	 */
	int REQUESTCARD = 2010;	// command, intArg (int cardNum)
	//int MATCHEDCARD = 2011; // command, intArg (int cardNum)
	int REQUESTQUIT = 2009;
	
		
	/**
	 * Converts an integer command cmd to its string representation. 
	 * A command that is not suppored returns the string
	 * "UNRECOGNIZABLE COMMAND".
	 * @param cmd an integer corresponding to a command
	 * @return String the textual representation of the command cmd
	 */ 
	 default String cmdToString(int cmd) {
		 String cmdString;
	     switch (cmd) {
	     	case SETID:
	     		cmdString = "SETID";
	     		break;
	         case SETTURN: 
	        	cmdString = "SETTURN";
	        	break;
	         case SETWINNER:
	        	cmdString = "SETWINNER";
	        	break;
	         case UPDATESCORELABEL:
	        	cmdString = "UPDATESCORELABEL";
	        	break;
	         case UPDATECOMMFIELD:
	        	cmdString = "UPDATECOMMFIELD";
	        	break;
	         case DISABLECARD:
		        cmdString = "DISABLECARD";
		        break;
	         case UPDATECARDLABEL:
		        cmdString = "UPDATECARDLABEL";
		        break;
	         case UPDATECARDIMAGE:
			        cmdString = "UPDATECARDIMAGE";
			        break;
	         case CONCLUDETURN:
		        cmdString = "CONCLUDETURN";
		        break;
	         case REQUESTCARD:
		        cmdString = "REQUESTCARD";
		        break;
	         case FLIPCARD:
		        cmdString = "FLIPCARD";
		        break;
	         case REQUESTQUIT:
	        	cmdString = "REQUESTQUIT";
		        break;
	         case WAIT:
	        	cmdString = "WAIT";
			    break;
	         case ACKQUIT:
	            cmdString = "ACKQUIT";
	            break;
	         default:
	            cmdString = "UNRECOGNIZABLE COMMAND " + cmd;
	     }  // switch
	     return cmdString;
	   } // cmdToString
}
