package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Handles all communication with the game server.
 */
public class ServerConnection implements Runnable {
	private final String host;
	private final int port;
	private final HangmanClient ui;
	private final LinkedBlockingQueue<String> strings = new LinkedBlockingQueue<>();
	private Scanner in;
	private PrintWriter out;

	/**
	 * Creates a new instance. Does not connect to the server.
	 * 
	 * @param ui
	 *            The client ui object.
	 * @param host
	 *            The reverse server host name.
	 * @param port
	 *            The reverse server port number.
	 */
	public ServerConnection(HangmanClient ui, String host, int port) {
		this.host = host;
		this.port = port;
		this.ui = ui;
	}

	@Override
	public void run() {
		connect();
		callServer();
	}

	/**
	 * Connects to the server using the host name and port number specified in
	 * the constructor.
	 */
	@SuppressWarnings("resource")
	void connect() {
		try {
			Socket clientSocket = new Socket(host, port);
			in = new Scanner(clientSocket.getInputStream());
			out = new PrintWriter(clientSocket.getOutputStream(), true);
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + host + ".");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: "
					+ host + ".");
			System.exit(1);
		}
	}

	/**
	 * Used to submit a string for reversal.
	 * 
	 * @param text
	 *            The string to reverse.
	 */
	void addEntry(String text) {
		strings.add(text);
	}

	/**
	 * Waits to receive a string from the ui and sends that to the reverse
	 * server.
	 */
	void callServer() {
		try {
			playGame();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void playGame() throws InterruptedException {
		String again;
		boolean done;
		String bodyparts = "6";
		String guess, secret, dashes;

		do {
			done = false;
			ui.printData("Game starts.");
			secret = in.nextLine();
			ui.printData(secret);
			dashes = in.nextLine();

			while (!done) {
				ui.printData("\nHere is your word: " + dashes);
				ui.printData(bodyparts + " bodyparts left.");
				ui.printData("Guesses so far: " + in.nextLine());
				ui.printData("Enter a letter or a word: ");
				ui.userInput();

				guess = strings.take();
				out.println(guess);
				out.flush();

				bodyparts = in.nextLine();
				dashes = in.nextLine();

				if (bodyparts.equals("0")) {
					ui.printData("\nYou lose, the word is " + secret);
					done = true;
				}

				if (secret.equals(dashes)) {
					ui.printData("\nYou win!");
					done = true;
				}
			}

			ui.printData("\nPlay again (y/n)?: ");
			ui.userInput();
			again = strings.take();
			out.println(again);
			out.flush();
		} while (again.equals("Y") || again.equals("y"));

		ui.printData("\nYou have won " + in.nextLine() + "/" + in.nextLine()
				+ " parties.");
	}
}
