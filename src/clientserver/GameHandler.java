package clientserver;

import game.WordSelector;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameHandler extends Thread {
	private Socket clientSocket;
	// String targetWord;
	// Scanner kb = new Scanner(System.in);
	char again = 'n';
	String secret;
	StringBuffer dashes;
	final int MAXPARTS = 6;
	int bodyparts;
	int gamecounter = 0, gamewin = 0;
	boolean done;
	String guess;
	String guesses;
	char letter;

	/**
	 * Creates a new instance.
	 *
	 * @param clientSocket
	 *            This socket should be connected to a hangman client.
	 */
	GameHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
		// this.secret = WordSelector.getWord();
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
			guesses = "";
			done = false;
			bodyparts = MAXPARTS;
			boolean incorrect = secret.indexOf(letter) < 0;

			// make dashes
			dashes = makeDashes(secret);
			out.println(dashes);

			while (!done) {
				// System.out.println("Here is your word: " + dashes);
				// System.out.println("Guesses so far: " + guesses);
				// System.out.print("enter a letter: ");
				
				guess = in.nextLine();	//receive input from client
				
				// process the guess
				letter = guess.charAt(0);
				guesses += letter;
				if (incorrect) // not there
				{
					--bodyparts;
					// System.out.print("bad guess - ");
					out.print(incorrect);
					
				} else // letter is in the secret
				{
					// put it in dashes where it belongs
					matchLetter(secret, dashes, letter);
				}
				//System.out.println(bodyparts + " bodyparts are left");
				out.println(bodyparts);
				
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
			again = in.next().charAt(0);
		} while (again == 'Y' || again == 'y');

		// System.out.println("thanks for playing (no more words)");
		// System.out.println("You have won " + gamewin + " of " + gamecounter
		// + " games");

		try {
			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void matchLetter(String secret, StringBuffer dashes,
			char letter) {
		for (int index = 0; index < secret.length(); index++)
			if (secret.charAt(index) == letter)
				dashes.setCharAt(index, letter);
		System.out.print("good guess - ");
	}

	public static StringBuffer makeDashes(String s) {
		StringBuffer dashes = new StringBuffer(s.length());
		for (int count = 0; count < s.length(); count++)
			dashes.append('-');
		return dashes;
	}
}
