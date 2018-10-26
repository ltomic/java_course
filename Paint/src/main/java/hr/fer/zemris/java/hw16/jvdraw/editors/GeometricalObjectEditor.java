package hr.fer.zemris.java.hw16.jvdraw.editors;

import java.awt.GridLayout;
import java.awt.Label;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;

import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObject;

/**
 * Editor for {@link GeometricalObject}s.
 * @author ltomic
 *
 */
public abstract class GeometricalObjectEditor extends JPanel {

	/** Serial version */
	private static final long serialVersionUID = 1L;
	
	/** Invalid coordinate message */
	private static final String INVALID_COORDINATE_MESSAGE = "Invalid %s point %s coordinate. Expected positive integer, given \"%s\"";
	/** Coordinate formatter. Allows only non-negative integers to be displayed */
	protected static final NumberFormatter COORDINATE_FORMATTER = new NumberFormatter() {

		private static final long serialVersionUID = 1L;

		{
			NumberFormat format = NumberFormat.getInstance();
			format.setGroupingUsed(false);
			this.setFormat(format);
			this.setValueClass(Integer.class);
			this.setMinimum(0);
			this.setAllowsInvalid(false);
		}

		@Override
		public Object stringToValue(String val) throws ParseException {
			if (val.isEmpty()) return null;
			return super.stringToValue(val);
		}

	};

	/**
	 * Called before acceptEditing method. Checks if all entered values are valid.
	 */
	public abstract void checkEditing();
	/**
	 * Stores the entered values into the {@link GeometricalObject} this editor is editing.
	 */
	public abstract void acceptEditing();
	
	/**
	 * Throws an {@link IllegalArgumentException} when provided {@link JFormattedTextField} contains
	 * invalid coordinate.
	 * @param coordinateField - coordinateField which stores the value
	 * @param point - point name which is checked
	 * @param coordinate - coordinate name which is checked
	 */
	protected static void checkCoordinate(JFormattedTextField coordinateField, String point,
			String coordinate) {
		if (coordinateField.getText().isEmpty()) throw new IllegalArgumentException(
				String.format(INVALID_COORDINATE_MESSAGE, point, coordinate, coordinateField.getText()));
	}
	
	/**
	 * Creates a {@link Label} with provided label and {@link JFormattedTextField} with provided value
	 * that are added to this editor as one {@link JPanel} 
	 * @param label - label to display
	 * @param val - initial value of field
	 * @return created {@link JFormattedTextField}
	 */
	protected JFormattedTextField setupIntegerLabel(String label, int val) {
		JPanel coordPanel = new JPanel(new GridLayout(1, 2));
		JLabel coordLabel = new JLabel(label);
		JFormattedTextField coordField = new JFormattedTextField(COORDINATE_FORMATTER);
		coordField.setValue(Integer.valueOf(val));

		coordPanel.add(coordLabel);
		coordPanel.add(coordField);
		this.add(coordPanel);

		return coordField;
	}
}
