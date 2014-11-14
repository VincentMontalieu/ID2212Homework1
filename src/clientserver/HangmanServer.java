package clientserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



/**
 * The main class of the reverse server. Opens a listening socket and waits for
 * client connections. Each connection is managed in s separate thread.
 */
public class HangmanServer {
	private static final int PORT = 4444;

	/**
	 * Starts the reverse server.
	 *
	 * @param args
	 *            No command line arguments are used.
	 */
	public static void main(String[] args) {
		boolean listening = true;
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(PORT);
			while (listening) {
				Socket clientSocket = serverSocket.accept();
				(new GameHandler(clientSocket)).start();      //	Thread starts
			}
			serverSocket.close();
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + PORT);
			System.exit(1);
		}
	}
}