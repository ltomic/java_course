package hr.fer.zemris.java.hw16.jvdraw.editors;

import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import hr.fer.zemris.java.hw16.jvdraw.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.Line;

/**
 * Editor for {@link Line}
 * @author ltomic
 *
 */
public class LineEditor extends GeometricalObjectEditor {

	/** Serial version */
	private static final long serialVersionUID = 1L;

	/** Start point x coordinate label */
	private static final String XSTART_LABEL_TEXT = "Start point x coordinate";
	/** Start point y coordinate label */
	private static final String YSTART_LABEL_TEXT = "Start point y coordinate";
	/** End point x coordinate label */
	private static final String XEND_LABEL_TEXT = "End point x coordinate";
	/** End point y coordinate label */
	private static final String YEND_LABEL_TEXT = "End point y coordinate";

	/** Line color label */
	private static final String COLOR_LABEL_TEXT = "Line color";

	/** Start point x coordinate field */
	private JFormattedTextField startXCoordinateField;
	/** Start point y coordinate field */
	private JFormattedTextField startYCoordinateField;
	/** End point x coordinate field */
	private JFormattedTextField endXCoordinateField;
	/** End point y coordinate field */
	private JFormattedTextField endYCoordinateField;
	/** Color area field */
	private JColorArea colorArea;

	/** Line which is edited */
	private Line line;

	/**
	 * Constructs a {@link LineEditor} wtih provided line to edit
	 * @param line - line to edit
	 */
	public LineEditor(Line line) {
		this.line = line;
		this.setLayout(new GridLayout(5, 1));

		startXCoordinateField = setupIntegerLabel(XSTART_LABEL_TEXT, line.getStartPoint().x);
		startYCoordinateField = setupIntegerLabel(YSTART_LABEL_TEXT, line.getStartPoint().y);
		endXCoordinateField = setupIntegerLabel(XEND_LABEL_TEXT, line.getEndPoint().x);
		endYCoordinateField = setupIntegerLabel(YEND_LABEL_TEXT, line.getEndPoint().y);

		JPanel colorPanel = new JPanel(new GridLayout(1, 2));
		JLabel colorLable = new JLabel(COLOR_LABEL_TEXT);
		colorArea = new JColorArea(line.getColor());

		colorPanel.add(colorLable);
		colorPanel.add(colorArea);
		this.add(colorPanel);
	}

	@Override
	public void checkEditing() {
		checkCoordinate(startXCoordinateField, "start", "x");
		checkCoordinate(startYCoordinateField, "start", "y");
		checkCoordinate(endXCoordinateField, "end", "x");
		checkCoordinate(endYCoordinateField, "end", "y");
	}

	@Override
	public void acceptEditing() {
		line.setStartPoint(new Point((Integer) startXCoordinateField.getValue(),
				(Integer) startYCoordinateField.getValue()));
		line.setEndPoint(new Point((Integer) endXCoordinateField.getValue(),
				(Integer) endYCoordinateField.getValue()));
		line.setColor(colorArea.getCurrentColor());
	}

}
