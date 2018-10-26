package hr.fer.zemris.java.hw16.jvdraw.geomobjects;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.editors.GeometricalObjectEditor;

/**
 * Models a geometrical object and supports Observer and Visitor pattern. <br>
 * @author ltomic
 *
 */
public abstract class GeometricalObject {

	/** Stores listeners */
	private List<GeometricalObjectListener> listeners = new ArrayList<>();
		
	/**
	 * Method that accepts {@link GeometricalObjectVisitor}.
	 * @param v - visitor to accept
	 */
	public abstract void accept(GeometricalObjectVisitor v);
	
	/**
	 * Creates {@link GeometricalObjectEditor}.
	 * @return {@link GeometricalObjectVisitor}
	 */
	public abstract GeometricalObjectEditor createGeometricalObjectEditor();

	/**
	 * Adds {@link GeometricalObjectListener}.
	 * @param l - {@link GeometricalObjectListener} to add
	 */
	public void addGeometricalObjectListener(GeometricalObjectListener l) {
		listeners.add(l);
	}
	
	/**
	 * Removes provided {@link GeometricalObjectListener}
	 * @param l - {@link GeometricalObjectListener} to remove
	 */
	public void removeGeometricalObjectListener(GeometricalObjectListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Notifies all listeners this {@link GeometricalObject} has changed. 
	 */
	public void notifyListeners() {
		listeners.forEach((listener) -> { listener.geometricalObjectChanged(this); });
	}
	
	/**
	 * Returns a {@link String} that is a JVD document representation of this {@link GeometricalObject}
	 * @return
	 */
	public abstract String toDoc();
}
