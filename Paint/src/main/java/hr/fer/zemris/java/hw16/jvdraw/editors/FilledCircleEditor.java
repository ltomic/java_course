package hr.fer.zemris.java.hw16.jvdraw.editors;

import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;

import hr.fer.zemris.java.hw16.jvdraw.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.FilledCircle;

/**
 * Editor for {@link FilledCircle}.
 * @author ltomic
 *
 */
public class FilledCircleEditor extends CircleEditor {

	/** Serial version */
	private static final long serialVersionUID = 1L;

	/** Fill color label **/
	private static final String FILLCOLOR_LABEL_TEXT = "Filled circle fill color";

	/** Fill color field */
	private JColorArea fillColorArea;

	/** Filled circle to edit */
	private FilledCircle circle;

	/**
	 * Constructs {@link FilledCircleEditor} with provided {@link FilledCircle} to edit.
	 * @param circle - filled circle to edit
	 */
	public FilledCircleEditor(FilledCircle circle) {
		super(circle);
		this.circle = circle;
		this.setLayout(new GridLayout(5, 1));

		centerXCoordinateField = setupIntegerLabel(XCENTER_LABEL_TEXT, circle.getCenter().x);
		centerYCoordinateField = setupIntegerLabel(YCENTER_LABEL_TEXT, circle.getCenter().y);
		radiusField = setupIntegerLabel(RADIUS_LABEL_TEXT, circle.getRadius());

		JPanel outlineColorPanel = new JPanel(new GridLayout(1, 2));
		JLabel outlineColorLabel = new JLabel(OUTLINECOLOR_LABEL_TEXT);
		outlineColorArea = new JColorArea(circle.getOutlineColor());
		JPanel fillColorPanel = new JPanel(new GridLayout(1, 2));
		JLabel fillColorLabel = new JLabel(FILLCOLOR_LABEL_TEXT);
		fillColorArea = new JColorArea(circle.getFillColor());

		outlineColorPanel.add(outlineColorLabel);
		outlineColorPanel.add(outlineColorArea);
		this.add(outlineColorPanel);
		
		fillColorPanel.add(fillColorLabel);
		fillColorPanel.add(fillColorArea);
		this.add(fillColorPanel);
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
		circle.setFillColor(fillColorArea.getCurrentColor());
	}
}
