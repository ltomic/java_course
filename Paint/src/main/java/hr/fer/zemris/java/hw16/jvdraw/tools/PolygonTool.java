package hr.fer.zemris.java.hw16.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import hr.fer.zemris.java.hw16.jvdraw.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObjectPainter;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.Polygon;

public class PolygonTool implements Tool {

	private static final String POINT_NOT_ACCEPTED_NOT_CONVEX = "Clicked point not accepted because polygon would not be convex";

	private DrawingModel drawingModel;
	private Polygon polygon;
	private IColorProvider fillColorProvider;
	private IColorProvider outlineColorProvider;
	private GeometricalObjectPainter painter = new GeometricalObjectPainter(null);

	private Point temporaryPoint;

	public PolygonTool(IColorProvider outlineColorProvider, IColorProvider fillColorProvider,
			DrawingModel drawingModel) {
		this.outlineColorProvider = outlineColorProvider;
		this.fillColorProvider = fillColorProvider;
		this.drawingModel = drawingModel;
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			polygon = null;
			temporaryPoint = null;
			return;
		}
		
		Point clickedPoint = e.getPoint();
		if (polygon != null) {
			Point lastPoint = polygon.getPoint(polygon.numberOfPoints()-1);
			Point firstPoint = polygon.getPoint(0);
			
			if (clickedPoint.distance(lastPoint) < Polygon.MINIMAL_POINT_DISTANCE) {
				if (polygon.numberOfPoints() < 3) return;
				polygon.setFillColor(fillColorProvider.getCurrentColor());
				polygon.setOutlineColor(outlineColorProvider.getCurrentColor());
				drawingModel.add(polygon);
				polygon = null;
				temporaryPoint = null;
				return;
			}

			if (polygon.isConvexWithNewPoint(clickedPoint)) {
				polygon.addPoint(clickedPoint);
				temporaryPoint = null;
			} else {
				JOptionPane.showMessageDialog(null, POINT_NOT_ACCEPTED_NOT_CONVEX, "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			polygon = new Polygon();
			polygon.addPoint(clickedPoint);
			temporaryPoint = null;
		}
		
		polygon.setOutlineColor(outlineColorProvider.getCurrentColor());
		polygon.setFillColor(fillColorProvider.getCurrentColor());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (polygon == null) return;
		
		temporaryPoint = e.getPoint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void paint(Graphics2D g2d) {
		if (polygon == null) return;
		painter.setG2d(g2d);
		if (temporaryPoint != null) polygon.addPoint(temporaryPoint);
		painter.visit(polygon);
		if (temporaryPoint != null) polygon.removeLastPoint();
	}

}
