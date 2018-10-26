package hr.fer.zemris.java.hw16.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw16.jvdraw.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObjectPainter;

/**
 * Tool used to draw {@link Circle}s. 
 * @author ltomic
 *
 */
public class CircleTool implements Tool {
	
	/** Circle currently drawn **/
	protected Circle circle;
	/** Drawing model in which new circles are stored */
	protected DrawingModel drawingModel;
	/** Outline color provider */
	protected IColorProvider outlineColorProvider;
	/** Painter used to draw circles */
	private GeometricalObjectPainter painter = new GeometricalObjectPainter();
	
	/**
	 * Construct {@link CircleTool} with provided arguments.
	 * @param colorProvider - circle outline color provider
	 * @param drawingModel - drawing model
	 */
	public CircleTool(IColorProvider colorProvider, DrawingModel drawingModel) {
		this.drawingModel = drawingModel;
		this.outlineColorProvider = colorProvider;
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (circle == null) {
			circle = new Circle(e.getPoint(), 0, outlineColorProvider.getCurrentColor());
			return;
		}
		
		circle.setRadius((int)circle.getCenter().distance(e.getPoint()));
		drawingModel.add(circle);
		circle = null;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (circle == null) return;
		circle.setRadius((int)circle.getCenter().distance(e.getPoint()));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void paint(Graphics2D g2d) {
		if (circle == null) return;
		painter.setG2d(g2d);
		circle.accept(painter);
	}

}
