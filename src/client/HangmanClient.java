package client;

import java.io.IOException;
import java.util.Scanner;

import javax.swing.SwingUtilities;

public class HangmanClient {
	private ServerConnection connection;
	Scanner userEntry;

	public HangmanClient() {
		userEntry = new Scanner(System.in);
		connection = new ServerConnection(HangmanClient.this, "localhost", 4444);
		new Thread(connection).start();
	}

	void userInput() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				connection.addEntry(userEntry.nextLine());
			}
		});
	}

	void printData(final String result) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				System.out.println(result);
			}
		});
	}

	public static void main(String[] args) throws IOException {
		new HangmanClient();
	}
}
