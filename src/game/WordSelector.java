package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Random;

public class WordSelector {

    public static int countLines() {
	LineNumberReader lnr = null;
	try {
	    lnr = new LineNumberReader(new FileReader(new File("words.txt")));
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	try {
	    lnr.skip(Long.MAX_VALUE);
	} catch (IOException e1) {
	    e1.printStackTrace();
	}
	int nbLines = lnr.getLineNumber();
	try {
	    lnr.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return nbLines;
    }

    private static int random() {
	Random rand = new Random();
	return (int) (Math.random() * ((countLines() + 1)));
    }

    @SuppressWarnings("resource")
    public static String getWord() {
	String line = null;
	BufferedReader in = null;
	try {
	    in = new BufferedReader(new FileReader(new File("words.txt")));
	} catch (FileNotFoundException e) {
	    line = "random";
	    e.printStackTrace();
	}
	int max = random();
	for (int i = 0; i <= max; i++) {
	    try {
		line = in.readLine();
	    } catch (IOException e) {
		line = "random";
		e.printStackTrace();
	    }
	}
	return line;
    }
}
