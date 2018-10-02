package com.codingchili.halloween.controller;

/** 
 * @author Robin Duda
 * @author Gustaf Nilstadius
 * @version 1.0
 * @date 2014-10-26
 * 
 * Controller: input events, View Updater.
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.codingchili.halloween.view.View;
import com.codingchili.halloween.model.Card;
import com.codingchili.halloween.model.Memory;
import com.codingchili.halloween.model.Memory.PickResult;

public class Controller implements ActionListener, MouseMotionListener,
		MouseListener, KeyListener {
	private View view;
	private Memory memory;
	private int x = 100, y = 100;
	private BotController botController;

	public Controller(View v, Memory m) {
		this.view = v;
		this.memory = m;
	}

	// set new memory, required for resuming where a new memory object will be
	// loaded.
	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	// the timerevent from the timer started in View.java.
	public void actionPerformed(ActionEvent e) {
		view.update(x, y);
		view.gameFinished();

		if (null != memory)
			memory.getTimer().Process();
	}

	// previously redrawing when data changed.
	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		// view.update(x, y);
	}

	// previously redrawing when data changed.
	@Override
	public void mouseDragged(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		// view.update(x, y);
	}

	// bot controller sends a message to update the view.
	public void botMessage() {
		view.pairPicked();
		view.update();
	}

	// huge method for handling mouse input.
	@Override
	public void mousePressed(MouseEvent e) {

		// ------------ cardview input & selecting cards. ----------------
		if (e.getButton() == MouseEvent.BUTTON1) {
			String button = view.getButton(e.getX(), e.getY());
			if (button == null) {
				if (memory.getPlayerTurn()) {
					Card card = view.mouseDown(e.getX(), e.getY());

					if (null != card) {
						PickResult pickaction = memory.pickCard(card);

						if (pickaction == PickResult.pair
								|| pickaction == PickResult.finished) {
							view.pairPicked();
						}
					}
				}

				// ------------------ Menu Input / Buttons -------------------
				// TODO button events in own view.
			} else if (button.equals("How To Play")) {
				view.showHowToPlay();
			} else if (button.equals("New Player vs Player")) {
				view.closeMainMenu();
				memory.newGame(Memory.GameMode.PlayerVsPlayer);
				view.openGame();
			} else if (button.equals("New Player vs Bot")) {
				view.closeMainMenu();
				memory.newGame(Memory.GameMode.PlayerVsAI);
				view.openGame();
				botController = new BotController(memory, memory.getBot(), this);
				botController.start();
			} else if (button.equals("Pause")) {
				view.togglePause();
				if (memory != null)
					memory.getTimer().toggleEnabled();
			} else if (button.equals("Keep Play")) {
				view.togglePause();
				if (memory != null)
					memory.getTimer().toggleEnabled();
			} else if (button.equals("Main Menu")) {
				view.storeGameState();
				view.hidePauseMenu();
				view.showMainMenu();
				view.closeGame();
			} else if (button.equals("Quit")) {
				view.storeGameState();
				System.exit(0);
			} else if (button.equals("Resume Game")) {
				if (view.loadGameState()) {
					memory.getTimer().start();
					memory.getTimer().reset();
					if (memory.getBot() != null) {
						botController = new BotController(memory,
								memory.getBot(), this);
						botController.start();
					}
					view.closeMainMenu();
					view.openGame();
				}
			} else if (button.equals("Close")) {
				view.hideHowToPlay();
			} else if (button.equals("CloseHighScore")) {
				view.closeHighScore();
				view.showMainMenu();
			} else if (button.equals("High Score")) {
				view.closeMainMenu();
				view.showHighScore();
			} else {
				System.out.println("Button: " + button);
			}
		}

		view.update();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	// show/hide the menu.
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			view.togglePause();
			if (memory != null)
				memory.getTimer().toggleEnabled();
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
