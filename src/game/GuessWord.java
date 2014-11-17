/*

HANGMAN GAME

1) Sample Run

Here is your word: -------
Guesses so far: 
Make a guess: a
Good guess!  you have 6 body parts left

Here is your word: -----a-
Guesses so far: a
Make a guess: l
Bad guess - you have 5 body parts left

Here is your word: -----a-
Guesses so far: al
Make a guess: e
Bad guess - you have 4 body parts

Here is your word: -----a-
Guesses so far: ale
Make a guess: h
Bad guess - you have 3 body parts

Here is your word: -----a-
Guesses so far: aleh
Make a guess: p
Good guess!  you have 3 body parts

Here is your word: p----a-
Guesses so far: alehp
Make a guess: program
Yeah! you got it!

Do you want to play again (y/n)? n

2) Design - Pseudocode

Repeat until user is done
	Play a game
	Ask if done
	
Play a game:
	get a secret word
	make corresponding dashes
	set guesses so far to empty
	set done to false
	set bodyparts to 6
	while not done
		show dashes
		show guesses so far
		get a guess
		process guess
		
Make corresponding dashes:
	repeat secret length # of times:
		put - in dashes
		
Process guess:
	check if guess is word or single letter
	if guess is letter:
		add guess to guesses so far
		look for guess in secret
		if find guess in secret
			put it in dashes where it belongs
			output good guess
		otherwise (not there)
			subtract 1 from bodyparts
			output bad guess
		output how many bodyparts are left
		if bodyparts is 0
			output you lose
			done gets true
		if dashes same as secret
			output you win
			done gets true

	otherwise (guess is word):
		if guess equals secret
			output you win
		otherwise
			output you lose
		set done to true

Change dashes to matching letter
	for position goes from 0 to secret length
		if character at position in secret matches letter
			put letter in that position in dashes
		add 1 to position
 */
package game;

import java.util.Scanner;
import java.io.*;

public class GuessWord {
	public static void main(String[] args) throws IOException {
		Scanner kb = new Scanner(System.in);
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

		do {
			// play a game
			gamecounter++;
			secret = WordSelector.getWord();
			guesses = "";
			done = false;
			bodyparts = MAXPARTS;

			// make dashes
			dashes = makeDashes(secret);

			while (!done) {
				System.out.println("Here is your word: " + dashes);
				System.out.println("Guesses so far: " + guesses);
				System.out.print("enter a letter: ");
				guess = kb.next();

				// process the guess
				letter = guess.charAt(0);
				guesses += letter;
				if (secret.indexOf(letter) < 0) // not there
				{
					--bodyparts;
					System.out.print("bad guess - ");
				} else // letter is in the secret
				{
					// put it in dashes where it belongs
					matchLetter(secret, dashes, letter);
				}
				System.out.println(bodyparts + " bodyparts are left");
				if (bodyparts == 0) {
					System.out.println("you lose, the word is " + secret);
					done = true;
				}
				if (secret.equals(dashes.toString())) {
					System.out.println("you win!");
					gamewin++;
					done = true;
				}
			} // process single letter guess

			System.out.print("play again (y/n)?: ");
			again = kb.next().charAt(0);
			} while (again == 'Y' || again == 'y');
		
		System.out.println("thanks for playing (no more words)");
		System.out.println("You have won " + gamewin + " of " + gamecounter
				+ " games");
	} // end of main

	/*
	 * Change dashes to matching letter for position goes from 0 to secret
	 * length if character at position in secret matches letter put letter in
	 * that position in dashes add 1 to position
	 */
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

} // end of hangmanMethods