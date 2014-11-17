package clientserver;

import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class HangmanClient{
	public static void main(String[] args) throws IOException{
		Socket clientSocket = null;
		String guess, secret, dashes = null;
		boolean incorrect;	
		int bodyparts;
		char again = 'n';
		try {
			clientSocket = new Socket("localhost", 4444);	//Establish a connection to the server
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for " + "the connection.");
			System.exit(1);
		}
		
		try {
			//in = new BufferedInputStream(clientSocket.getInputStream());	//Set up input and output streams
			Scanner in = new Scanner(clientSocket.getInputStream());
			//out = new BufferedOutputStream(clientSocket.getOutputStream());
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			
			System.out.println("Game starts.");
			Scanner userEntry = new Scanner(System.in); 	// Set up stream for the keyboard
			
			secret = in.nextLine();
			dashes = in.nextLine();
			System.out.println("Here is your word " + dashes);
			System.out.println("Guess so far: ");
			System.out.println("Enter a letter:");
			
			guess = userEntry.nextLine();
			//System.out.println(guess);
			out.println(guess);
			
			incorrect = in.nextBoolean();
			if(incorrect = true){
				System.out.println("bad guess - ");
			}
			
			bodyparts = in.nextInt();
			System.out.println(bodyparts + " bodyparts left.");
			
			if (bodyparts == 0) {
				System.out.println("you lose, the word is " + secret);	
			}
			
			if (secret.equals(dashes.toString())) {
				System.out.println("you win!");
				//gamewin++;
				//done = true;
			}
		
			System.out.println("play again (y/n)?: ");
			again = userEntry.next().charAt(0);
			out.print(again);
		} catch (IOException e) {
			System.out.println(e.toString());
			System.exit(1);
		}
		
		
		
		
	}
}
