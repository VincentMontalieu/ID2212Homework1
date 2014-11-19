package clientserver;

import game.WordSelector;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class GameHandler extends Thread {
	private Socket clientSocket;
	// String targetWord;
	// Scanner kb = new Scanner(System.in);
	String again;
	String secret;
	StringBuffer dashes;
	final int MAXPARTS = 6;
	int bodyparts;
	int gamecounter = 0, gamewin = 0;
	boolean done;
	String guess;
	private ArrayList<String> guesses;
	char letter;
	boolean incorrect;

	/**
	 * Creates a new instance.
	 * 
	 * @param clientSocket
	 *            This socket should be connected to a hangman client.
	 */
	GameHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
		guesses = new ArrayList<String>();
	}

	public boolean findGuess(String a) {
		return guesses.contains(a);
	}

	/**
	 * Reads a char from the client, decide whether the char matches
	 */
	@Override
	public void run() {
		Scanner in; // input from client
		PrintWriter out; // output to client
		try {
			in = new Scanner(clientSocket.getInputStream());
			out = new PrintWriter(clientSocket.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println(e.toString());
			return;
		}

		do {
			// play a game
			gamecounter++;
			secret = WordSelector.getWord();
			out.println(secret);
			out.flush();
			done = false;
			bodyparts = MAXPARTS;
			// make dashes
			dashes = makeDashes(secret);
			out.println(dashes);
			out.flush();

			while (!done) {
				out.println(guesses.toString());
				out.flush();
				guess = in.nextLine(); // receive input from client
				// process the guess
				if (guess.length() == 1) {
					letter = guess.charAt(0);
					matchLetter(secret, dashes, letter);
				} else if (guess.equals(secret)) {
					incorrect = false;
					dashes = new StringBuffer(guess);
				} else if (findGuess(guess)) {
					incorrect = false;
				} else {
					incorrect = true;
					guesses.add(guess);
				}

				if (incorrect) {
					bodyparts--;
				}
				// System.out.println(bodyparts + " bodyparts are left");
				out.println(bodyparts);
				out.flush();
				out.println(dashes);
				out.flush();

				if (bodyparts == 0) {
					// System.out.println("you lose, the word is " + secret);
					done = true;
				}
				if (secret.equals(dashes.toString())) {
					// System.out.println("you win!");
					gamewin++;
					done = true;
				}
			} // process single letter guess

			// System.out.print("play again (y/n)?: ");
			// again = in.toString().charAt(0);
			again = in.nextLine();
		} while (again.equals("Y") || again.equals("y"));

		out.println(gamewin);
		out.flush();
		out.println(gamecounter);
		out.flush();

		try {
			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void matchLetter(String secret, StringBuffer dashes, char letter) {
		if (!findGuess(Character.toString(letter))) {
			guesses.add(Character.toString(letter));
			incorrect = true;
			for (int index = 0; index < secret.length(); index++) {
				if (secret.charAt(index) == letter) {
					dashes.setCharAt(index, letter);
					incorrect = false;
				}
			}
		} else {
			incorrect = false;
		}
	}

	public static StringBuffer makeDashes(String s) {
		StringBuffer dashes = new StringBuffer(s.length());
		for (int count = 0; count < s.length(); count++)
			dashes.append('-');
		return dashes;
	}
}
