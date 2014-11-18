package View;

/** 
 * @author Gustaf Nilstadius
 * @version 1.0
 * @date 2014-10-26
 * 
 * View.Parallax: parallax background effects.
 */

import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Parallax implements Paintable {
	private int X, Y;
	private int with, height;
	private ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	private String[] file = new String[] {"parallax1.png", "parallax2.png", "parallax3.png", "parallax4.png"};
	
	public Parallax(int with, int height){
		this.with = with;
		this.height = height;
		InputStream is;
		
		// read in the background image layers.
		for (int i = 0; i < BACKGROUND_LAYERS; i++)
		{
			is = this.getClass().getClassLoader().getResourceAsStream(file[i]); 
			try {
				// package org.imgscalr.Scalr
				//resized = Scalr.resize(ImageIO.read(is), Scalr.Method.BALANCED, width + 190, height + 0);
				images.add(ImageIO.read(is));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		scale();
		//System.out.println("image with: " + images.get(0).getWidth() + " with = " + with);
	}
	
	private void scale (){
		BufferedImage tmp;
		int scaleWidth, scaleHeight;
		if ( (double)(this.with /this.height) <= (double) (images.get(0).getWidth()/images.get(0).getHeight()) ){
			scaleWidth = ((images.get(0).getWidth()*this.height)/images.get(0).getHeight());
			scaleHeight = this.height;
		} else {
			scaleHeight = (images.get(0).getHeight()/(images.get(0).getWidth()*this.with));
			scaleWidth = this.with;
		}
		scaleWidth+=(2*(this.with/2)/(5));
		scaleHeight+=(2*(this.height/2)/(10));
		for(int i = 0; i < images.size(); i++){
			tmp = new BufferedImage(scaleWidth, scaleHeight, images.get(0).getType());
			Graphics2D g = tmp.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(images.get(i), 0, 0, scaleWidth, scaleHeight, null);
			images.remove(i);
			images.add(i, tmp);
			g.dispose();
		}
		
		 
		
	}
	
	
	// paintable interface method.
	public void paint (Graphics g){
		Graphics2D g2d = (Graphics2D) g.create();
		for (int i = 0; i < images.size(); i++) { 
			g2d.drawImage(images.get(i), null, ((with - images.get(i).getWidth())/2) + ((X - with/2)/(5*((4-i)+1))) , 
					((height - images.get(i).getHeight())/2) + ((Y- height/2)/(10*((4-i)+1)) )  );
		}
		g2d.dispose();
				

	}

	
	// updates the position of the background layers according to mouse positions.
	@Override
	public boolean update(int X, int Y) {
		this.X = X;
		this.Y = Y;
		return false;
	}


	@Override
	public String getButton(int X, int Y) {
		return null;
	}

	private static int BACKGROUND_LAYERS = 4;
}
