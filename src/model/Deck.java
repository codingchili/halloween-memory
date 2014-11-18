package model;

/** 
 * @author Robin Duda
 * @version 1.0
 * @date 2014-10-26
 * 
 * Model.deck: a deck of cards, cards come in pair. 
 * Cards are scrambled.
 */

import java.io.Serializable;
import java.util.Random;

import model.Card.Mode;

public class Deck implements Serializable {
	private Card[] deck = new Card[20];

	/***
	 * Calls deck.reset on instantiation.
	 * 
	 * @see Deck#reset()
	 */
	public Deck() {
		this.reset();
	}

	/***
	 * @return if all the cards in deck has state Mode.removed then the deck is
	 *         empty and the method returns true.
	 */
	public boolean isEmpty() {
		boolean result = true;

		// scan through the cards in deck.
		for (int i = 0; i < Memory.DECK_SIZE; i++)
			if (deck[i].getMode() != Mode.Removed)
				result = false;

		return result;
	}

	/***
	 * @return returns the size of the deck, how many cards are in the deck.
	 *         Returns Memory.DECK_SIZE.
	 * @see Memory#DECK_SIZE
	 */
	public int getSize() {
		return Memory.DECK_SIZE;
	}

	/***
	 * checks if the card is in the deck, checks are done using equals.
	 * 
	 * @return true/false - whether or not the deck contains the card.
	 * @see Card#equals()
	 */
	public boolean contains(Card card) {
		boolean result = false;
		for (int i = 0; i < Memory.DECK_SIZE; i++)
			if (deck[i].getMode() != Mode.Removed && deck[i].equals(card))
				result = true;

		return result;
	}

	/***
	 * returns a card from the deck given by param.
	 * 
	 * @param index
	 *            index of card to return from deck.
	 * @return card in deck with specified index from param.
	 */
	public Card get(int index) {
		return deck[index];
	}

	/***
	 * Get full contents of Deck.
	 * 
	 * @return a copy of all cards in Deck.
	 */
	public Card[] getAll() {
		Card[] copy = new Card[Memory.DECK_SIZE];
		System.arraycopy(deck, 0, copy, 0, deck.length);
		return copy;
	}

	/***
	 * removes all card in deck by type, call when a pair has been picked.
	 * 
	 * @param cards
	 *            that match the type parameter will be removed.
	 */
	public void removeByType(int type) {
		for (int i = Memory.DECK_SIZE - 1; i >= 0; i--) {
			if (deck[i].getType() == type) {
				deck[i].setMode(Mode.Removed);
			}
		}
	}

	/***
	 * check if a card is still in the deck. (not flagged as Mode.Removed)
	 * 
	 * @param card
	 *            the card to search for.
	 * @return returns true if a card is still in the deck.
	 * @see Card#Mode
	 * @obsolete used for testing, use Deck.Contains instead.
	 * @see Deck#contains(Card)
	 */
	public boolean cardNotRemoved(Card card) {
		boolean result = true;

		for (int i = 0; i < Memory.DECK_SIZE; i++)
			if (deck[i].equals(card) && ((deck[i].getMode() == Mode.Removed)))
				result = false;

		return result;
	}

	/*** regenerate all cards with random positioning, reset their states (Mode)
	 * @see Card#Mode */
	public void reset() {
		Random rand = new Random();
		int iterations = 20;
		Card tmp;
		int pos1, pos2, i = 0, type = 0;

		// loop through 0 - DECK_SIZE/2 generating two cards for every step
		// these two cards represents a pair, and is unique within the pair
		// given the Card.Id.First or Card.Id.Second.
		while (i < Memory.DECK_SIZE) {
			deck[i] = (new Card(type, Card.Id.First));
			deck[i + 1] = (new Card(type, Card.Id.Second));
			i += 2;
			type++;
		}

		while (iterations > 0) {
			pos1 = rand.nextInt(Memory.DECK_SIZE);

			do {
				pos2 = rand.nextInt(Memory.DECK_SIZE);
			} while (pos1 == pos2);

			tmp = deck[pos2];
			deck[pos2] = deck[pos1];
			deck[pos1] = tmp;
			iterations--;
		}
	}

	/*** use this method to get the index of a card in the deck.
	 * 
	 * @param card the card which the index should be found for.
	 * @return returns the index of the found card, card flagged with
	 * Card#Mode.Removed returns index of -1.
	 */
	public int index(Card card) {
		int result = -1;
		
		// cards flagged with Mode.Removed does not return as index.
		for (int i = 0; i < Memory.DECK_SIZE; i++) {
			if (deck[i].getMode() != Mode.Removed && card.equals(deck[i]))
				result = i;
		}

		return result;
	}

	/*** 
	 * Set a new mode for an existing card.
	 * @param mode The new mode of the card.
	 * @param index The index of the card.
	 */
	public void setMode(Mode mode, int index) {
		deck[index].setMode(mode);
	}

	private static final long serialVersionUID = 1L;
}
