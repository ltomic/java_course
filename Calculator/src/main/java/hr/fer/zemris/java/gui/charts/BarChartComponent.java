package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;

/**
 * {@link JComponent} that is a bar chart graph with axis descriptions.
 * @author ltomic
 *
 */
public class BarChartComponent extends JComponent {

	/** Serial version **/
	private static final long serialVersionUID = 1L;

	/** Default font of the component **/
	private static final Font FONT = new Font("Loma", Font.PLAIN, 18);
	/**
	 * Standard padding value, primarily defines the space between x-axis description and bottom
	 * border of the component
	 **/
	private static final int DEFUALT_PAD = 12;
	/** Color of the lines indicating borders of y values and x values **/
	private static final Color SMALL_LINES_COLOR = new Color(244, 215, 66);
	/** Color of the bars **/
	private static final Color BAR_COLOR = new Color(252, 107, 45);
	/** Text color **/
	private static final Color TEXT_COLOR = Color.BLACK;

	/** Bar shadow width **/
	private static final int SHADOW_WIDTH = 5;
	/** How much lower is the shadow from the top of the object **/
	private static final int SHADOW_OFFSET = 10;

	/**
	 * yMax value used instead of the given yMax value if the interval would not end on the given
	 * yMax value. Calculated as the next greater y value that is greater than the given yMax value
	 * and the interval ends on the given yMax value.
	 */
	private int newYMax;
	/** Number of small lines marking x values **/
	private int numOfXLines;
	/** Number of small lines marking y values **/
	private int numOfYLines;
	/** Maximum text width of the numbers noting y values on the axis **/
	private int yAxisNumbersWidth;
	/** Origins y coordinate **/
	private int originYCoordinate;
	/** Origins x coordinate **/
	private int originXCoordinate;
	/** Distance between small lines marking x values **/
	private int distBetweenXLines;
	/** Distance between small lines marking y values **/
	private int distBetweenYLines;

	/** Font metrics of the FONT **/
	private FontMetrics fontMetrics;
	/** Dimensions of this component **/
	private Dimension dim;

	/** Provided chart to draw **/
	private BarChart chart;

	/**
	 * Constructs a {@link BarChartComponent} with provided bar chart
	 * @param chart bar chart this component draws
	 */
	public BarChartComponent(BarChart chart) {
		super();
		this.chart = chart;
	}

	public void paint(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		initValues(g2);

		drawAxisDesc(g2);
		drawCoordinateLines(g2);
		drawBars(g2);
	}

	/**
	 * Initializes all values needed to draw a bar chart
	 * @param g - graphics2D of this component
	 */
	private void initValues(Graphics2D g) {
		g.setFont(FONT);
		fontMetrics = g.getFontMetrics(FONT);
		dim = this.getSize();

		newYMax = (chart.getMaxY() - chart.getMinY()) % chart.getSpace() == 0 ? chart.getMaxY()
				: (((chart.getMaxY() - chart.getMinY()) / chart.getSpace()) + 1) * chart.getSpace() + 1;

		numOfXLines = (newYMax - chart.getMinY()) / chart.getSpace();
		numOfYLines = chart.getValues().size();

		yAxisNumbersWidth = Math.max(fontMetrics.stringWidth(String.valueOf(newYMax)),
				fontMetrics.stringWidth(String.valueOf(chart.getMinY())));

		originYCoordinate = dim.height - (3 * DEFUALT_PAD + 2 * fontMetrics.getAscent());
		originXCoordinate = fontMetrics.getAscent() + 3 * DEFUALT_PAD + yAxisNumbersWidth;

		distBetweenXLines = (originYCoordinate - 2 * DEFUALT_PAD) / numOfXLines;
		distBetweenYLines = (dim.width - 3 * DEFUALT_PAD - originXCoordinate) / numOfYLines;
	}

	/**
	 * Draws the bars of the bar chart
	 * @param g2 - graphics2D of this component
	 */
	private void drawBars(Graphics2D g2) {
		for (int i = 0, sz = chart.getValues().size(); i < sz; ++i) {
			int width = distBetweenYLines;
			int height = (distBetweenXLines * chart.getValues().get(i).getY()) / chart.getSpace();
			int x = originXCoordinate + distBetweenYLines * i;
			int y = originYCoordinate - height;
			g2.setColor(BAR_COLOR);
			g2.fillRect(x + 1, y, width - 1, height);

			g2.setColor(Color.WHITE);
			g2.drawLine(x + width, y, x + width, y + height);

			g2.setColor(Color.GRAY);
			g2.fillRect(x + width + 1, y + SHADOW_OFFSET, SHADOW_WIDTH, height - SHADOW_OFFSET);
		}
	}

	/**
	 * Draws coordinate lines of the bar chart.
	 * @param g2 - graphics2D of this component
	 */
	private void drawCoordinateLines(Graphics2D g2) {
		g2.drawString(String.valueOf(chart.getMinY()),
				DEFUALT_PAD + fontMetrics.getAscent() + DEFUALT_PAD,
				originYCoordinate + fontMetrics.getAscent() / 3);

		g2.setColor(Color.GRAY);
		g2.drawLine(originXCoordinate - DEFUALT_PAD / 2, originYCoordinate,
				dim.width - 2 * DEFUALT_PAD, originYCoordinate);

		for (int i = 1; i < numOfXLines + 1; ++i) {
			int y = originYCoordinate - distBetweenXLines * i;
			g2.setColor(SMALL_LINES_COLOR);
			g2.drawLine(originXCoordinate, y, dim.width - 2 * DEFUALT_PAD, y);
			g2.setColor(TEXT_COLOR);
			g2.drawString(String.valueOf(chart.getMinY() + i * chart.getSpace()),
					DEFUALT_PAD + fontMetrics.getAscent() + DEFUALT_PAD,
					y + fontMetrics.getAscent() / 3);

			g2.setColor(Color.GRAY);
			g2.drawLine(originXCoordinate - DEFUALT_PAD / 2, y, originXCoordinate, y);

		}

		g2.setColor(Color.GRAY);
		g2.drawLine(originXCoordinate, originYCoordinate + DEFUALT_PAD / 2, originXCoordinate,
				DEFUALT_PAD);

		for (int i = 1; i < numOfYLines + 1; ++i) {
			int x = originXCoordinate + distBetweenYLines * i;
			g2.setColor(SMALL_LINES_COLOR);
			g2.drawLine(x, originYCoordinate, x, DEFUALT_PAD);

			g2.setColor(TEXT_COLOR);
			String text = String.valueOf(chart.getValues().get(i - 1).getX());
			g2.drawString(text, x - distBetweenYLines / 2 - fontMetrics.stringWidth(text) / 2,
					originYCoordinate + fontMetrics.getAscent() + DEFUALT_PAD);

			g2.setColor(Color.GRAY);
			g2.drawLine(x, originYCoordinate + DEFUALT_PAD / 2, x, originYCoordinate);
		}

		drawArrowEnds(g2);
	}

	/**
	 * Draws arrow ends of the x-axis and y-axis.
	 * @param g2 - graphics2D of this component
	 */
	private void drawArrowEnds(Graphics2D g2) {
		int[] xPoints = new int[] { originXCoordinate - DEFUALT_PAD / 2, originXCoordinate,
				originXCoordinate + DEFUALT_PAD / 2 };
		int[] yPoints = new int[] {
				originYCoordinate - numOfXLines * distBetweenXLines - DEFUALT_PAD,
				originYCoordinate - numOfXLines * distBetweenXLines - 2 * DEFUALT_PAD,
				originYCoordinate - numOfXLines * distBetweenXLines - DEFUALT_PAD };

		g2.setColor(Color.GRAY);
		g2.fillPolygon(xPoints, yPoints, 3);

		xPoints = new int[] { originXCoordinate + numOfYLines * distBetweenYLines + DEFUALT_PAD,
				originXCoordinate + numOfYLines * distBetweenYLines + 2 * DEFUALT_PAD,
				originXCoordinate + numOfYLines * distBetweenYLines + DEFUALT_PAD };
		yPoints = new int[] { originYCoordinate - DEFUALT_PAD / 2, originYCoordinate,
				originYCoordinate + DEFUALT_PAD / 2 };

		g2.fillPolygon(xPoints, yPoints, 3);
	}

	/**
	 * Draws axis descriptions.
	 * @param g2 - graphics2D of this component
	 */
	private void drawAxisDesc(Graphics2D g2) {
		AffineTransform defaultTransform = g2.getTransform();
		AffineTransform at2 = AffineTransform.getQuadrantRotateInstance(3);

		g2.drawString(chart.getxAxisDesc(), (dim.width - fontMetrics.stringWidth(chart.getxAxisDesc())) / 2,
				(dim.height - DEFUALT_PAD));

		g2.setTransform(at2);

		g2.drawString(chart.getyAxisDesc(), -(dim.height + fontMetrics.stringWidth(chart.getyAxisDesc())) / 2,
				fontMetrics.getAscent() + DEFUALT_PAD / 2);

		g2.setTransform(defaultTransform);
	}
}
