package hr.fer.zemris.java.hw16.jvdraw.geomobjects;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.List;

/**
 * Paints {@link GeometricalObject} when it visits them on the provided {@link Graphics2D}.
 * @author ltomic
 *
 */
public class GeometricalObjectPainter implements GeometricalObjectVisitor {
	
	/** Graphics on which this painter draws */
	private Graphics2D g2d;
	
	/**
	 * Constructs {@link GeometricalObjectPainter} with {@link Graphics2D} set to null.
	 */
	public GeometricalObjectPainter() {
	}
	
	/**
	 * Contructs {@link GeometricalObjectPainter} with provided arguments.
	 * @param g2d - graphics on which painter should draw
	 */
	public GeometricalObjectPainter(Graphics2D g2d) {
		this.g2d = g2d;
	}

	
	/**
	 * Gets the g2d.
	 *
	 * @return the g2d
	 */
	public Graphics2D getG2d() {
		return g2d;
	}

	/**
	 * Sets the g2d.
	 *
	 * @param g2d the new g2d
	 */
	public void setG2d(Graphics2D g2d) {
		this.g2d = g2d;
	}
	
	@Override
	public void visit(Line line) {
		g2d.setColor(line.getColor());
		g2d.drawLine(
				line.getStartPoint().x, line.getStartPoint().y, 
				line.getEndPoint().x, line.getEndPoint().y
				);
	}

	@Override
	public void visit(Circle circle) {
		g2d.setColor(circle.getOutlineColor());
		int radius = circle.getRadius();
		g2d.drawOval(
				circle.getCenter().x-radius, circle.getCenter().y-radius, 
				radius*2, radius*2
				);
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		g2d.setColor(filledCircle.getFillColor());
		int radius = filledCircle.getRadius();
		Ellipse2D circle = new Ellipse2D.Float(
				filledCircle.getCenter().x-radius, filledCircle.getCenter().y-radius, 
				radius*2, radius*2);
		g2d.fill(circle);
		g2d.setColor(filledCircle.getFillColor());
		visit((Circle)filledCircle);
	}

	@Override
	public void visit(Polygon polygon) {
		List<Point> points = polygon.getPoints();
		int[] xPoints = new int[polygon.getPoints().size()];
		int[] yPoints = new int[polygon.getPoints().size()];
		
		for (int i = 0, sz = points.size(); i < sz; ++i) {
			Point curr = points.get(i);
			xPoints[i] = curr.x;
			yPoints[i] = curr.y;
		}
		
		g2d.setColor(polygon.getFillColor());
		g2d.fillPolygon(xPoints, yPoints, points.size());
		
		g2d.setColor(polygon.getOutlineColor());
		g2d.drawPolygon(xPoints, yPoints, points.size());
	}
	
	

}
