package hr.fer.zemris.java.gui.charts;

/**
 * Models a single bar in the bar chart with following properties : integer x value, integer
 * y value.
 * @author ltomic
 *
 */
public class XYValue {
	
	/** x value **/
	private int x;
	/** y value **/
	private int y;

	/**
	 * Constructs a {@link XYValue} with given properties
	 * @param x - x value
	 * @param y - y value
	 */
	public XYValue(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns x value
	 * @return x value
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns y value
	 * @return y value
	 */
	public int getY() {
		return y;
	}

}
