package hr.fer.zemris.java.hw16.jvdraw.tools;

import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw16.jvdraw.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.FilledCircle;

/**
 * Tool used to draw {@link FilledCircle}s.
 * @author ltomic
 *
 */
public class FilledCircleTool extends CircleTool {

	/** Circle fill color provider */
	private IColorProvider fillColorProvider;
	
	/**
	 * Constructs {@link FilledCircleTool} with provided arguments.
	 * @param outlineColorProvider - outline color provider
	 * @param fillColorProvider - fill color provider
	 * @param drawingModel - drawing model
	 */
	public FilledCircleTool(IColorProvider outlineColorProvider,
			IColorProvider fillColorProvider, DrawingModel drawingModel) {
		super(outlineColorProvider, drawingModel);
		this.fillColorProvider = fillColorProvider;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (circle == null) {
			circle = new FilledCircle(e.getPoint(), 0, outlineColorProvider.getCurrentColor(),
					fillColorProvider.getCurrentColor());
			return;
		}

		circle.setRadius((int) circle.getCenter().distance(e.getPoint()));
		drawingModel.add(circle);
		circle = null;
	}
	

}
