package View;

/** 
 * @author Robin Duda
 * @authoe Gusaf Nilstadius
 * @version 1.0
 * @date 2014-10-26
 * 
 * View.EndOfGame/HighScore: Displays End of Game Screen and/or winner and new high-scores.
 */

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import model.HighScoreList;

public class HighScore implements Paintable {
	private Button close;
	private int width, height, posX, posY, X, Y;
	private BufferedImage background;
	private HighScoreList highScoreList;
	private String winnerName;
	private int winnerScore;
	
	public HighScore(int width, int height) {
		try {
			// load the background image and the overlay.
			InputStream is = this.getClass().getClassLoader()
					.getResourceAsStream("HighScoreSplash.png");
			background = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.winnerName = null;
		this.highScoreList = new HighScoreList();
		this.width = width;
		this.height = height;
		this.posX = Math.round((width / 2) - (WIDTH / 2));
		this.posY = Math.round((height / 2) - (HEIGHT / 2));
		close = new Button(this.width, "Close", "CloseHighScore", posY + HEIGHT
				- (BUTTON_HEIGHT + 24));
	}

	// main draw call. TODO split into smaller methods.
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		Font font = new Font("Arial", Font.PLAIN, 22);
		g2d.setFont(font);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		AlphaComposite ac = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 0.5f);
		g2d.setComposite(ac);
		g2d.setColor(new Color(0, 0, 0));
		g2d.fillRect(0, 0, width, height);
		ac = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 1.0f);
		g2d.setComposite(ac);
		
		g2d.drawImage(background, null, posX, posY);

		g2d.setColor(new Color(0, 0, 0));

		// if there is no winnerName set, just display the scores.
		if (winnerName != null)
		{
			// if winner is set, display name and the score.
			g2d.drawString(winnerName + " Wins! Score " + winnerScore, (this.posX + 200), this.posY + 80);
		}
		
		// draw column captions
		font = new Font("Arial", Font.BOLD, 22);
		g2d.setFont(font);
		g2d.drawString("Place", (this.posX + 90), this.posY + 145);
		g2d.drawString("Name", (this.posX + 175), this.posY + 145);
		g2d.drawString("Score", (this.posX + 400), this.posY + 145);
		
		font = new Font("Arial", Font.PLAIN, 22);
		g2d.setFont(font);
		// draw all highscore items to the board.
		for (int i = 0; i < HighScoreList.ENTRIES - 1 && Integer.parseInt(highScoreList.getScore(i)) > 0; i++) {
			g2d.drawString((i+1) + ".", (this.posX + 90), this.posY + 175 + (22 * i));
			g2d.drawString(highScoreList.getName(i), (this.posX + 175), this.posY + 175 + (22 * i));
			g2d.drawString(highScoreList.getScore(i), (this.posX + 400), this.posY + 175 + (22 * i));
		}

		close.paint(g2d, X, Y);
	}

	// update the high-scores.
	public void updateHighScore(String name, int score)
	{
		this.highScoreList.update(name, score);
	}
	
	@Override
	public boolean update(int X, int Y) {
		this.X = X;
		this.Y = Y;
		return false;
	}

	// check for button press.
	@Override
	public String getButton(int X, int Y) {
		if (close.isInside(X, Y)) {
			return close.getValue();
		}
		return null;
	}
	
	public void clearWinner()
	{
		this.winnerName = null;
	}

	private static int WIDTH = 600;
	private static int HEIGHT = 558;
	private static int BUTTON_HEIGHT = 70;

	public void setWinner(String whoseTurn, int winnerScore) {
		this.winnerName = whoseTurn;
		this.winnerScore = winnerScore;
	}
}
