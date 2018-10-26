package hr.fer.zemris.java.hw16.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObject;

/**
 * Utilizes State pattern to draw and change properties of {@link GeometricalObject}s.
 * When user click on a screen to draw the object a temporary object is drawn to show the
 * user what will be drawn and stored if he clicks on the point mouse is currently at.
 * After the user made the click that determined all the properties of the object 
 * object is stored. This tool tracks mouse actions.
 * @author ltomic
 *
 */
public interface Tool {

	/**
	 * Method called when mouse is pressed.
	 * @param e - event
	 */
	public void mousePressed(MouseEvent e);

	/**
	 * Method called when mouse is released.
	 * @param e
	 */
	public void mouseReleased(MouseEvent e);

	/**
	 * Method called when mous is clicked
	 * @param e
	 */
	public void mouseClicked(MouseEvent e);

	/** 
	 * Method called when mouse is moved.
	 * @param e
	 */
	public void mouseMoved(MouseEvent e);

	/**
	 * Method called when mouse is dragged.
	 * @param e
	 */
	public void mouseDragged(MouseEvent e);

	/**
	 * Method called when a {@link GeometricalObject} is currently drawn.
	 * Draws an {@link GeometricalObject} assuming the objects properties
	 * are determined by the point under which the mouse currently is.
	 * @param g2d
	 */
	public void paint(Graphics2D g2d);
}
