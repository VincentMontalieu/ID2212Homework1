package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * Hangman.java
 * @version 1.0, May 16, 2001
 */

/**
 * The Hangman class implements a game of Hangman.
 * 
 * @author Carl Reynolds
 */
public class Hangman {
    GallowsArea gallowsArea = null;

    JButton exitButton = null;
    JButton newGameButton = null;

    JLabel wordArea = null;
    JLabel messageArea = null;
    java.util.List alphaButtonList = new ArrayList();
    Iterator alphaIterator = null;

    boolean reset = true;
    boolean disable = false;
    boolean dontWrap = false;
    boolean wrap = true;
    boolean headDrawn = false;
    boolean bodyDrawn = false;
    boolean leftArmDrawn = false;
    boolean rightArmDrawn = false;
    boolean leftLegDrawn = false;
    boolean rightLegDrawn = false;

    // Target words
    String[] targetWords = { "native", "country", "color", "example", "helper",
	    "favorite", "charcoal", "smoke", "interest", "video", "language",
	    "drink", "homework", "shell", "sympathy", "define", "specify",
	    "drawing", "picture", "frame", "nutshell", "polygon", "circle",
	    "rectangle", "sphere", "sherry", "lotion", "shoes", "trowsers",
	    "belt", "blouse", "nightgown", "cowboy", "engineer", "waiter",
	    "wheel", "engine", "pedal", "street", "navigate", "sailing",
	    "skiing", "outboard", "runner", "dancer", "hero", "helpless",
	    "pseudonym", "lioness", "integrity" };
    String winnerMessage = "Congratulations!  You are a saved!";
    String losingPrefix = "Wrong!  You die!  The answer was ";
    String currentGuess;
    String targetWord;

    int numberWrong = 0;
    int numberOfBodyParts = 6;
    int next = 0;

    /**
     * Set up a new game of "Hangman".
     */
    public void setUpNewGame() {
	numberWrong = 0;
	messageArea.setText("Win or Die!");

	// Enable alphabet buttons
	Iterator alphaIterator = alphaButtonList.iterator();
	while (alphaIterator.hasNext()) {
	    ((JButton) alphaIterator.next()).setEnabled(reset);
	}

	// Disable new game button
	newGameButton.setEnabled(disable);

	// Color the word area
	wordArea.setBackground(Color.lightGray);

	// Present the new word
	double numb = Math.random();
	next = (int) (numb * targetWords.length);
	targetWord = targetWords[next];

	// Fill the word-to-guess with ???
	currentGuess = "?";
	for (int i = 0; i < targetWord.length() - 1; i++) {
	    currentGuess = currentGuess.concat("?");
	}
	wordArea.setText(currentGuess);

	// Nothing is drawn yet
	headDrawn = false;
	bodyDrawn = false;
	leftArmDrawn = false;
	rightArmDrawn = false;
	leftLegDrawn = false;
	rightLegDrawn = false;
	gallowsArea.repaint();

    }// setUpNewGame

    /**
     * Process a click on an alphabet button. Right or not?
     * 
     * @param answer
     *            Will be "a", "b", "c", etc.
     */
    public void processAnswer(String answer) { // Have Vanna turn the correct
					       // letters over
	char newCharacter = answer.charAt(0);

	// Look thru the target word.
	// If the character matches the target, concat the new character.
	// If the character doesn't match the target, concat the character
	// from the current guess.
	String nextGuess = "";
	boolean foundAMatch = false;
	for (int i = 0; i < targetWord.length(); i++) {
	    char characterToMatch = targetWord.charAt(i);
	    if (characterToMatch == newCharacter) {
		nextGuess = nextGuess.concat(String.valueOf(newCharacter));
		foundAMatch = true;
	    } else {
		nextGuess = nextGuess.concat(String.valueOf(currentGuess
			.charAt(i)));
	    }
	}// for each character
	currentGuess = nextGuess;
	wordArea.setText(currentGuess);

	// We have a winner?
	if (currentGuess.equals(targetWord)) {
	    // Disable the buttons
	    Iterator alphaIterator = alphaButtonList.iterator();
	    while (alphaIterator.hasNext()) {
		((JButton) alphaIterator.next()).setEnabled(disable);
	    }
	    messageArea.setText(winnerMessage);
	    newGameButton.setEnabled(reset);
	    exitButton.setEnabled(reset);
	}
	// Wrong Answer
	// Set out a new body part to be drawn by repaint()
	else {
	    if (!foundAMatch) {
		numberWrong++;
		switch (numberWrong) {
		case 1: {
		    headDrawn = true;
		    break;
		}
		case 2: {
		    bodyDrawn = true;
		    break;
		}
		case 3: {
		    leftArmDrawn = true;
		    break;
		}
		case 4: {
		    rightArmDrawn = true;
		    break;
		}
		case 5: {
		    leftLegDrawn = true;
		    break;
		}
		case 6: {
		    rightLegDrawn = true;
		    break;
		}
		default:
		    System.out.println("You should be dead!");
		}
		// Repaint the gallows area JPanel
		gallowsArea.repaint();
	    }
	    // Is the game over?
	    if (numberWrong >= numberOfBodyParts) {
		// Disable the buttons
		Iterator alphaIterator = alphaButtonList.iterator();
		while (alphaIterator.hasNext()) {
		    ((JButton) alphaIterator.next()).setEnabled(disable);
		}
		messageArea.setText(losingPrefix + targetWord);
		newGameButton.setEnabled(reset);
		exitButton.setEnabled(reset);
	    }
	}// if else
    }// processAnswer

    /**
     * Create the North pane of the BorderLayout used by the game. The returned
     * component is a JPanel with a single JLabel where the word prompts will be
     * displayed.
     * 
     * @return JPanel for use displaying game words.
     */
    public Component createNorthPane() {
	JPanel pane = new JPanel();
	pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
	pane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
	pane.add(Box.createHorizontalGlue());
	wordArea = new JLabel("Press New Game");
	wordArea.setFont(new Font("Helvetica", Font.PLAIN, 24));
	wordArea.setBackground(Color.lightGray);
	wordArea.setForeground(Color.black);
	pane.add(wordArea);
	pane.add(Box.createHorizontalGlue());
	return pane;
    }

    /**
     * Create the West pane of the BorderLayout used by the game. The returned
     * JPanel has 9 rows and 3 columns consisting of buttons for each letter in
     * the alphabet Also creates the ActionListener for the alphabet buttons.
     * 
     * @return JPanel with alphabet buttons in it.
     */
    public Component createWestPane() {
	ActionListener alphabetButtonAction = new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		JButton buttonPushed = (JButton) e.getSource();
		buttonPushed.setEnabled(disable);
		processAnswer(buttonPushed.getText());
	    }
	};

	JPanel pane = new JPanel();
	pane.setBorder(BorderFactory.createLoweredBevelBorder());
	GridBagLayout gridbag = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
	pane.setLayout(gridbag);
	c.fill = GridBagConstraints.BOTH;

	JButton button = new JButton("a");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 0;
	c.gridy = 0;
	c.gridheight = 1;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("b");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 1;
	c.gridy = 0;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("c");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 2;
	c.gridy = 0;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("d");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 0;
	c.gridy = 1;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("e");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 1;
	c.gridy = 1;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("f");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 2;
	c.gridy = 1;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("g");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 0;
	c.gridy = 2;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("h");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 1;
	c.gridy = 2;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("i");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 2;
	c.gridy = 2;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("j");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 0;
	c.gridy = 3;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("k");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 1;
	c.gridy = 3;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("l");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 2;
	c.gridy = 3;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("m");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 0;
	c.gridy = 4;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("n");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 1;
	c.gridy = 4;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("o");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 2;
	c.gridy = 4;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("p");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 0;
	c.gridy = 5;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("q");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 1;
	c.gridy = 5;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("r");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 2;
	c.gridy = 5;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("s");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 0;
	c.gridy = 6;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("t");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 1;
	c.gridy = 6;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("u");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 2;
	c.gridy = 6;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("v");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 0;
	c.gridy = 7;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("w");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 1;
	c.gridy = 7;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("x");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 2;
	c.gridy = 7;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("y");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 0;
	c.gridy = 8;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	button = new JButton("z");
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.gridx = 1;
	c.gridy = 8;
	gridbag.setConstraints(button, c);
	button.addActionListener(alphabetButtonAction);
	pane.add(button);
	alphaButtonList.add(button);

	alphaIterator = alphaButtonList.iterator();
	while (alphaIterator.hasNext()) {
	    ((JButton) alphaIterator.next()).setEnabled(disable);
	}
	return pane;
    }

    /**
     * Create the South pane of the BorderLayout used by the game. The returned
     * component is a JPanel with a single JLabel where the winning and losing
     * messages will be displayed.
     * 
     * @return JPanel for use displaying winning and losing messages.
     */
    public Component createSouthPane() {
	JPanel pane = new JPanel();
	pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
	pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	pane.add(Box.createHorizontalGlue());

	messageArea = new JLabel("Win or Die!");
	messageArea.setFont(new Font("Helvetica", Font.PLAIN, 28));
	messageArea.setBackground(Color.lightGray);
	messageArea.setForeground(Color.red);
	pane.add(messageArea);
	pane.add(Box.createHorizontalGlue());
	return pane;
    }

    /**
     * Create the Center pane of the BorderLayout used by the game. The returned
     * component is a JPanel where the victim will be hanged.
     * 
     * @return JPanel for use displaying gallows.
     */
    public Component createCenterPane() {
	// Pass the reference to this instance of the game so that
	// the repaint() method can find out what to draw
	gallowsArea = new GallowsArea(this);
	return gallowsArea;
    }

    /**
     * Create the East pane of the BorderLayout used by the game. The returned
     * JPanel contains New Game and Exit buttons arranged vertically in a
     * BoxLayout form.
     * 
     * @return JPanel with control buttons set in a BoxLayout.
     */
    public Component createEastPane() {
	ActionListener controlButtonListener = new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		JButton buttonPushed = (JButton) e.getSource();
		if (buttonPushed.getText().equals("New Game")) {
		    setUpNewGame();
		} else {
		    System.exit(0);
		}
	    }// actionPerformed
	};// controlButtonListener

	JPanel pane = new JPanel();
	pane.setBorder(BorderFactory.createLoweredBevelBorder());
	pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

	newGameButton = new JButton("New Game");
	newGameButton.setFont(new Font("Helvetica", Font.PLAIN, 18));
	newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	newGameButton.addActionListener(controlButtonListener);
	pane.add(newGameButton);

	pane.add(Box.createVerticalGlue());

	exitButton = new JButton("Exit");
	exitButton.setFont(new Font("Helvetica", Font.PLAIN, 18));
	exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	exitButton.addActionListener(controlButtonListener);
	pane.add(exitButton);

	return pane;
    }

    /**
     * Create the components for the game GUI. The returned JPanel will have all
     * the elements of the GUI arranged in a BorderLayout.
     * 
     * @return JPanel with GUI elements arranged in a BorderLayout.
     */
    public Component createComponents() {
	JPanel pane = new JPanel();
	pane.setBorder(BorderFactory.createLoweredBevelBorder());
	pane.setLayout(new BorderLayout());
	pane.add(createNorthPane(), BorderLayout.NORTH);
	pane.add(createWestPane(), BorderLayout.WEST);
	pane.add(createSouthPane(), BorderLayout.SOUTH);
	pane.add(createCenterPane(), BorderLayout.CENTER);
	pane.add(createEastPane(), BorderLayout.EAST);

	return pane;
    }

    /**
     * Usage:
     * 
     * >java Hangman lookAndFeel
     * 
     * Where lookAndFeel is optional, and if supplied can take the values:
     * 'motif', 'system', or 'metal' (default if none is specified).
     */
    public static void main(String[] args) {
	String lookAndFeel;

	lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
	if (args.length == 1) {
	    if (args[0].equals("motif"))
		lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
	    if (args[0].equals("metal"))
		lookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";
	    else if (args[0].equals("system"))
		lookAndFeel = UIManager.getSystemLookAndFeelClassName();
	}
	try {
	    UIManager.setLookAndFeel(lookAndFeel);
	} catch (Exception e) {
	}

	// Create the top-level container and add contents to it.
	JFrame frame = new JFrame("Hangman");
	frame.setSize(new Dimension(640, 480));
	Hangman app = new Hangman();
	Component contents = app.createComponents();

	frame.getContentPane().add(contents);

	// Finish setting up the frame, and show it.
	frame.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
		System.exit(0);
	    }
	});
	// Don't pack the frame -- it squashes things into one another
	// and causes size settings to be ignored. I don't know why...?
	// frame.pack();
	frame.setVisible(true);
    }
}

/**
 * Class GallowsArea
 */
class GallowsArea extends JPanel {
    Hangman controller;

    public GallowsArea(Hangman controller) {
	this.controller = controller;
	Border raisedBevel = BorderFactory.createRaisedBevelBorder();
	Border loweredBevel = BorderFactory.createLoweredBevelBorder();
	Border compound = BorderFactory.createCompoundBorder(raisedBevel,
		loweredBevel);
	setBorder(compound);
    }

    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	// Draw the gallows
	g.setColor(Color.black);
	g.drawRect(10, 10, 25, 350);
	g.drawRect(10, 10, 200, 25);
	g.setColor(Color.darkGray);
	g.fillRect(11, 11, 24, 349);
	g.fillRect(11, 11, 199, 24);

	// Draw the head and noose
	if (controller.headDrawn) {
	    g.setColor(Color.white);
	    g.fillRect(210, 35, 6, 15);
	    g.setColor(Color.pink);
	    g.fillOval(200, 50, 30, 30);
	    g.setColor(Color.white);
	    g.fillRect(207, 75, 15, 6);
	}

	// Draw the body
	if (controller.bodyDrawn) {
	    g.setColor(Color.black);
	    g.fillOval(195, 80, 40, 80);
	}

	// Draw the left arm
	if (controller.leftArmDrawn) {
	    g.setColor(Color.black);
	    // fillPolygon(xPoints, yPoints, numPoints)
	    int x6Points[] = { 220, 240, 270, 270, 240, 220 };
	    int y6Points[] = { 85, 90, 85, 95, 100, 95 };
	    g.fillPolygon(x6Points, y6Points, x6Points.length);
	}

	// Draw the right arm
	if (controller.rightArmDrawn) {
	    g.setColor(Color.black);
	    int x6Points[] = { 210, 190, 160, 160, 190, 210 };
	    int y6Points[] = { 85, 90, 85, 95, 100, 95 };
	    g.fillPolygon(x6Points, y6Points, x6Points.length);
	}

	// Draw the left leg
	if (controller.leftLegDrawn) {
	    g.setColor(Color.black);
	    int x9Points[] = { 220, 250, 270, 270, 280, 280, 270, 250, 220 };
	    int y9Points[] = { 130, 140, 160, 190, 190, 200, 200, 160, 160 };
	    g.fillPolygon(x9Points, y9Points, x9Points.length);
	}

	// Draw the right leg and finishing touches -- he's dead
	if (controller.rightLegDrawn) {
	    g.setColor(Color.black);
	    int x9Points[] = { 210, 180, 160, 160, 150, 150, 160, 180, 210 };
	    int y9Points[] = { 130, 140, 160, 190, 190, 200, 200, 160, 160 };
	    g.fillPolygon(x9Points, y9Points, x9Points.length);
	    g.drawLine(206, 65, 212, 65);
	    g.drawLine(209, 62, 209, 68);
	    g.drawLine(217, 65, 223, 65);
	    g.drawLine(220, 62, 220, 68);
	}
    }
}// GallowsArea

