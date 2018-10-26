package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import hr.fer.zemris.java.hw16.jvdraw.geomobjects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObjectBBCalculator;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObjectPainter;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.Line;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.Polygon;

/**
 * Class providing utility methods
 * @author ltomic
 *
 */
public class Util {

	/**
	 * Generates a {@link BufferedImage} from the provided {@link DrawingModel}
	 * @param drawingModel - {@link DrawingModel} from which {@link GeometricalObject}s are
	 *            retrieved
	 * @return {@link BufferedImage} of {@link GeometricalObject}s from provided
	 *         {@link DrawingModel}
	 */
	public static BufferedImage generateImage(DrawingModel drawingModel) {
		GeometricalObjectBBCalculator bbcalc = new GeometricalObjectBBCalculator();
		for (int i = 0, sz = drawingModel.getSize(); i < sz; ++i) {
			drawingModel.getObject(i).accept(bbcalc);
		}

		Rectangle box = bbcalc.getBoundingBox();
		BufferedImage image = new BufferedImage(box.width, box.height,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = image.createGraphics();

		AffineTransform tf = new AffineTransform();
		tf.translate(-box.x, -box.y);
		g.transform(tf);
		GeometricalObjectPainter painter = new GeometricalObjectPainter(g);

		for (int i = 0, sz = drawingModel.getSize(); i < sz; ++i) {
			drawingModel.getObject(i).accept(painter);
		}

		g.dispose();

		return image;
	}

	/**
	 * Prompts user to choose a file in which image should be stored. User should add one of the
	 * following extensions to the file "png", "jpg", "gif".
	 * @param component - frame in which dialogs should be displayed
	 * @return {@link Path} of the chosen file
	 */
	public static Path chooseImageFile(Component component) {
		while (true) {
			Path imagePath = selectFile(component, "Export path");
			if (imagePath == null) return null;

			int ind = imagePath.toString().lastIndexOf(".");
			if (ind == -1 || ind == imagePath.toString().length() - 1) {
				JOptionPane.showMessageDialog(component,
						"Please choose one of the formats jpg, pnf or gif and include it in file extension",
						"Info", JOptionPane.INFORMATION_MESSAGE);
				continue;
			}

			return imagePath;
		}
	}

	/**
	 * Parses an array of strings into a {@link Line}. Array of strings concatenated should have the
	 * following format <br>
	 * LINE startX startY endX endY red green blue
	 * @param splitted - array of string
	 * @return the parsed {@link Line}
	 */
	public static Line parseLine(String[] splitted) {
		if (splitted.length != 8) throw new IllegalArgumentException();
		return new Line(new Point(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2])),
				new Point(Integer.parseInt(splitted[3]), Integer.parseInt(splitted[4])),
				new Color(Integer.parseInt(splitted[5]), Integer.parseInt(splitted[6]),
						Integer.parseInt(splitted[7])));
	}

	/**
	 * Parses an array of strings into a {@link Circle}. Array of string concatenated should have
	 * the following format <br>
	 * CIRCLE centerX centerY radius red green blue
	 * @param splitted
	 * @return
	 */
	public static Circle parseCircle(String[] splitted) {
		if (splitted.length != 7) throw new IllegalArgumentException();
		return new Circle(new Point(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2])),
				Integer.parseInt(splitted[3]), new Color(Integer.parseInt(splitted[4]),
						Integer.parseInt(splitted[5]), Integer.parseInt(splitted[6])));
	}

	/**
	 * Parses an array of strings into a {@link FilledCircle}. Array of string concatenated should
	 * have the following format. <br>
	 * FCIRCLE centerX centerY radius outlineRed outlineGreen outlineBlue fillRed fillBlue fillGreen
	 * @param splitted
	 * @return
	 */
	public static FilledCircle parseFilledCircle(String[] splitted) {
		if (splitted.length != 10) throw new IllegalArgumentException();
		return new FilledCircle(
				new Point(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2])),
				Integer.parseInt(splitted[3]),
				new Color(Integer.parseInt(splitted[4]), Integer.parseInt(splitted[5]),
						Integer.parseInt(splitted[6])),
				new Color(Integer.parseInt(splitted[7]), Integer.parseInt(splitted[8]),
						Integer.parseInt(splitted[9])));
	}

	/** Overwrite file message **/
	private static final String OVERWRITE_FILE_MESSAGE = "File already exists would you like to overwrite?";

	/**
	 * Propmts user to select a file to save data to.
	 * @param component - frame in which dialogs will be displayed
	 * @param message - message to display
	 * @return {@link Path} of the chosen file
	 */
	public static Path selectFile(Component component, String message) {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle(message);

		File selected;
		while (true) {
			if (fc.showSaveDialog(component) != JFileChooser.APPROVE_OPTION) {
				return null;
			}

			selected = fc.getSelectedFile();
			if (selected.exists()) {
				if (JOptionPane.showConfirmDialog(component, OVERWRITE_FILE_MESSAGE, "Overwrite",
						JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
					continue;
				}
			}
			break;
		}
		return selected.toPath();
	}

	/**
	 * Parses a line from JVD document into a {@link GeometricalObject}
	 * @param component - frame in which error dialog can be displayed
	 * @param line - line from JVD document
	 * @return parsed {@link GeometricalObject}
	 */
	public static GeometricalObject parseStringLine(Component component, String line) {
		String[] splitted = line.split("\\s+");
		switch (splitted[0]) {
		case "LINE":
			return Util.parseLine(splitted);
		case "CIRCLE":
			return Util.parseCircle(splitted);
		case "FCIRCLE":
			return Util.parseFilledCircle(splitted);
		case "FPOLY":
			return Util.parsePolygon(splitted);
		default:
			throw new IllegalArgumentException(
					"Line does not start with the name of a geometrical object");
		}
	}

	private static GeometricalObject parsePolygon(String[] splitted) {
		Polygon polygon = new Polygon();

		int n = 0;
		try {
			n = Integer.parseInt(splitted[1]);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(ex);
		}

		for (int i = 1; i <= n; ++i) {
			int x = 0;
			int y = 0;

			try {
				x = Integer.parseInt(splitted[i * 2]);
				y = Integer.parseInt(splitted[i * 2 + 1]);
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException(ex);
			}
			polygon.addPoint(new Point(x, y));
		}

		n++;
		try {
			polygon.setOutlineColor(new Color(Integer.parseInt(splitted[n * 2]),
					Integer.parseInt(splitted[n * 2 + 1]), Integer.parseInt(splitted[n * 2 + 2])));
			polygon.setFillColor(new Color(Integer.parseInt(splitted[n * 2 + 3]),
					Integer.parseInt(splitted[n * 2 + 4]), Integer.parseInt(splitted[n * 2 + 5])));
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(ex);
		}

		return polygon;
	}

	/**
	 * Displays error dialog with provided message
	 * @param component - frame in with error dialog will be displayed
	 * @param errorMessage - message to display
	 */
	public static void errorDialog(Component component, String errorMessage) {
		JOptionPane.showMessageDialog(component, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
	}

}
