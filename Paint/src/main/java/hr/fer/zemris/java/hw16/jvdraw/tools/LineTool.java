package hr.fer.zemris.java.hw16.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw16.jvdraw.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObjectPainter;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.Line;

/**
 * Tool used to draw {@link Line}s.
 * @author ltomic
 *
 */
public class LineTool implements Tool {
	
	/** Line currently drawn */
	private Line line;
	/** Line color provider */
	private IColorProvider colorProvider;
	/** Drawing model in which new lines are stored */
	private DrawingModel drawingModel;
	/** Painter used to paint currently drawn lines */
	private GeometricalObjectPainter painter = new GeometricalObjectPainter(null); 
	
	/**
	 * Constructs {@link LineTool} with provided arguments.
	 * @param colorProvider - line color provider
	 * @param drawingModel - drawing model
	 */
	public LineTool(IColorProvider colorProvider, DrawingModel drawingModel) {
		this.colorProvider = colorProvider;
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
		if (line == null) {
			line = new Line(e.getPoint(), e.getPoint(), colorProvider.getCurrentColor());
			return;
		}
		
		line.setEndPoint(e.getPoint());
		line.setColor(colorProvider.getCurrentColor());
		drawingModel.add(line);
		line = null;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (line == null) return;
		this.line.setEndPoint(e.getPoint());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void paint(Graphics2D g2d) {
		if (line == null) return;
		painter.setG2d(g2d);
		line.accept(painter);
	}

}
