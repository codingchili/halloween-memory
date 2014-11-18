package model;

/** 
 * @author Robin Duda

 * @version 1.0
 * @date 2014-10-26
 * 
 * Model.bot: Artificial Player.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import model.Card.Mode;
import model.Memory.PickResult;

public class Bot extends Player implements Serializable {
	private ArrayList<Card> knownCards; // cards and their known position.
	private Memory memory;
	private Random rand = new Random();

	private int minimumCapacity;	// if exceeded, will forget cards. 1-DECK_SIZE
	private int mindfulness;		// lower value, more likely to forget a card. 1-5

	/*** constructor with specified difficulty. */
	public Bot(Memory memory, Intelligence difficulty) {
		super("AI");
		knownCards = new ArrayList<Card>();
		this.memory = memory;

		// preset for difficulty levels.
		switch (difficulty) {
		case dumb:
			minimumCapacity = 2;
			mindfulness = 2;
			break;
		case clever:
			minimumCapacity = 3;
			mindfulness = 3;
			break;
		case genius:
			minimumCapacity = 4;
			mindfulness = 4;
			break;
		case professor:
			minimumCapacity = 6;
			mindfulness = 5;
			break;
		default:
			break;
		}
	}

	/*** Finds a pair in cards with known position.
	 * @param cards returns the cards in the pair.
	 * @param deck the deck cards, check if the card still exists
	 * 	this parameter is no longer required, knownCards is updated 
	 * when any user picks a card.
	 * @return true if at least two known cards make up a pair. */
	private boolean hasPair(Card cards[], Deck deck) {
		int i, j;
		boolean result = false;

		// find a pair in knownCards.
		for (i = 0; i < knownCards.size(); i++) {
			for (j = 0; j < knownCards.size(); ++j) {
				if (knownCards.get(i).getType() == knownCards.get(j).getType()
						&& (i != j) && deck.cardNotRemoved(cards[0]) //TODO cardNotRemoved check not required anymore. - Remove
						&& deck.cardNotRemoved(cards[1])) {
					cards[0] = knownCards.get(i);
					cards[1] = knownCards.get(j);
					result = true;
				}
			}
		}
		return result;
	}

	/*** removes all cards of type from the known cards.
	 * @param type specifies the type of cards to be removed */
	private void removeCardByType(int type) {
		for (int i = knownCards.size() - 1; i >= 0; i--)
			if (knownCards.get(i).getType() == type)
				knownCards.remove(i);
	}

	/*** notification that a card has been picked, if not in knownCards then add. 
	 * @param card the card that has been picked during a game.*/
	public void notifyCardPicked(Card card) {
		boolean newcard = true;
		for (int i = 0; i < knownCards.size(); i++)
			if (knownCards.get(i).equals(card))
				newcard = false;
		// if the card has not already been picked and added to known cards, 
		// add it.
		if (newcard)
			knownCards.add(card);
	}

	/*** a pair has been picked, remove cards from knownCards. 
	 * @param card a card which specifies the pair picked in its getType()*/
	public void notifyPairPicked(Card card) {
		removeCardByType(card.getType());
	}

	/*** pick cards from the deck 
	 * @param deck cards in memory, if no known cards then cards from
	 * this pool will be selected.*/
	public void pick(Deck deck) {
		PickResult pair = PickResult.pair;
		Card cards[] = new Card[2];

		// the bot will continue to pick cards while it knows a pair,
		// this could be unfair, the bot should "think" (wait) before
		// picking more cards, currently it will gain lots of bonus points.
		while (pair == PickResult.pair) {

			// if there is a pair in the known cards pick them.
			if (hasPair(cards, deck)) {
				memory.pickCard(cards[0]);
				pair = memory.pickCard(cards[1]);
			} else {
				do {
					// no pair in known cards, generate two random cards.
					cards[0] = deck.get(rand.nextInt(deck.getSize()));
					cards[1] = deck.get(rand.nextInt(deck.getSize()));
				} while ((cards[0].getMode() == Mode.Removed || cards[1]
						.getMode() == Mode.Removed)
						|| (cards[0].equals(cards[1])));

				memory.pickCard(cards[0]);
				pair = memory.pickCard(cards[1]);
			}
		}

		// at the end of turn, remove a card. The bot acts human,
		// forgetting where the cards are.
		if (knownCards.size() > minimumCapacity) {
			if (rand.nextInt(mindfulness) == 0) {
				knownCards.remove(rand.nextInt(knownCards.size()));
			}
		}
	}
	
	/*** Used by the bot controller to determine how fast cards should be
	 * picked.
	 * @return the defined mindfulness score.
	 */
	public int getMindfulness() {
		return mindfulness;
	}

	// defines the bot level, a clever bot will forget cards more often
	// than a professor.
	public static enum Intelligence {
		dumb, clever, genius, professor
	}

	private static final long serialVersionUID = 1L;
}
