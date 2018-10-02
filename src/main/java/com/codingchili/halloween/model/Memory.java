package com.codingchili.halloween.model;

/** 
 * @author Robin Duda
 * @version 1.0
 * @date 2014-10-26
 * 
 * Model.memory: Ruleset for Memory game.
 */

import java.io.Serializable;
import java.util.ArrayList;

import com.codingchili.halloween.model.Card.Mode;

public class Memory implements Serializable {
	private Player player1;
	private Player player2;
	private Player turn;
	private Deck deck;
	private ArrayList<Card> pickedCards;
	private TurnTimer timer;

	public Memory() {
		this.deck = new Deck();
		this.pickedCards = new ArrayList<Card>();
		this.timer = new TurnTimer(60 * 5, 1);
	}

	/***
	 * create a new game, with specified GameMode.
	 * 
	 * @param mode
	 *            GameMode type specifies players/bots in the game.
	 * @see Memory#GameMode
	 * @remarks creating a new game with bots does not start the bots, this must
	 *          instead be done from the Class who called upon newGame. the
	 *          model cannot create threads! (Serializability) Starting a bot
	 *          requires a bot driver, which will create a thread to operate on
	 *          the model object Bot.
	 * @see BotController
	 */
	public void newGame(GameMode mode) {
		deck.reset(); // regenerate the deck.
		pickedCards.clear(); // clear previously picked cards.
		timer.start(); // reset and restart the timer.
		timer.reset();

		// depending on the mode parameter create players or bots.
		switch (mode) {
		case PlayerVsPlayer:
			player1 = new Player("Player 1");
			player2 = new Player("Player 2");
			break;
		case PlayerVsAI:
			player1 = new Player("Player 1");
			player2 = new Bot(this, Bot.Intelligence.dumb);
			break;
		case AIVsAI:
			player1 = new Bot(this, Bot.Intelligence.professor);
			player1.setName("AI 1");
			player2 = new Bot(this, Bot.Intelligence.professor);
			player2.setName("AI 2");
			turn = player1;
		default:
			break;
		}
		turn = player1;
	}

	/***
	 * returns a string of whose turn it is, to identify their turn. called from
	 * the bot controller to determine when its time to pull cards. Also called
	 * from the Card View, to highlight the current player.
	 */
	public String whoseTurn() {
		return turn.getName();
	}

	/***
	 * 
	 * @return returns the score of the winner. If the players are tied, the
	 *         player who last picked card is chosen as a winner.
	 */
	public int winnerScore() {
		if (player1.getScore() > player2.getScore()) {
			return player1.getScore();
		} else if (player2.getScore() > player1.getScore())
			return player2.getScore();
		else
			return turn.getScore();
	}

	/***
	 * 
	 * @return returns the name of the winner. If the players are tied, the
	 *         player who last picked card is chosen as a winner.
	 */
	public String winnerName() {
		if (player1.getScore() > player2.getScore()) {
			return player1.getName();
		} else if (player2.getScore() > player1.getScore())
			return player2.getName();
		else
			return turn.getName();
	}

	/***
	 * @return true when the cards are all taken, this indicates that the game is
	 *         finished.
	 */
	public boolean isFinished() {
		if (deck.isEmpty())
			return true;
		else
			return false;
	}

	/***
	 * notify the AI that a card has been picked from deck.
	 * 
	 * @param card
	 *            the picked card.
	 */
	private void notifyAICardPicked(Card card) {
		if (player1 instanceof Bot)
			((Bot) player1).notifyCardPicked(card);
		if (player2 instanceof Bot)
			((Bot) player2).notifyCardPicked(card);
	}

	/***
	 * notify the AI that a pair has been picked from deck.
	 * 
	 * @param card
	 *            contains the type of the pair.
	 */
	private void notifyAIPairPicked(Card card) {
		if (player1 instanceof Bot)
			((Bot) player1).notifyPairPicked(card);
		if (player2 instanceof Bot)
			((Bot) player2).notifyPairPicked(card);
	}

	/*** clears previously picked cards from the deck. */
	private void clearPreviousCards() {
		int previousCards = 0;
		Card[] cards = deck.getAll();

		// detects how many previously picked cards there are in the deck.
		for (int i = 0; i < Memory.DECK_SIZE; i++)
			if (cards[i].getMode() == Mode.Open)
				previousCards++;

		// if the previously picked cards exceed 2, then the previous cards
		// should be flipped back. By setting their mode to closed.
		if (previousCards == 2) {
			for (int i = 0; i < Memory.DECK_SIZE; i++)
				if (cards[i].getMode() == Mode.Open)
					cards[i].setMode(Mode.Closed);
			previousCards = 0;
		}
	}

	/***
	 * method for picking cards from the deck.
	 * 
	 * @param card
	 *            card to pick from deck
	 * @return returns a PickResult
	 * @see PickResult
	 */
	public PickResult pickCard(Card card) {
		PickResult result = PickResult.none;
		int index = deck.index(card);
		clearPreviousCards();
		deck.setMode(Mode.Open, index);

		//System.out.println(this.whoseTurn() + " picked card: "+ card.toString());

		if (!deck.contains(card)) {
			// the card does not exist in deck, no action.
			// PickResult is none.
		} else {
			pickedCards.add(card);
			notifyAICardPicked(card);

			// if two cards has been picked, the user input for the turn is
			// complete.
			// check the two cards if its a pair, or if its a fail.
			if ((pickedCards.size() % 2) == 0) {
				if (pickedCards.get(0).getType() == pickedCards.get(1)
						.getType()
						&& (pickedCards.get(0).getId() != pickedCards.get(1)
								.getId())) {
					// the player has picked a pair, update score and remove the
					// cards from deck.
					turn.addScore(5 + timer.getBonus());
					deck.removeByType(pickedCards.get(0).getType());

					// notify the AI that a pair has been picked.
					notifyAIPairPicked(card);
					result = PickResult.pair;
					//System.out.println(this.whoseTurn() + " got pair !");
				} else {
					// the two cards picked did not form a pair, such sadness,
					// very failure.
					// set turn to the next player.
					result = PickResult.fail;
					pickedCards.clear();
					if (!deck.isEmpty())
						this.nextTurn();
				}
				pickedCards.clear();
			}
		}

		// if the deck is empty after pick, finished is returned to indicate a
		// game end.
		if (deck.isEmpty())
			result = PickResult.finished;

		return (result);
	}

	/*** the bot controller should call this on their turn, to activate the card picker.
	 * @remarks this method is called from a thread. */
	public void turn() {
		if (turn instanceof Bot)
			((Bot) turn).pick(deck);
	}

	/*** rotate turn, player/bot failed to pick a pair. */
	private void nextTurn() {
		timer.reset();

		if (player1.equals(turn))
			turn = player2;
		else
			turn = player1;
	}

	/***
	 * returns the current scoring for the players.
	 * 
	 * @return returns the scoring in as int array, [player1, player2].
	 */
	public int[] getScoring() {
		return new int[] { player1.getScore(), player2.getScore() };
	}

	/***
	 * returns the player names.
	 * 
	 * @return returns the scoring in as String array, [player1, player2].
	 */
	public String[] getPlayerNames() {
		return new String[] { player1.getName(), player2.getName() };
	}

	/***
	 * 
	 * @return array of cards from deck. the returned array is a copy of the cards.
	 */
	public Card[] getDeck() {
		return deck.getAll();
	}

	/*** @return returns true if its a players turn and not a bot.*/
	public boolean getPlayerTurn() {
		if (!(turn instanceof Bot))
			return true;
		else
			return false;
	}

	/*** returns the timer object. */
	public TurnTimer getTimer() {
		return timer;
	}
	
	/*** returns the bot object. Bot controller cannot inherit any model classesn*/
	public Bot getBot() {
		if (player1 instanceof Bot)
			return (Bot) player1;
		else if (player2 instanceof Bot)
			return (Bot) player2;
		return null;
	}

	/*** the pickresult indicates the return status from pickCard method. */
	public static enum PickResult {
		none, pair, fail, finished
	}

	public static final int DECK_SIZE = 20;

	public static enum GameMode {
		PlayerVsPlayer, PlayerVsAI, AIVsAI
	}

	private static final long serialVersionUID = 1L;
}
