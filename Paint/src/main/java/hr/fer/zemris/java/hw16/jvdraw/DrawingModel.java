package hr.fer.zemris.java.hw16.jvdraw;

import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObject;

/**
 * Stores {@link GeometricalObject} in a order and tracks property changes of the objects.
 * @author ltomic
 *
 */
public interface DrawingModel {

	/**
	 * Returns number of objects stored
	 * @return number of objects stored
	 */
	public int getSize();
	/**
	 * Gets object on the provided index
	 * @param index - index of the object to be retrieved
	 * @return object on the provided index
	 */
	public GeometricalObject getObject(int index);
	/**
	 * Adds given object to the last place
	 * @param object - object to be added
	 */
	public void add(GeometricalObject object);
	/**
	 * Removes the provided object from the list
	 * @param object - object to be removed
	 */
	void remove(GeometricalObject object);
	/**
	 * Moves the provided object to the position equal to
	 * currentIndex+offset where curretIndex is the current index
	 * of the object in order 
	 * @param object - object whose position should be changed
	 * @param offset - number of places object move, if negative object moves up, else
	 * 					object moves down 
	 */
	void changeOrder(GeometricalObject object, int offset);
	/**
	 * Adds the provided {@link DrawingModelListener} as an observer.
	 * @param l - listener to be added as observer
	 */
	public void addDrawingModelListener(DrawingModelListener l);
	/**
	 * Removes provided {@link DrawingModelListener} as an observer.
	 * @param l - listener to be removed
	 */
	public void removeDrawingModelListener(DrawingModelListener l);
}
