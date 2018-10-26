package hr.fer.zemris.java.hw16.jvdraw;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObject;

/**
 * {@link ListModel} which observes list of objects in {@link DrawingModel}.
 * @author ltomic
 *
 */
public class DrawingObjectListModel extends AbstractListModel<GeometricalObject> {

	/** Serial version */
	private static final long serialVersionUID = 1L;
	
	/** Observed {@link DrawingModel} */
	private DrawingModel drawingModel;
	
	/** {@link DrawingModelListener} which fires content modified methods */
	private final DrawingModelListener drawingModelListener = new DrawingModelListener() {
		
		@Override
		public void objectsRemoved(DrawingModel source, int index0, int index1) {
			DrawingObjectListModel.this.fireIntervalRemoved(this, index0, index1);
		}
		
		@Override
		public void objectsChanged(DrawingModel source, int index0, int index1) {
			DrawingObjectListModel.this.fireContentsChanged(this, index0, index1);			
		}
		
		@Override
		public void objectsAdded(DrawingModel source, int index0, int index1) {
			DrawingObjectListModel.this.fireIntervalRemoved(this, index0, index1);			
		}
	};

	/**
	 * Constructs {@link DrawingObjectListModel} with provided arguments
	 * @param drawingModel - {@link DrawingModel} to observe
	 */
	public DrawingObjectListModel(DrawingModel drawingModel) {
		this.drawingModel = drawingModel;
		drawingModel.addDrawingModelListener(drawingModelListener);
	}

	@Override
	public GeometricalObject getElementAt(int index) {
		return drawingModel.getObject(index);
	}

	@Override
	public int getSize() {
		return drawingModel.getSize();
	}

}
