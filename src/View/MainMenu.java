package View;

/** 
 * @author Gustaf Nilstadius
 * @version 1.0
 * @date 2014-10-26
 * 
 * View.MainMenu: The main menu!
 */

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

public class MainMenu implements Paintable{
	
	private ArrayList<Button> buttons = new ArrayList<Button>();
	private int width, height;
	private int X,Y, colorMod;
	private Color noticeColor;
	
	
	// total size (550 + 250)
	public MainMenu (int width, int height){
		this.width = width;
		this.height = height;
		int placmentY = Math.round(height / 2 - (400 / 2) - 40);
		buttons.add(new Button(this.width, "PlayerVsBot", "New Player vs Bot", placmentY));
		placmentY+= 80;
		buttons.add(new Button(this.width, "PlayerVsPlayer", "New Player vs Player", placmentY));
		placmentY+= 80;
		buttons.add(new Button(this.width, "ResumeGame", "Resume Game", placmentY));
		placmentY+= 80;
		buttons.add(new Button(this.width, "HighScore", "High Score", placmentY));
		placmentY+= 80;
		buttons.add(new Button(this.width, "HowToPlay", "How To Play", placmentY));
		placmentY+= 145;
		buttons.add(new Button(this.width, "Quit", "Quit", placmentY));
		this.noticeColor = new Color(255, 253, 0);
		this.colorMod = -2;
	}
	
	// display copyright on main menu for artwork contained. ~RD
	public void copyRightNotice(Graphics2D g2d)
	{
		if (noticeColor.getGreen() < 2 || noticeColor.getGreen() >= 254)
			colorMod *= -1;

		Color newColor = new Color(noticeColor.getRed(), noticeColor.getGreen() + colorMod, noticeColor.getBlue());
		noticeColor = newColor;
		Font font = new Font("Arial", Font.PLAIN, 14);
		g2d.setFont(font);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			g2d.setColor(noticeColor);
			g2d.drawString(COPYRIGHT_NOTICE, width / 2 - ((int) g2d.getFontMetrics()
					.getStringBounds(COPYRIGHT_NOTICE, g2d)
					.getWidth() / 2), height - 32);
			
			g2d.drawString(PROGRAMMER_NOTICE, width / 2 - ((int) g2d.getFontMetrics()
					.getStringBounds(PROGRAMMER_NOTICE, g2d)
					.getWidth() / 2), height - 56);
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		
		AlphaComposite ac = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 0.5f);
		g2d.setComposite(ac);
		g2d.setColor(new Color(0, 0, 0));
		g2d.fillRect(0, 0, width, height);
		ac = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 1.0f);
		g2d.setComposite(ac);
		
		for (int i = 0; i < buttons.size(); i++) { 
			buttons.get(i).paint(g2d, X, Y);
			/*if (i == mouseAbove){
				buttons.get(i).paintIdle(g2d);		
			} else {
				buttons.get(i).paintSelected(g2d);
			}*/
		}
		copyRightNotice(g2d);
		g2d.dispose();

	}

	@Override
	public boolean update(int X, int Y) {
		this.X = X;
		this.Y = Y;
		/*mouseAbove = -1;
		for (int i = 0; i < buttons.size(); i++) {
			if(buttons.get(i).isInside(X, Y)){
				mouseAbove = i;
			}
		}
		*/
		return false;
	}

	@Override
	public String getButton(int X, int Y) {
		for (int i = 0; i < buttons.size(); i++) {
			if(buttons.get(i).isInside(X, Y)){
				return buttons.get(i).getValue();
			}
		}
		return null;
	}

	private static final String PROGRAMMER_NOTICE = "Programmers Robin Duda & Gustaf Nilstadius. 2014";
	private static final String COPYRIGHT_NOTICE = "Contains artwork by Arrioch (Milos Mirkovic) [CC BY-NC-ND 4.0].";
}
