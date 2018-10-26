package hr.fer.zemris.java.hw16.jvdraw;

/**
 * Observer of the {@link DrawingModel}. Tracks when objects are added, removed or changed
 * @author ltomic
 *
 */
public interface DrawingModelListener {

	/**
	 * Should be called when objects are added
	 * @param source - {@link DrawingModel} in which objects are added
	 * @param index0 - start index of the interval where objects are added(inclusive)
	 * @param index1 - end index of the interval where objects are added(inclusive)
	 */
	public void objectsAdded(DrawingModel source, int index0, int index1);

	/**
	 * Should be called when objects are removed
	 * @param source - {@link DrawingModel} in which objects are removed
	 * @param index0 - start index of the interval where objects are removed(inclusive)
	 * @param index1 - end index of the interval where object are removed(inclusive)
	 */
	public void objectsRemoved(DrawingModel source, int index0, int index1);

	/**
	 * Should be called when objects changed
	 * @param source - {@link DrawingModel} in which objects changed
	 * @param index0 - start index of the interval where objects changed(inclusive)
	 * @param index1 - end index of the interval where objects changed(inclusive)
	 */
	public void objectsChanged(DrawingModel source, int index0, int index1);
}
