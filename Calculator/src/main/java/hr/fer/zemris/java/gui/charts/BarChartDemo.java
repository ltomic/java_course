package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * This program draws a window with bar chart described by a text file. File is provided 
 * using command line arguments. File describes the bar chart following these rules :
 * -> 1st file line is a x-axis description
 * -> 2nd file line  is a y-axis description
 * -> 3rd file line is a sequence of pairs with format x_value, y_value
 * -> 4th file line is a y value from which the y-axis will start
 * -> 5th file line is a y value with which the y-axis will end
 * -> 6th file line is a interval between two values marked on the y-axis
 * 
 * Example :
 * Number of people in the car
 * Frequency
 * 1,8 2,20 3,22 4,10 5,4
 * 0
 * 22
 * 2
 * @author ltomic
 *
 */
public class BarChartDemo extends JFrame {

	/** Serial version **/
	private static final long serialVersionUID = 1L;
	/** Initial window location **/
	private static final Point INIT_LOCATION = new Point(100, 100);
	/** Initial window dimension **/
	private static final Dimension INIT_DIMENSION = new Dimension(500, 500);
	/** Window title **/
	private static final String WINDOW_TITLE = "Chart";

	/** Expected number of non-empty lines in the file describing the chart **/
	private static final int EXPECTED_NUMBER_OF_LINES = 6;

	/** Message printed for invalid number of main method arguments **/
	private static final String INVALID_NUMBER_OF_ARGUMENTS_MESSAGE = "Expected one argument - path to file describing a bar chart, given: ";
	/** Message printed for invalid filepath **/
	private static final String INVALID_FILEPATH_MESSAGE = "Given path to file is not valid, given: ";
	/** Message printed for invalid number of non-empty lines in the file describing the chart **/
	private static final String INVALID_NUMBER_OF_LINES_MESSAGE = "File has to many non-empty lines, expected "
			+ EXPECTED_NUMBER_OF_LINES + " given: ";
	/** Message printed when a string cannot be parsed as integer **/
	private static final String CANNOT_PARSE_INTEGER_MESSAGE = "Could not parse to integer : ";
	/** Message printed when there is a incomplete pair in bar description **/
	private static final String NOT_EVEN_NUMBER_OF_XYVALUES_MESSAGE = "Expected even number of integers in third line";

	/**
	 * Initializes the window and GUI and draws the provided chart and puts the label
	 * with given string as text to the top side of the window
	 * @param filepath - top side of the window label text
	 * @param chart - bar chart to display
	 */
	public BarChartDemo(String filepath, BarChart chart) {
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(WINDOW_TITLE);
		setLocation(INIT_LOCATION);
		setSize(INIT_DIMENSION);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new BarChartComponent(chart), BorderLayout.CENTER);

		JLabel pathLabel = new JLabel(filepath);
		pathLabel.setHorizontalAlignment(JLabel.CENTER);
		getContentPane().add(pathLabel, BorderLayout.PAGE_START);
	}

	/**
	 * Parses the file lines given as list of strings to a {@link BarChart}
	 * @param lines - List of lines from the file describing the chart
	 * @return bar chart that was described in the file
	 */
	private static BarChart parseBarChart(List<String> lines) {
		String[] valuesText = lines.get(2).replace(",", " ").split("\\s+");
		ArrayList<XYValue> values = new ArrayList<>();

		if (valuesText.length % 2 != 0)
			throw new IllegalArgumentException(NOT_EVEN_NUMBER_OF_XYVALUES_MESSAGE);
		for (int i = 0; i < valuesText.length; i += 2) {
			try {
				values.add(new XYValue(Integer.parseInt(valuesText[i]),
						Integer.parseInt(valuesText[i + 1])));
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException(NOT_EVEN_NUMBER_OF_XYVALUES_MESSAGE);
			}
		}

		return new BarChart(values, lines.get(0), lines.get(1), Integer.parseInt(lines.get(3)),
				Integer.parseInt(lines.get(4)), Integer.parseInt(lines.get(5)));
	}

	/**
	 * Method called at the beginning of the program. Receives filepath describing the chart
	 * following the rules described in the class javadoc through command line arguments and
	 * invokes a initialization of the windows drawing that chart.
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println(INVALID_NUMBER_OF_ARGUMENTS_MESSAGE + args.length);
			return;
		}

		List<String> lines;
		try {
			lines = Files.readAllLines(Paths.get(args[0]));
		} catch (IOException ex) {
			System.out.println(INVALID_FILEPATH_MESSAGE + args[0]);
			return;
		}

		lines.removeAll(Arrays.asList(""));
		if (lines.size() != EXPECTED_NUMBER_OF_LINES) {
			System.out.println(INVALID_NUMBER_OF_LINES_MESSAGE + lines.size());
			return;
		}

		BarChart chart;
		try {
			chart = parseBarChart(lines);
		} catch (NumberFormatException ex) {
			System.out.println(CANNOT_PARSE_INTEGER_MESSAGE + ex.getMessage());
			return;
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
			return;
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				BarChartDemo prozor = new BarChartDemo(args[0], chart);
				prozor.setVisible(true);
			}
		});
	}
}
