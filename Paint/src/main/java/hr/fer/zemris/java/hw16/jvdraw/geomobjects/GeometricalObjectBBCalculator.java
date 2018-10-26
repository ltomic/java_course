package hr.fer.zemris.java.hw16.jvdraw.geomobjects;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.function.BinaryOperator;

/**
 * Calculates a minimal bounding box of multiple geometrical objects.
 * @author ltomic
 *
 */
public class GeometricalObjectBBCalculator implements GeometricalObjectVisitor {
	
	/** Upper left bounding box point */
	private Point pointLeftTop = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
	/** Bottom right bounding box point */
	private Point pointRightBottom = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
	
	/** Minimum binary operator */
	private static final BinaryOperator<Integer> MIN_OPERATOR = (a, b) -> Math.min(a, b);
	/** Maximum binary operator */
	private static final BinaryOperator<Integer> MAX_OPERATOR = (a, b) -> Math.max(a, b);

	@Override
	public void visit(Line line) {
		Point startPoint = line.getStartPoint();
		Point endPoint = line.getEndPoint();
		extremePoint(pointLeftTop, startPoint, MIN_OPERATOR);
		extremePoint(pointLeftTop, endPoint, MIN_OPERATOR);
		extremePoint(pointRightBottom, startPoint, MAX_OPERATOR);
		extremePoint(pointRightBottom, endPoint, MAX_OPERATOR);
	}

	@Override
	public void visit(Circle circle) {
		Point center = circle.getCenter();
		int radius = circle.getRadius();
		extremePoint(pointLeftTop, new Point(center.x-radius, center.y-radius), MIN_OPERATOR);
		extremePoint(pointRightBottom, new Point(center.x+radius, center.y+radius), MAX_OPERATOR);
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		this.visit((Circle)filledCircle);
	}
	
	@Override
	public void visit(Polygon polygon) {
		polygon.getPoints().forEach((pt) -> {
			extremePoint(pointLeftTop, pt, MIN_OPERATOR);
			extremePoint(pointRightBottom, pt, MAX_OPERATOR);
		});
	}
	
	/**
	 * Returns {@link Rectangle} describing minimal bounding box for visited {@link GeometricalObject}s.
	 * @return minimal bounding box
	 */
	public Rectangle getBoundingBox() {
		return new Rectangle(
				pointLeftTop.x, pointLeftTop.y, 
				pointRightBottom.x-pointLeftTop.x, 
				pointRightBottom.y-pointLeftTop.y
				);
	}

	/**
	 * Modifies {@link Point} provided as first argument by setting its 
	 * x coordinate to be equal to value calculated when provided {@link BinaryOperator} is
	 * applied to x cooridnates of both {@link Point} provided and its y coordinate
	 * is calculated in the same manner.
	 * @param a - point to be modified
	 * @param b - second point
	 * @param f - operator to apply
	 */
	private void extremePoint(Point a, Point b, BinaryOperator<Integer> f) {
		a.setLocation(f.apply(a.x, b.x), f.apply(a.y, b.y));
	}

}
