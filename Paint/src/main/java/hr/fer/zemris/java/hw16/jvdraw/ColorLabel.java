package hr.fer.zemris.java.hw16.jvdraw;

import javax.swing.JLabel;

/**
 * Label that shows currently chosen colors of two {@link IColorProvider}s
 * @author ltomic
 *
 */
public class ColorLabel extends JLabel {

	/** Serial version */
	private static final long serialVersionUID = 1L;

	/** Background color label name part */
	private static final String BACKGROUND_NAME = "background";
	/** Foreground color label name part */
	private static final String FOREGROUND_NAME = "Foreground";
	/** Format of the single color string */
	private static final String COLOR_FORMAT = "%s color: (%3d, %3d, %3d)";
	/** Label format */
	private static final String LABEL_FORMAT = "%s, %s.";

	/** Changes the text of this label when foreground color is updated */
	private final ColorChangeListener outlineColorChangeListener = (source, oldColor, newColor) -> {
		String text = this.getText();
		text = text.substring(text.indexOf(BACKGROUND_NAME));
		this.setText(String.format(LABEL_FORMAT, 
				String.format(COLOR_FORMAT, FOREGROUND_NAME, newColor.getRed(), newColor.getGreen(), newColor.getBlue()),
				text));
	};
	
	/** Changes the text of this label when background color is updated */
	private final ColorChangeListener fillColorChangeListener = (source, oldColor, newColor) -> {
		String text = this.getText();

		text = text.substring(0, text.indexOf(BACKGROUND_NAME)-2);
		this.setText(String.format(LABEL_FORMAT,
				text,
				String.format(COLOR_FORMAT, BACKGROUND_NAME, newColor.getRed(), newColor.getGreen(), newColor.getBlue())
				));
	};
	
	/**
	 * Constructs {@link ColorLabel} with provided arguments and registers listeners to the providers
	 * @param outlineColorProvider - outline/foreground color provider
	 * @param fillColorProvider - fill/background color provider
	 */
	public ColorLabel(IColorProvider outlineColorProvider, IColorProvider fillColorProvider) {
		super();
		this.setText(formatLabel(0, 0, 0, 0, 0, 0));
		outlineColorProvider.addColorChangeListener(outlineColorChangeListener);
		fillColorProvider.addColorChangeListener(fillColorChangeListener);
		outlineColorChangeListener.newColorSelected(outlineColorProvider,
				outlineColorProvider.getCurrentColor(), outlineColorProvider.getCurrentColor());
		fillColorChangeListener.newColorSelected(fillColorProvider,
				fillColorProvider.getCurrentColor(), fillColorProvider.getCurrentColor());
	}
	
	/**
	 * Formats the text for this label
	 * @param fgR - foreground red color
	 * @param fgG - foreground green color
	 * @param fgB - foreground blue color
	 * @param bgR - background red color
	 * @param bgG - background green color
	 * @param bgB - background blue color
	 * @return {@link String} for the text of the label
	 */
	private String formatLabel(int fgR, int fgG, int fgB, int bgR, int bgG, int bgB) {
		return String.format(LABEL_FORMAT, 
				String.format(COLOR_FORMAT, FOREGROUND_NAME, fgR, fgG, fgB), 
				String.format(COLOR_FORMAT, BACKGROUND_NAME, bgR, bgG, bgB));
	}
	
	
}
