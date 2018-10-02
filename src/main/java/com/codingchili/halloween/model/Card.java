package com.codingchili.halloween.model;

import java.io.Serializable;

/**
 * @author Robin Duda
 * @version 1.0
 * @date 2014-10-26
 * 
 *       Model.card: card in the memory deck.
 */

public final class Card implements Serializable {
	private Id id; // members of a pair are unique, used to prevent a player
					// from picking the same card twice and gaining a pair.
	private int type; // pair member of type.
	private Mode mode; // the card mode, open, closed (flipped or not) or
						// removed (taken as pair).

	/***
	 * constructor for card class :p
	 * 
	 * @param type
	 *            type identifier, defines pairs.
	 * @param card
	 *            card id, uniqueness in pair.
	 */
	public Card(int type, Id card) {
		this.id = card;
		this.type = type;
		this.mode = Mode.Closed;
	}

	/*** @return pair membership identifier as integer. */
	public int getType() {
		return this.type;
	}

	/*** @return Card.Id is unique in pair, not in deck. */
	public Id getId() {
		return this.id;
	}

	/*** @return string representation of Card class, type + id. */
	public String toString() {
		return new String(type + "-" + id.toString());
	}

	/***
	 * sets the card mode, card modes Open, Closed or Removed.
	 * 
	 * @param mode
	 *            sets the mode of card, used when opening/closing/removing a
	 *            card.
	 */
	public void setMode(Mode mode) {
		this.mode = mode;
	}

	/***
	 * @return returns the mode of the card, mode depends on the cards state,
	 *         open, closed or removed from deck.
	 */
	public Mode getMode() {
		return this.mode;
	}

	/***
	 * cards are equal if type identifier and first/second card in pair equals.
	 * 
	 * @return returns true if cards are equal.
	 * @param a
	 *            second card form comparison.
	 */
	public boolean equals(Card other) {
		boolean result = false;

		if (other instanceof Card) {
			if (this.type == other.type && (this.id == ((Card) other).getId())) {
				result = true;
			}

		}
		return result;
	}

	public static enum Id {
		First, Second
	}; 

	public static enum Mode {
		Open, Closed, Removed, Disintegrated
	};

	private static final long serialVersionUID = 1L;
}
