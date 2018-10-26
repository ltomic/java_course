package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObjectPainter;
import hr.fer.zemris.java.hw16.jvdraw.tools.Tool;

/**
 * Component that uses {@link GeometricalObjectPainter} to paint objects from {@link DrawingModel}
 * on itself and enables {@link Tool} to paint on it.
 */
public class JDrawingCanvas extends JComponent implements DrawingModelListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The drawing model. */
	private DrawingModel drawingModel;
	
	/** The current tool. */
	private Tool currentTool;
	
	/** The painter. */
	private GeometricalObjectPainter painter = new GeometricalObjectPainter((Graphics2D)this.getGraphics());
	
	/**
	 * Instantiates a new {@link JDrawingCanvas} with provided arguments.
	 *
	 * @param drawingModel the drawing model from which objects that should be painted are got
	 */
	public JDrawingCanvas(DrawingModel drawingModel) {
		this.drawingModel = drawingModel;
		drawingModel.addDrawingModelListener(this);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw16.jvdraw.DrawingModelListener#objectsAdded(hr.fer.zemris.java.hw16.jvdraw.DrawingModel, int, int)
	 */
	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		repaint();
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw16.jvdraw.DrawingModelListener#objectsRemoved(hr.fer.zemris.java.hw16.jvdraw.DrawingModel, int, int)
	 */
	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		repaint();
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw16.jvdraw.DrawingModelListener#objectsChanged(hr.fer.zemris.java.hw16.jvdraw.DrawingModel, int, int)
	 */
	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		repaint();
	}
	
	/**
	 * Gets the current tool.
	 *
	 * @return the current tool
	 */
	public Tool getCurrentTool() {
		return currentTool;
	}

	/**
	 * Sets the current tool.
	 *
	 * @param currentTool the new current tool
	 */
	public void setCurrentTool(Tool currentTool) {
		this.currentTool = currentTool;
	}

	/**
	 * Paints {@link GeometricalObject}s from {@link DrawingModel} on the component
	 * @param g - graphics
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		painter.setG2d(g2d);
		for (int i = 0, sz = drawingModel.getSize(); i < sz; ++i) {
			drawingModel.getObject(i).accept(painter);
		}
		
		if (currentTool != null) {
			currentTool.paint(g2d);
		}
	}


	
}

