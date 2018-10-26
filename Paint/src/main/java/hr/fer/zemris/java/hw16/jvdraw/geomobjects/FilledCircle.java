package hr.fer.zemris.java.hw16.jvdraw.geomobjects;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw16.jvdraw.editors.FilledCircleEditor;
import hr.fer.zemris.java.hw16.jvdraw.editors.GeometricalObjectEditor;

// TODO: Auto-generated Javadoc
/**
 * Filled circle is defined by its center point, radius, outline color and fill color.
 * @author ltomic
 *
 */
public class FilledCircle extends Circle {

	/** Filled circle string representation */
	private static final String STRING_FILLED_CIRCLE_FORMAT = "Filled circle (%d, %d), %d, #%s";
	/** Filled circle doc representation */
	private static final String DOC_FILLED_CIRCLE_FORMAT = "FCIRCLE %d %d %d %d %d %d %d %d %d";
	/** Color in hex format */
	private static final String COLOR_HEX_FORMAT = "%02x%02x%02x";

	/** Circle fill color */
	private Color fillColor;

	/** 
	 * Constructs {@link FilledCircle} with properties set to null
	 */
	public FilledCircle() {
	}

	/**
	 * Constructs {@link FilledCircle} with provided properties
	 * @param center - circle center
	 * @param radius - circle radius
	 * @param outlineColor - circle outline color
	 * @param fillColor - circle fill color
	 */
	public FilledCircle(Point center, int radius, Color outlineColor, Color fillColor) {
		super(center, radius, outlineColor);
		this.fillColor = fillColor;
	}

	@Override
	public String toDoc() {
		return String.format(DOC_FILLED_CIRCLE_FORMAT, center.x, center.y, radius,
				outlineColor.getRed(), outlineColor.getGreen(), outlineColor.getBlue(),
				fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue());
	}

	@Override
	public String toString() {
		Color color = this.getFillColor();
		return String.format(STRING_FILLED_CIRCLE_FORMAT, this.getCenter().x, this.getCenter().y,
				this.getRadius(),
				String.format(COLOR_HEX_FORMAT, color.getRed(), color.getGreen(), color.getBlue())
						.toUpperCase());
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new FilledCircleEditor(this);
	}

	/**
	 * Gets the fill color.
	 *
	 * @return the fill color
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
	 * Sets the fill color.
	 *
	 * @param fillColor the new fill color
	 */
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
		notifyListeners();
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

}
