package com.codingchili.halloween.view;

/** 
 * @author Gustaf Nilstadius
 * @version 1.0
 * @date 2014-10-26
 * 
 * View.button: Buttons for menus.
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Button {
	private final int X1, X2, Y1, Y2;
	private BufferedImage idleButton;
	private BufferedImage selectedButton;
	private final String buttonValue;
	
	
	//Returns true if image not found.
	public Button (String fileName, String buttonValue, int x1, int y1) {
		this.X1 = x1;
		this.Y1 = y1;
		this.buttonValue = buttonValue;
		InputStream is;

		
		try {
			is = this.getClass().getClassLoader().getResourceAsStream(fileName + ".png"); 
			idleButton = ImageIO.read(is);
			is = this.getClass().getClassLoader().getResourceAsStream(fileName + "select.png"); 
			selectedButton = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
			idleButton = null; 
			selectedButton = null;
		}
		
		this.Y2 = Y1 + idleButton.getHeight();
		this.X2 = X1 + idleButton.getWidth();
	}
	
	// button constructor for button class into button object.
	public Button (int width, String fileName, String buttonValue, int y1) {

		this.Y1 = y1;
		this.buttonValue = buttonValue;
		InputStream is;
		
		try {
			is = this.getClass().getClassLoader().getResourceAsStream(fileName + ".png"); 
			idleButton = ImageIO.read(is);
			is = this.getClass().getClassLoader().getResourceAsStream(fileName + "select.png"); 
			selectedButton = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
			idleButton = null; 
			selectedButton = null;
		}
		this.X1 = ((width-idleButton.getWidth())/2);
		this.Y2 = Y1 + idleButton.getHeight();
		this.X2 = X1 + idleButton.getWidth();

	}
	
	// creates a button from filename.
	public Button (String fileName) {
		this.X1 = 0;
		this.Y1 = 0;
		this.Y2 = 0;
		this.X2 = 0;
		this.buttonValue = null;
		InputStream is;
		
		
		try {
			is = this.getClass().getClassLoader().getResourceAsStream(fileName + ".png"); 
			idleButton = ImageIO.read(is);
			selectedButton = idleButton;	
		} catch (IOException e) {
			e.printStackTrace();
			idleButton = null; 
			selectedButton = null;
		}
	}
	

	// method for painting a button, x/y is mouse position.
	public void paint (Graphics2D g2d, int X, int Y){
		if(isInside(X,Y)){
			g2d.drawImage(selectedButton, null, X1, Y1);
		} else {
			g2d.drawImage(idleButton, null, X1, Y1);
		}
	}
	
	// paints button at its coordinates.
	public void paint (Graphics2D g2d){
		g2d.drawImage(idleButton, null, X1, Y1);
	}
	
	public void paintIdle (Graphics2D g2d){
		g2d.drawImage(idleButton, null, X1, Y1);
	}
	
	public void paintSelected (Graphics2D g2d){
		g2d.drawImage(selectedButton, null, X1, Y1);
	}
	
	
	public BufferedImage getIdleImage(){
		return idleButton;
	}
	public BufferedImage getSelectedImage(){
		return selectedButton;
	}
	
	public int getX(){
		return X1;
	}
	public int getY(){
		return Y1;
	}
	
	public String getValue(){
		return buttonValue;
	}
	
	// returns true if coordinates inside rect.
	public boolean isInside (int X, int Y){
		if (X >= X1 && X <= X2 && Y >= Y1 && Y <= Y2){
			return true;
		} else {
			return false;
		}
	}

}
