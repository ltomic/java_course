package hr.fer.zemris.java.hw16.jvdraw;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObjectListener;

/**
 * Implementation of the {@link DrawingModel} which stores objects in {@link ArrayList}.
 * @author ltomic
 *
 */
public class DrawingModelImpl implements DrawingModel, GeometricalObjectListener {

	/** Format of message emitted when provided index is out of list bounds */
	private static final String INDEX_OUT_OF_BOUNDS_MESSAGE_FORMAT = "Expected number between 0 and %d. Given %d.";

	/** List where {@link GeometricalObject}s are stored*/
	private List<GeometricalObject> objects = new ArrayList<>();
	/** List where {@link DrawingModelListener}s are stored */
	private List<DrawingModelListener> listeners = new ArrayList<>();

	@Override
	public int getSize() {
		return objects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		if (index < 0 || index >= objects.size()) throw new IndexOutOfBoundsException(
				String.format(INDEX_OUT_OF_BOUNDS_MESSAGE_FORMAT, objects.size(), index));
		
		return objects.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		objects.add(object);
		object.addGeometricalObjectListener(this);
		listeners.forEach((listener) -> { listener.objectsAdded(this, listeners.size()-1, listeners.size()-1); });
	}
	
	@Override
	public void remove(GeometricalObject object) {
		int index = objects.indexOf(object);
		if (index == -1) return;
		objects.remove(object);
		object.removeGeometricalObjectListener(this);
		listeners.forEach((listener) -> { listener.objectsRemoved(this, index, index); });
	}

	@Override
	public void changeOrder(GeometricalObject object, int offset) {
		int index = objects.indexOf(object);
		if (index == -1) return;
		int newIndex = index+offset;
		if (newIndex < 0) newIndex = 0;
		if (newIndex >= objects.size()) newIndex = objects.size()-1;

		int position = newIndex;
		objects.remove(object);
		objects.add(newIndex, object);
		listeners.forEach((listener) -> { listener.objectsAdded(this, Math.min(index, position), Math.max(index,  position)); });
	}

	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void geometricalObjectChanged(GeometricalObject o) {
		int index = objects.indexOf(o);
		listeners.forEach((listener) -> { listener.objectsChanged(this, index, index); });
	}
	

}
