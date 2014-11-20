package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The main class of the hangman server. Opens a listening socket and waits for
 * client connections. Each connection is managed in s separate thread.
 */
public class HangmanServer {
	private static final int PORT = 4444; // Define a port number

	/**
	 * Starts the hangman server.
	 * 
	 * @param args
	 *            No command line arguments are used.
	 */
	public static void main(String[] args) {
		boolean listening = true;
		ServerSocket serverSocket; // Create a ServerSocket object
		try {
			serverSocket = new ServerSocket(PORT);
			while (listening) {
				Socket clientSocket = serverSocket.accept(); // Put the server
																// into a
																// waiting state
				(new GameHandler(clientSocket)).start(); // Thread starts
			}
			serverSocket.close();
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + PORT);
			System.exit(1);
		}
	}
}