package com.codingchili.halloween.view;

/** 
 * @author Robin Duda
 * @version 1.0
 * @date 2014-10-26
 * 
 * Model.ImageData: for passing animation state data. 
 */

import java.awt.image.BufferedImage;

public final class ImageData {
	BufferedImage image;
	int width, height, x, y;
	
	// just for passing data.
	public ImageData(BufferedImage image, int x, int y, int width, int height)
	{
		this.image = image;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}
