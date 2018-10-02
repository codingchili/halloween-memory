package com.codingchili.halloween.view;

/** 
 * @author Robin Duda
 * @author Gustaf Nilstadius
 * @version 1.0
 * @date 2014-10-26
 * 
 * View: Paints the view.
 */

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.codingchili.halloween.controller.Controller;
import com.codingchili.halloween.controller.MemorySerializer;
import com.codingchili.halloween.model.Card;
import com.codingchili.halloween.model.Memory;
 
public class View extends JPanel {
	private static final long serialVersionUID = 1L;
	private Parallax parallax;
	private MainMenu mainMenu;
	private CardView cardView;
	private HowToPlay howToPlay;
	private PauseMenu pauseMenu;
	private HighScore highScore;
	private Memory memory;
	private Timer timer;
	private ArrayList<Paintable> toPaint = new ArrayList<Paintable>();
	private int x = 0, y = 0;
	private Controller controller;

	public View(JFrame frame, Dimension screenSize) {
		super();
		// Create the model, memory the game.
		this.memory = new Memory();

		// set up panel options.
		this.setPreferredSize(screenSize);
		this.setSize(screenSize);
		this.setFocusable(true);

		// create some views.
		this.cardView = new CardView(screenSize.width, screenSize.height, memory);
		this.howToPlay = new HowToPlay(screenSize.width, screenSize.height);
		this.highScore = new HighScore(screenSize.width, screenSize.height);
		;

		// create and add the parallax background,
		parallax = new Parallax(screenSize.width, screenSize.height);
		toPaint.add(parallax);

		// Initiate MainMenu
		mainMenu = new MainMenu(screenSize.width, screenSize.height);
		toPaint.add(mainMenu);

		// create the pause menu in game view, from pause button or ESC key.
		pauseMenu = new PauseMenu(screenSize.width, screenSize.height);

		// create the controller, set up mouse events for the controller.
		controller = new Controller(this, memory);
		this.addMouseMotionListener(controller);
		this.addMouseListener(controller);
		this.addKeyListener(controller);

		// set new mouse cursor.
		InputStream is;
		is = this.getClass().getClassLoader()
				.getResourceAsStream("hwcursor.gif");
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = null;
		try {
			image = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Point pointer = new Point(0, 0);
		Cursor cursor = toolkit.createCustomCursor(image, pointer,
				"halloween-cursor");
		setCursor(cursor);

		// create a controller for updating the view every 1000/FPS ms.
		this.update();
		timer = new Timer(Math.round(1000 / FPS), controller);
		timer.start();

		// play ambient sound. //TODO add mute option.
		/*
		 * try { AmbientAudio.Play(this); //Getting errors on Linux systems }
		 * catch (Exception e) { e.printStackTrace(); }
		 */
	}

	// update the memory model, used for resuming game state.
	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	// hide the cardView.
	public void closeGame() {
		toPaint.remove(cardView);
	}

	// show the cardView
	public void openGame() {
		cardView.reset();
		if (!toPaint.contains(cardView))
			toPaint.add(cardView);
		update(this.x, this.y);
	}

	// display the how to play
	public void showHowToPlay() {
		if (!toPaint.contains(howToPlay))
			toPaint.add(howToPlay);
	}

	// hide how to play
	public void hideHowToPlay() {
		toPaint.remove(howToPlay);
	}

	// show the main menu.
	public void showMainMenu() {
		if (!toPaint.contains(mainMenu))
			toPaint.add(mainMenu);
		update(this.x, this.y);
	}

	// close the main menu.
	public void closeMainMenu() {
		toPaint.remove(mainMenu);
		update(this.x, this.y);
	}

	// show the high-score.
	public void showHighScore() {
		highScore.clearWinner();
		if (!toPaint.contains(highScore))
			toPaint.add(highScore);
	}

	// close the high-score.
	public void closeHighScore() {
		toPaint.remove(highScore);
	}

	public String getButton(int X, int Y) {
		// TODO, should check all layers, not only the uppermost.
		return toPaint.get((toPaint.size() - 1)).getButton(X, Y);
	}

	public void togglePause() {
		if (toPaint.get(toPaint.size() - 1) instanceof PauseMenu) {
			toPaint.remove(pauseMenu);
		} else if (toPaint.get(toPaint.size() - 1) instanceof MainMenu) {
			// Do nothing
		} else {
			toPaint.add(pauseMenu);
		}
	}

	// call the update method of views added in toPaint.
	public void update(int x, int y) {
		this.x = x;
		this.y = y;
		for (int i = 0; i < toPaint.size(); i++) {
			if (toPaint.get(i).update(x, y)) {
				toPaint.remove(i);
				i--;
			}
		}
		repaint();
	}

	// update this view.
	public void update() {
		update(this.x, this.y);
	}

	// a pair was picked, notify the view.
	public void pairPicked() {
		cardView.pairPicked();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < toPaint.size(); i++) {
			toPaint.get(i).paint(g);
		}
	}

	// controller asks if a card was hit by the mouse click event.
	public Card mouseDown(int x, int y) {
		Card card = null;
		if (toPaint.get(toPaint.size() - 1).equals(cardView))
			card = cardView.mouseDown(x, y);
		return card;
	}

	// hide the pause menu.
	public void hidePauseMenu() {
		toPaint.remove(pauseMenu);
	}

	// checks if the game is finished, called from control Timer.
	public void gameFinished() {
		if (toPaint.contains(cardView)) {
			if (cardView.finished()) {
				// if the game view reports finished, show the highscore.
				toPaint.remove(cardView);
				showHighScore();
				highScore.updateHighScore(memory.getPlayerNames()[0],
						memory.getScoring()[0]);
				highScore.updateHighScore(memory.getPlayerNames()[1],
						memory.getScoring()[1]);
				highScore.setWinner(memory.winnerName(), memory.winnerScore());
				System.out.println("Winner Is: " + memory.winnerName());
			}
		}
	}

	// store the game state to file for later resume.
	public void storeGameState() {
		MemorySerializer.storeGameState(memory);
	}

	// load the game state from file and update active memory.
	public boolean loadGameState() {
		if ((memory = MemorySerializer.loadGameState()) != null) {
			controller.setMemory(memory);
			cardView.setMemory(memory);
			return true;
		} else
			return false;
	}

	private static final int FPS = 60;
}
