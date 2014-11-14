package clientserver;

import game.WordSelector;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GameHandler extends Thread {
	private Socket clientSocket;
	String targetWord;
	
	
	/**
	 * Creates a new instance.
	 *
	 * @param clientSocket
	 *            This socket should be connected to a hangman client.
	 */
	GameHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
		this.targetWord = WordSelector.getWord();
	}

	 
	
	/**
	 * Reads a char from the client, decide whether the char matches
	 */
	@Override
	public void run() {
		try (BufferedInputStream in = new BufferedInputStream(
				clientSocket.getInputStream());
				BufferedOutputStream out = new BufferedOutputStream(
						clientSocket.getOutputStream())) {
			
			
			
				
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
