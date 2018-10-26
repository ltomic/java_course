package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

/**
 * Component that enables a user to choose a color and stores that color.
 * Component is displayed as 15x15 rectangled filled with currently chosen color.
 * @author ltomic
 *
 */
public class JColorArea extends JComponent implements IColorProvider {

	/**  Serial version. */
	private static final long serialVersionUID = 1L;
	
	/**  Default {@link Dimension} of {@link JColorArea}. */
	private static final Dimension DEFAULT_PREFERRED_DIMENSION = new Dimension(15, 15);
	
	/**  Currently selected color. */
	private Color selectedColor;
	
	/**  List where {@link ColorChangeListener} are stored. */
	private List<ColorChangeListener> colorChangeListeners = new ArrayList<>();
	
	/**  Invokes a {@link JColorChooser} dialog when {@link JColorArea} is pressed on. */
	private MouseListener mouseClick = new MouseAdapter() {
		
		private static final String COLOR_CHOOSER_TITLE = "Choose color";

		@Override
		public void mousePressed(MouseEvent event) {
			Color chosenColor = JColorChooser.showDialog(event.getComponent().getParent(), COLOR_CHOOSER_TITLE, selectedColor);
			if (chosenColor != null) {
				setCurrentColor(chosenColor);
			}
		}
	};
	
	/**
	 * Constructs {@link JColorArea} with provided arguments.
	 *
	 * @param color - sets it as currently selected color
	 */
	public JColorArea(Color color) {
		this.selectedColor = color;
		this.addMouseListener(mouseClick);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(DEFAULT_PREFERRED_DIMENSION);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw16.jvdraw.IColorProvider#getCurrentColor()
	 */
	@Override
	public Color getCurrentColor() {
		return selectedColor;
	}
	
	/**
	 * Sets the current color.
	 *
	 * @param color the new current color
	 */
	private void setCurrentColor(Color color) {
		notifyListeners(selectedColor, color);
		this.selectedColor = color;
		this.repaint();
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw16.jvdraw.IColorProvider#addColorChangeListener(hr.fer.zemris.java.hw16.jvdraw.ColorChangeListener)
	 */
	@Override
	public void addColorChangeListener(ColorChangeListener l) {
		colorChangeListeners.add(l);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw16.jvdraw.IColorProvider#removeColorChangeListener(hr.fer.zemris.java.hw16.jvdraw.ColorChangeListener)
	 */
	@Override
	public void removeColorChangeListener(ColorChangeListener l) {
		colorChangeListeners.remove(l);
	}
	
	/**
	 * Notify listeners.
	 *
	 * @param oldColor the old color
	 * @param newColor the new color
	 */
	public void notifyListeners(Color oldColor, Color newColor) {
		colorChangeListeners.forEach((listener) -> {
			listener.newColorSelected(this, oldColor, newColor);
		});
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(selectedColor);
		g.fillRect(0, 0, DEFAULT_PREFERRED_DIMENSION.width, DEFAULT_PREFERRED_DIMENSION.height);
	}
	
	
	
	
}
