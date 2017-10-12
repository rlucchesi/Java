import java.awt.BorderLayout;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * GameServer Class used to establish connection to clients and start threads with runnable services.
 * @author Rafael Lucchesi
 * @version April 05th 2017
 */
public class GameServer extends JFrame implements GameConstants {
	// Frame Variables
	private final int WIDTH = 700;
	private final int HEIGHT = 300;
	private final String FRAME_TITLE = "Server";
	
	// Layout Variables
	private JTextArea textAreaLog;
	private JScrollPane scrollPaneLog;
	private JPanel panelServer;
	
	// Network Variables
	private ServerSocket serverSocket;
	private Socket[] clientList;
	private Runnable gameService;
	
	// Server Properties
	private static int MAX_CLIENT_NUMBER;
	
	/**
	 * GameServer constructor used to establish the minimum Layout and to listen to client's connections
	 */
	public GameServer() {
		// Frame Attributes
		setSize(WIDTH, HEIGHT);
		setTitle(FRAME_TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		this.textAreaLog = new JTextArea();
		this.scrollPaneLog = new JScrollPane(this.textAreaLog);
		frameInitializer();
		setVisible(true);
		openConnection();
	}
	
	private void frameInitializer() {
		this.panelServer = new JPanel(new BorderLayout());
		this.panelServer.add(this.scrollPaneLog, BorderLayout.CENTER);
		getContentPane().add(this.panelServer);
	}
	
	/**
	 * This method creates a Array with the max number of clients (defined by the user in the line arguments or set to two as default).
	 * It starts accepting connections in the PORT established in the GameConstants and when the service is full it puts it into a thread
	 * and goes back into listening for more clients.
	 */
	private void openConnection() {
		try {
			this.clientList = new Socket[MAX_CLIENT_NUMBER];
			this.serverSocket = new ServerSocket(PORT);
			reportStatsOnServer();
			
			while (true) {
				for (int clientNum = 0; clientNum < this.clientList.length; clientNum++) {
					this.clientList[clientNum] = this.serverSocket.accept();
					reportStatsOnClient(this.clientList[clientNum], clientNum);
				}
				
				this.gameService = new GameService(this.clientList, this.textAreaLog);
				new Thread(this.gameService).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Used to put messages in the server Log Area
	 * @param aMessage
	 */
	private void report(String aMessage) {
		this.textAreaLog.append(aMessage + "\r\n");
	}
	
	/**
	 * Used to give details when a client connects to the server 
	 * @param client
	 * @param clientNum
	 */
	private void reportStatsOnClient (Socket client, int clientNum) {
		report("The client " + clientNum + " connected using socket " + client.getLocalPort() + " on " + Calendar.getInstance().getTime());
	}
	
	/**
	 * Used to give details when the server goes online 
	 * @param client
	 * @param clientNum
	 */
	private void reportStatsOnServer() {
		report("The server port " + serverSocket.getLocalPort() + " started on " + Calendar.getInstance().getTime()+ ". Number of players/game: " + MAX_CLIENT_NUMBER);
	}

	/**
	 * Line Argument help method
	 */
	private static void help() {
		System.out.println("Usage: GameServer [-option] <argument>");
		System.out.println("Options include:");
		System.out.println();
		System.out.println("   -num <number of players>\tfor the number of players that will be needed for a single game");
		System.out.println("   -help\t\t\tprints this help message");
	}
	
	/**
	 * Main program deals with line arguments, if there are any, and starts the server.
	 * @param args
	 */
	public static void main (String[] args) {
		int numberOfPlayers = 2; 
		if (args.length > 0) {
			try {
				switch (args[0]) {
					case "-num":
						numberOfPlayers = Integer.parseInt(args[1]);
						break;
						
					default:
						help();
						return;
				}
			} catch (NullPointerException e) {
				help();
				return;
			}
		}
		MAX_CLIENT_NUMBER = numberOfPlayers;
		new GameServer();
	}
}