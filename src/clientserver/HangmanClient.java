package clientserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class HangmanClient {

	public static void main(String[] args) throws IOException {
		Socket clientSocket = null;
		String guess, secret, dashes, guesses = "";
		boolean done = false;
		String bodyparts = "6";
		String again;
		try {
			clientSocket = new Socket("localhost", 4444); // Establish a
															// connection to the
															// server
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for " + "the connection.");
			System.exit(1);
		}

		try {
			// in = new BufferedInputStream(clientSocket.getInputStream());
			// //Set up input and output streams
			Scanner in = new Scanner(clientSocket.getInputStream());
			// out = new BufferedOutputStream(clientSocket.getOutputStream());
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
					true);

			do {
				System.out.println("Game starts.");
				done = false;
				bodyparts = "6";
				Scanner userEntry = new Scanner(System.in); // Set up stream for
															// the
															// keyboard

				secret = in.nextLine();
				System.out.println(secret);
				dashes = in.nextLine();

				while (!done) {
					System.out.println("\nHere is your word: " + dashes);
					System.out.println(bodyparts + " bodyparts left.");
					System.out.println("Guesses so far: " + in.nextLine());
					System.out.print("Enter a letter or a word: ");

					guess = userEntry.nextLine();
					if (guesses.length() == 0) {
						guesses = guess;

					} else {
						guesses = guesses + ", " + guess;
					}

					out.println(guess);
					out.flush();

					bodyparts = in.nextLine();
					dashes = in.nextLine();

					if (bodyparts.equals("0")) {
						System.out.println("\nYou lose, the word is " + secret);
						done = true;
					}

					if (secret.equals(dashes.toString())) {
						System.out.println("\nYou win!");
						done = true;
					}
				}

				System.out.println("\nPlay again (y/n)?: ");
				again = userEntry.nextLine();
				out.println(again);
				out.flush();
			} while (again.equals("Y") || again.equals("y"));

			System.out.println("\nYou have won " + in.nextLine() + "/"
					+ in.nextLine() + " parties.");

		} catch (IOException e) {
			System.out.println(e.toString());
			System.exit(1);
		}

	}
}
