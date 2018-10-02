package com.codingchili.halloween.model;

import java.io.Serializable;

/**
 * @author Robin Duda
 * 
 * @version 1.0
 * @date 2014-10-26
 * 
 *       Model.TurnTimer: Keep track of turn lengths, awards bonus points for
 *       quick draws.
 */

public class TurnTimer implements Serializable {
	private int reset;
	private int left;
	private int bonus;
	private boolean enabled;

	/***
	 * @param reset
	 *            Time to set to when the timer is reset.
	 * @param bonusPerSecond
	 *            bonus score to add per second left.
	 */
	public TurnTimer(int reset, int bonusPerSecond) {
		this.bonus = bonusPerSecond;
		this.reset = reset;
		this.enabled = true;
	}

	/*** reset the timer value. */
	public void reset() {
		left = reset;
	}

	/***
	 * get time left
	 * 
	 * @return returns the time left in frames. (1s = 60 at 60fps)
	 */
	public int getLeft() {
		return left;
	}

	/*** stop the timer, called when the game is paused */
	public void stop() {
		enabled = false;
	}

	/*** start the timer, called when the game is unpaused */
	public void start() {
		enabled = true;
	}

	/*** timer.enabled != enabled. */
	public void toggleEnabled() {
		enabled = !enabled;
	}

	/*** @return the time which the timer is set to on reset. (in frames) */
	public int getReset() {
		return reset;
	}

	/*** this method will modify the time left of the timer. */
	public void Process() {
		if (enabled && left > 0) {
			left--;
		}
	}

	/***
	 * @return the amount of bonus points the player earned depending on
	 *         bonusPerSecondLeft and the remaining time.
	 */
	public int getBonus() {
		return Math.round((left / 60 * bonus));
	}

	private static final long serialVersionUID = 1L;
}
