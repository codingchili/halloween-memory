import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import View.View;

/**
 * @author Robin Duda
 * @author Gustaf Nilstadius
 * @version 1.0
 * @date 2014-10-26
 * 
 *       The mains.
 */

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame(
				"Halloween Memory - Robin Duda, Gustaf Nilstadius. 2014");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		if ((screenSize.getWidth() < 1024 || screenSize.getWidth() > 2770)
				|| (screenSize.getHeight() < 720
						|| screenSize.getHeight() > 1600))
			JOptionPane.showMessageDialog(frame,
					"Resolution [" + Math.round(screenSize.getWidth()) + "x"
							+ Math.round(screenSize.getHeight())
							+ "] Not Supported!\r\n"
							+ "Requires [1024x720] to [2770x1600].",
					"Resolution Error!", JOptionPane.ERROR_MESSAGE);
		else {
			frame.setUndecorated(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.getContentPane().setBackground(new Color(0, 0, 0));
			frame.setVisible(true);
			View view = new View(frame, screenSize);
			frame.getContentPane().add(view);
			frame.setVisible(true);
		}
	}
}
