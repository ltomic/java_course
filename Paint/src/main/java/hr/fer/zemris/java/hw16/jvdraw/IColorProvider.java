package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;

/**
 * Keeps track of current color. Subject in Observer pattern where {@link ColorChangeListener} is observer.
 * @author ltomic
 *
 */
public interface IColorProvider {

	/**
	 * Return current color
	 * @return current color
	 */
	public Color getCurrentColor();
	/**
	 * Adds {@link ColorChangeListener}
	 * @param l - listener to add
	 */
	public void addColorChangeListener(ColorChangeListener l);
	/**
	 * Removes provided {@link ColorChangeListener}
	 * @param l - listener to remove
	 */
	public void removeColorChangeListener(ColorChangeListener l);
}
