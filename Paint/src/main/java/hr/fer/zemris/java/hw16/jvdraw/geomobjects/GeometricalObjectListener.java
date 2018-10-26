package hr.fer.zemris.java.hw16.jvdraw.geomobjects;

/**
 * Observer for {@link GeometricalObject}
 * @author ltomic
 *
 */
public interface GeometricalObjectListener {

	/**
	 * Called when {@link GeometricalObject} is changed
	 * @param o - {@link GeometricalObject} that changed
	 */
	public void geometricalObjectChanged(GeometricalObject o);
}
