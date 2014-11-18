package View;

/** 
 * @author Gustaf Nilstadius
 * @version 1.0
 * @date 2014-10-26
 * 
 * View.Paintable: paintable interface.
 */

import java.awt.Graphics;

public interface Paintable {
	
	public abstract void paint(Graphics g);			// the paint method
	public abstract boolean update(int X, int Y);   // mouse position update for hovers etc.
	public abstract String getButton (int X, int Y); // button updates.
	

}
