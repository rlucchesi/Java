/**
 * Write and submit as part of your assignment a protocol which will be used between the 
 * server and the players (the clients).
 * @author Rafael Lucchesi
 * @version March 29th 2017
 */
public interface NetworkProtocol {
	int PORT = 1181;
	
	/**
	 * Server Commands
	 */
	// Control game and the Client's GUI
	int SETID = 1000;
	int SETTURN = 1001;
	int SETWINNER = 1003;
	int UPDATESCORELABEL = 1004;
	int UPDATECOMMFIELD = 1005;
	int SETGAMEOVER = 1008;
	int ACKQUIT = 1009;
	
	// Cards specifics
	int DISABLECARD = 1010;
	int FLIPCARD = 1011;
	int UPDATECARDLABEL = 1012;
	
	
	/**
	 * Client Commands
	 */
	int REQUESTID = 2000;	// not sure if necessary but for now...
	int REQUESTCARD = 2010;
	int REQUESTQUIT = 2009;
}
