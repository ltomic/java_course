package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;

/**
 * Implements observer in Observer pattern where IColorProvider is the Subject.
 * @author ltomic
 *
 */
public interface ColorChangeListener {

	/**
	 * Invoked by the subject when color has changed
	 * @param source - color provider
	 * @param oldColor - old color
	 * @param newColor - new color
	 */
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
}
