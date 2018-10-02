package com.codingchili.halloween.view;

/** 
 * @author Gustaf Nilstadius
 * @version 1.0
 * @date 2014-10-26
 * 
 * View.PauseMenu: pause menu.
 */

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;


public class PauseMenu implements Paintable {
	
	private ArrayList<Button> buttons = new ArrayList<Button>();
	private int width, height;
	private int X,Y;
	
	public PauseMenu (int width, int height){
		this.width = width;
		this.height = height;
		
		int placmentY = Math.round(height / 2 - (400 / 2) - 40);
		buttons.add(new Button(this.width, "KeepPlay", "Keep Play", placmentY));
		placmentY+= 80;
		buttons.add(new Button(this.width, "MainMenu", "Main Menu", placmentY));
		placmentY+= 80;
		buttons.add(new Button(this.width, "HowToPlay", "How To Play", placmentY));
		placmentY+= 160;
		buttons.add(new Button(this.width, "Quit", "Quit", placmentY));
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
		}
		g2d.dispose();

	}

	@Override
	public boolean update(int X, int Y) {
		this.X = X;
		this.Y = Y;
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



}
