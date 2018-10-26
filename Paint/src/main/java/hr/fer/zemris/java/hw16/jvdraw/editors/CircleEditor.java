package hr.fer.zemris.java.hw16.jvdraw.editors;

import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import hr.fer.zemris.java.hw16.jvdraw.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.Circle;

/**
 * Editor for the {@link Circle}. Displayed when user double clicks on {@link Circle} object
 * in list.
 * @author ltomic
 *
 */
public class CircleEditor extends GeometricalObjectEditor {

	/** Serial version */
	private static final long serialVersionUID = 1L;

	/** Center point X coordinate label */
	protected static final String XCENTER_LABEL_TEXT = "Center x coordinate";
	/** Center point Y coordinate label */
	protected static final String YCENTER_LABEL_TEXT = "Center y coordinate";
	/** Radius label */
	protected static final String RADIUS_LABEL_TEXT = "Radius ";
	/** Outline color label */
	protected static final String OUTLINECOLOR_LABEL_TEXT = "Circle outline color";
	
	/** Invalid radius message */
	protected static final String INVALID_RADIUS_MESSAGE = "Invalid radius given. Expected positive integer. Given %s";

	/** Center point X coordinate field */
	protected JFormattedTextField centerXCoordinateField;
	/** Center point Y coordinate field */
	protected JFormattedTextField centerYCoordinateField;
	/** Radius field */
	protected JFormattedTextField radiusField;
	/** Outline color field */
	protected JColorArea outlineColorArea;
	/** Circle which is edited */
	private Circle circle;

	/**
	 * Initializes {@link CircleEditor} with provided {@link Circle} which should be edited.
	 * @param circle - circle to be edited
	 */
	public CircleEditor(Circle circle) {
		this.circle = circle;
		this.setLayout(new GridLayout(4, 1));

		centerXCoordinateField = setupIntegerLabel(XCENTER_LABEL_TEXT, circle.getCenter().x);
		centerYCoordinateField = setupIntegerLabel(YCENTER_LABEL_TEXT, circle.getCenter().y);
		radiusField = setupIntegerLabel(RADIUS_LABEL_TEXT, circle.getRadius());

		JPanel colorPanel = new JPanel(new GridLayout(1, 2));
		JLabel colorLable = new JLabel(OUTLINECOLOR_LABEL_TEXT);
		outlineColorArea = new JColorArea(circle.getOutlineColor());

		colorPanel.add(colorLable);
		colorPanel.add(outlineColorArea);
		this.add(colorPanel);
	}

	@Override
	public void checkEditing() {
		checkCoordinate(centerXCoordinateField, "center", "x");
		checkCoordinate(centerYCoordinateField, "center", "y");

		if (radiusField.getText().isEmpty()) throw new IllegalArgumentException(
				String.format(INVALID_RADIUS_MESSAGE, radiusField.getText()));
	}

	@Override
	public void acceptEditing() {
		circle.setCenter(new Point((Integer) centerXCoordinateField.getValue(),
				(Integer) centerYCoordinateField.getValue()));
		circle.setRadius((Integer)radiusField.getValue());
		circle.setOutlineColor(outlineColorArea.getCurrentColor());
	}

}
