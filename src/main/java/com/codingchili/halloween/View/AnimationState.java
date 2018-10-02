package com.codingchili.halloween.view;

import java.awt.image.BufferedImage;

import com.codingchili.halloween.model.Card;
import com.codingchili.halloween.model.Card.Mode;

/** 
 * @author Robin Duda
 * @version 1.0
 * @date 2014-10-26
 * 
 * View.AnimationState: animations for the Card Flip.
 */

public class AnimationState {
	private BufferedImage from;
	private BufferedImage to;
	private CurrentImage currentImage;
	private int width, height, originX, x, y;
	private boolean stopped;
	private Card.Mode mode;

	public AnimationState(BufferedImage standard, int x, int y) {
		this.from = standard;
		this.currentImage = CurrentImage.From;
		this.mode = Card.Mode.Closed;
		this.stopped = true;
		this.width = IMAGE_SIZE;
		this.height = IMAGE_SIZE;
		this.originX = x;
		this.x = x;
		this.y = y;
	}

	// transition by flip, from image from->to. Setting the animatio nmode
	// will cause the view to update the animationstate when it differs from source.
	public void animate(BufferedImage from, BufferedImage to, Card.Mode mode) {
		this.stopped = false;
		this.from = from;
		this.to = to;
		this.currentImage = CurrentImage.From;
		this.mode = mode;
	}
	
	//Setting the animatio nmode
	// will cause the view to update the animationstate when it differs from source.
	public void setMode(Mode mode)
	{
		this.mode = mode;
	}

	// returns animation data in ImageData object.
	public ImageData getImage() {
		if (currentImage == CurrentImage.From)
			return new ImageData(from, x, y, width, height);
		else
			return new ImageData(to, x, y, width, height);
	}

	/***
	 * reduces the width of the image until 0 is reached, where the image will
	 * be swapped and start to increase up to IMAGE_SIZE again.
	 */
	public void process() { 

		if (!stopped) {
			if (currentImage == CurrentImage.From) {
				width -= 24;

				if (width < 1)
					currentImage = CurrentImage.To;
			}

			if (currentImage == CurrentImage.To) {
				width += 24;

				if (width >= IMAGE_SIZE) {
					width = IMAGE_SIZE;
					stopped = true;
				}
			}
		}

		x = originX + IMAGE_SIZE - (width / 2);
	}

	// return the animation mode.
	public Card.Mode getMode() {
		return this.mode;
	}

	//indicates whether the animation is stopped or playing.
	public boolean stopped() {
		return stopped;
	}
	
	public static final int IMAGE_SIZE = 128;
	public static enum CurrentImage {
		From, To
	}
}
