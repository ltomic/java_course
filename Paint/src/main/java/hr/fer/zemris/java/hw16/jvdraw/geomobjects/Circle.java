package hr.fer.zemris.java.hw16.jvdraw.geomobjects;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw16.jvdraw.editors.CircleEditor;
import hr.fer.zemris.java.hw16.jvdraw.editors.GeometricalObjectEditor;

/**
 * Circle is defined by its radius and center point. Only the outline of the circle
 * is colored.
 * @author ltomic
 *
 */
public class Circle extends GeometricalObject {

	/** String representation of circle */
	private static final String STRING_CIRCLE_FORMAT = "Circle (%d,%d), %d";
	/** Doc representation of circle */
	private static final String DOC_CIRCLE_FORMAT = "CIRCLE %d %d %d %d %d %d";

	/** Circle center */
	protected Point center;
	/** Circle radius */
	protected int radius;
	/** Circle outline color */
	protected Color outlineColor;

	/**
	 * Constructs {@link Circle} with properties set to null
	 */
	public Circle() {
	}

	/**
	 * Constructs {@link Circle} with provided properties.
	 * @param center - circle center 
	 * @param radius - circle radius
	 * @param outlineColor - circle outline color
	 */
	public Circle(Point center, int radius, Color outlineColor) {
		this.center = center;
		this.radius = radius;
		this.outlineColor = outlineColor;
	}

	@Override
	public String toDoc() {
		return String.format(DOC_CIRCLE_FORMAT, center.x, center.y, radius, outlineColor.getRed(),
				outlineColor.getGreen(), outlineColor.getBlue());
	}

	@Override
	public String toString() {
		return String.format(STRING_CIRCLE_FORMAT, this.getCenter().x, this.getCenter().y,
				this.getRadius());
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new CircleEditor(this);
	}
	/**
	 * Gets the center.
	 *
	 * @return the center
	 */
	public Point getCenter() {
		return center;
	}

	/**
	 * Sets the center.
	 *
	 * @param center the new center
	 */
	public void setCenter(Point center) {
		this.center = center;
		notifyListeners();
	}

	/**
	 * Gets the radius.
	 *
	 * @return the radius
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Sets the radius.
	 *
	 * @param radius the new radius
	 */
	public void setRadius(int radius) {
		this.radius = radius;
		notifyListeners();
	}

	/**
	 * Gets the outline color.
	 *
	 * @return the outline color
	 */
	public Color getOutlineColor() {
		return outlineColor;
	}

	/**
	 * Sets the outline color.
	 *
	 * @param outlineColor the new outline color
	 */
	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
		notifyListeners();
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

}
