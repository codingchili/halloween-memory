package com.codingchili.halloween.view;

/** 
 * @author Robin Duda
 * @author Gustaf Nilstadius
 * @version 1.0
 * @date 2014-10-26
 * 
 * View.CardView: The game screen, cards, player scores, animations.
 */

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.codingchili.halloween.view.BlendState.Direction;
import com.codingchili.halloween.model.Card;
import com.codingchili.halloween.model.Card.Mode;
import com.codingchili.halloween.model.Memory;

public class CardView implements Paintable {
	private ArrayList<BufferedImage> images = new ArrayList<BufferedImage>(); // imageState
	private int width, height;
	private String[] cardFiles = { "1.png", "2.png", "3.png", "4.png", "5.png",
			"6.png", "7.png", "8.png", "9.png", "10.png" };
	private BufferedImage back;
	private Memory memory;
	private BlendState[] alpha;
	private AnimationState[] animations;
	private ArrayList<Button> buttons = new ArrayList<Button>();
	private boolean finished = false;
	private int mouseX = 0, mouseY = 0; 

	public CardView(int width, int height, Memory memory) {
		this.width = width;
		this.height = height;
		this.memory = memory;
		alpha = new BlendState[Memory.DECK_SIZE];
		animations = new AnimationState[Memory.DECK_SIZE];

		// top left pause button
		buttons.add(new Button("Pause", "Pause", 3, 3));

		// create blend effects.
		for (int i = 0; i < Memory.DECK_SIZE; i++)
			alpha[i] = new BlendState();

		// read in image resources from InputStream with classloaders.
		try {
			// read in back image of cards.
			InputStream is = this.getClass().getClassLoader()
					.getResourceAsStream("back.png");
			back = (ImageIO.read(is));

			// read in memory card images.
			for (int i = 0; i < 10; i++) {
				InputStream card_image_stream = this.getClass()
						.getClassLoader().getResourceAsStream(cardFiles[i]);
				images.add(ImageIO.read(card_image_stream));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// update the used model, required when loading new memory from
	// MemorySerializer.
	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	// reset the draw area to make ready for new game.
	public void newGame() {
		int x = 0;
		int y = 0;
		Card[] cards = memory.getDeck();

		// reset the animations and the alpha levels.
		for (int i = 0; i < Memory.DECK_SIZE; i++) {
			alpha[i] = new BlendState();
			animations[i] = new AnimationState(back, x
					* CARD_WIDTH
					+ Math.round(width / 2
							- (((CARD_COLS + 1) * CARD_WIDTH) / 2)), y
					* CARD_HEIGHT
					+ Math.round(height / 2 - (CARD_ROWS * CARD_WIDTH / 2)));

			// if game is resumed, do remove old pairs at startup.
			if (cards[i].getMode() == Mode.Removed) {
				animations[i].setMode(Mode.Removed);
				alpha[i].setAlpha(0f);
				alpha[i].setDirection(Direction.Fade);
			}

			// keep track of card position, used to generate hitboxes.
			x++;
			if ((i + 1) % (CARD_COLS) == 0) {
				y++;
				x = 0;
			}
		}
	}

	// draws the current score for players in the top-bar.
	public void scoring(Graphics2D g2d) {
		int score[] = memory.getScoring();
		String[] players = memory.getPlayerNames();
		Font font = new Font("Arial", Font.PLAIN, 35);
		g2d.setFont(font);

		// enable AA, the text will now be readable too.
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		//g2d.drawImage(topBar, null, 0, 0);
		
		AlphaComposite ac = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 0.5f);
		g2d.setComposite(ac);
		g2d.setColor(new Color(0, 0, 0));
		g2d.fillRect(0, 0, width, 50);
		ac = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 1.0f);
		g2d.setComposite(ac);

		// detect whose turn it is, and draw them in another color.
		// the active color is not predetermined.
		if (players[0].equals(memory.whoseTurn())) {
			g2d.setColor(new Color(255, 255, 0));
			g2d.drawString(
					players[0],
					(this.width
							/ 2
							- (int) g2d.getFontMetrics()
									.getStringBounds(players[0], g2d)
									.getWidth() - 60), 40);
			g2d.setColor(new Color(220, 135, 0));
			g2d.drawString(players[1], (this.width / 2 + 60), 40);
		} else {
			g2d.setColor(new Color(255, 255, 0));
			g2d.drawString(players[1], (this.width / 2 + 60), 40);
			g2d.setColor(new Color(220, 135, 0));
			g2d.drawString(
					players[0],
					(this.width
							/ 2
							- (int) g2d.getFontMetrics()
									.getStringBounds(players[0], g2d)
									.getWidth() - 60), 40);
		}

		// draw the current score
		g2d.setColor(new Color(92, 51, 23));
		g2d.drawString(
				score[0] + "  " + score[1],
				(this.width / 2 - ((int) g2d.getFontMetrics()
						.getStringBounds(score[0] + " " + score[1], g2d)
						.getWidth()) / 2), 40);
	}

	// main draw method, TODO reduce into smaller methods.
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		scoring(g2d);
		finished = true; // indicates if all cards are taken, and all animations
							// finished.

		// display the time left for bonus points.
		g2d.setColor(new Color(92, 51, 23));
		g2d.fillRect(this.width / 2 - (memory.getTimer().getReset() / 2), 52,
				memory.getTimer().getLeft(), 8);

		// check if any animations needs to be started.
		for (int i = 0; i < Memory.DECK_SIZE; i++) {
			Card[] card = memory.getDeck();

			if (animations[i].getMode() != card[i].getMode()
					&& animations[i].stopped()) {
				if (card[i].getMode() == Mode.Closed) {
					animations[i].animate(images.get(card[i].getType()), back,
							Mode.Closed);
				} else if (card[i].getMode() == Mode.Open) {
					animations[i].animate(back, images.get(card[i].getType()),
							Mode.Open);
				} else if (card[i].getMode() == Mode.Removed) {
					animations[i].animate(images.get(card[i].getType()),
							images.get(card[i].getType()), Mode.Removed);
				}
			}

			// wait for animations to finish before exiting the CardView to show HighScore.
			if (!animations[i].stopped() || !alpha[i].stopped())
				finished = false;

			// process animations and alphas.
			alpha[i].process();
			animations[i].process();

			// draw the cards in deck, applying animation and alpha levels.
			AlphaComposite ac = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, alpha[i].getAlpha());
			g2d.setComposite(ac);

			if (card[i].getMode() == Mode.Open
					|| card[i].getMode() == Mode.Removed) {
				ImageData data = animations[i].getImage();
				g2d.drawImage(data.image, data.x, data.y, data.width,
						data.height, null);
			} else if (card[i].getMode() == Mode.Closed) {
				ImageData data = animations[i].getImage();
				g2d.drawImage(data.image, data.x, data.y, data.width,
						data.height, null);
				finished = false;
			}
		}
		// draw the buttons in the current view.
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).paint(g2d, mouseX, mouseY);
		}

		g2d.dispose();
	}

	// detect which card was picked and flip it, also returns the selected card.
	public Card mouseDown(int x, int y) {
		int posx = 0;
		int posy = 0;
		Card[] cards = memory.getDeck();
		Card card = null;

		// find the card rect that was clicked.
		for (int i = 0; i < Memory.DECK_SIZE; i++) {
			if (new Rectangle(posx * CARD_WIDTH
					+ Math.round(width / 2 - (CARD_COLS * CARD_WIDTH / 2)),
					posy
							* CARD_HEIGHT
							+ Math.round(height / 2
									- (CARD_ROWS * CARD_WIDTH / 2)),
					CARD_WIDTH, CARD_HEIGHT).contains(new Point(x, y))) {

				if (null != cards[i] && cards[i].getMode() == Mode.Closed) {
					card = cards[i];
				}
			}

			// update the search region.
			posx++;
			if ((i + 1) % (CARD_COLS) == 0) {
				posy++;
				posx = 0;
			}
		}

		return card;
	}

	public void reset() {
		this.newGame();
	}

	public boolean finished() {
		return finished;
	}

	// a pair was picked, start the animations.
	public void pairPicked() {
		Card[] card = memory.getDeck();

		for (int i = 0; i < Memory.DECK_SIZE; i++) {
			if (card[i].getMode() == Mode.Removed)
				if (alpha[i].getDirection() == BlendState.Direction.Saturate)
					alpha[i].animate(BlendState.Direction.Fade);

		}
	}

	@Override
	public boolean update(int X, int Y) {
		mouseX = X;
		mouseY = Y;
		return false;
	}

	// perform button check for press.
	@Override
	public String getButton(int X, int Y) {
		for (int i = 0; i < buttons.size(); i++) {
			if (buttons.get(i).isInside(X, Y)) {
				return buttons.get(i).getValue();
			}
		}
		return null;
	}

	public static final int CARD_WIDTH = 125;
	public static final int CARD_HEIGHT = 125;
	private static final int CARD_ROWS = 4;
	private static final int CARD_COLS = 5;
}
