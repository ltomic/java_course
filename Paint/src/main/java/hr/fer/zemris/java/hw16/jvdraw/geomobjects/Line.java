package hr.fer.zemris.java.hw16.jvdraw.geomobjects;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw16.jvdraw.editors.GeometricalObjectEditor;
import hr.fer.zemris.java.hw16.jvdraw.editors.LineEditor;

// TODO: Auto-generated Javadoc
/**
 * Models a Line which is defined by its start point, end point and color.
 * @author ltomic
 *
 */
public class Line extends GeometricalObject {

	/** Line string representation */
	private static final String STRING_LINE_FORMAT = "Line (%d,%d)-(%d, %d)";
	/** Line doc respresntation */
	private static final String DOC_LINE_FORMAT = "LINE %d %d %d %d %d %d %d";

	/** Line start point */
	private Point startPoint;
	/** Line end point */
	private Point endPoint;
	/** Line color */
	private Color color;

	/**
	 * Contructs {@link Line} with properties set to null
	 */
	public Line() {
	}

	/**
	 * Contructs {@link Line} with provided arguments
	 * @param startPoint - line start point
	 * @param endPoint - line end point
	 * @param color - line color
	 */
	public Line(Point startPoint, Point endPoint, Color color) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.color = color;
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new LineEditor(this);
	}

	@Override
	public String toDoc() {
		return String.format(DOC_LINE_FORMAT, startPoint.x, startPoint.y, endPoint.x, endPoint.y,
				color.getRed(), color.getGreen(), color.getBlue());
	}

	@Override
	public String toString() {
		return String.format(STRING_LINE_FORMAT, this.getStartPoint().x, this.getStartPoint().y,
				this.getEndPoint().x, this.getEndPoint().y);
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	
	/**
	 * Gets the start point.
	 *
	 * @return the start point
	 */
	public Point getStartPoint() {
		return startPoint;
	}

	/**
	 * Sets the start point.
	 *
	 * @param startPoint the new start point
	 */
	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
		notifyListeners();
	}

	/**
	 * Gets the end point.
	 *
	 * @return the end point
	 */
	public Point getEndPoint() {
		return endPoint;
	}

	/**
	 * Sets the end point.
	 *
	 * @param endPoint the new end point
	 */
	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
		notifyListeners();
	}

	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the color.
	 *
	 * @param color the new color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

}
