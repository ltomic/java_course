package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * Models a bar chart with following properties : x-axis description, y-axis description, 
 * value from which the y-axis start, value with which the y-axis ends, interval between y-axis
 * values, list of {@link XYValue} which describes each bar in the chart.
 * @author ltomic
 *
 */
public class BarChart {

	/** List of bars of the chart **/
	private List<XYValue> values;
	/** X-axis description **/
	private String xAxisDesc;
	/** Y-axis description **/
	private String yAxisDesc;
	/** Value from which the y-axis begins **/
	private int minY;
	/** Value with which the y-axis ends **/
	private int maxY;
	/** Interval between y-axis values **/
	private int space;

	/**
	 * Constructs a {@link BarChart} with provided properties
	 * @param values - List of bars of the chart
	 * @param xAxisDesc - X-axis description
	 * @param yAxisDesc - Y-axis description
	 * @param minY - Value from which the y-axis begins
	 * @param maxY - Value with which the y-axis ends
	 * @param space - Interval between y-axis values
	 */
	public BarChart(List<XYValue> values, String xAxisDesc, String yAxisDesc, int minY, int maxY,
			int space) {
		super();
		this.values = values;
		this.xAxisDesc = xAxisDesc;
		this.yAxisDesc = yAxisDesc;
		this.minY = minY;
		this.maxY = maxY;
		this.space = space;
	}

	public List<XYValue> getValues() {
		return values;
	}

	public String getxAxisDesc() {
		return xAxisDesc;
	}

	public String getyAxisDesc() {
		return yAxisDesc;
	}

	public int getMinY() {
		return minY;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getSpace() {
		return space;
	}
	
	
}
