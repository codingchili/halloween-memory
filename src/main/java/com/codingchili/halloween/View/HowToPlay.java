package com.codingchili.halloween.view;

/** 
 * @author Robin Duda
 * @version 1.0
 * @date 2014-10-26
 * 
 * View.HowToPlay: displays a box with HowToPlays
 */

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class HowToPlay implements Paintable {
	private Button close;
	private int width, posX, posY, X, Y;
	private BufferedImage background;
	
	public HowToPlay(int width, int height)
	{
		// load the background image.
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("HowToPlaySplash.png"); 
		try {
			background = ImageIO.read(is);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.width = width;
		this.posX = Math.round((width / 2) - (WIDTH / 2));
		this.posY = Math.round((height / 2) - (HEIGHT / 2));
		close = new Button(this.width, "Close", "Close", posY + HEIGHT - (BUTTON_HEIGHT + 24));
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		
		g2d.drawImage(background, null, posX,  posY);
		close.paint(g2d, X, Y);
	}

	@Override
	public boolean update(int X, int Y) {
		this.X = X;
		this.Y = Y;
		return false;
	}

	@Override
	public String getButton(int X, int Y) {
			if(close.isInside(X, Y)){
				return close.getValue();
			}
		return null;
	}

	private static int WIDTH = 600;
	private static int HEIGHT = 558;
	private static int BUTTON_HEIGHT = 70;
}
