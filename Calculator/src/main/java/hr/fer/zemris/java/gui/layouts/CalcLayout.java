package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;

/**
 * Implements a layout that has 5 rows and 7 columns. Component on position (1, 1) occupies 5
 * columns, all other component occupy a single row and column. All components(except the first one)
 * have the same size and it is defined as a maximum of preferred sizes of all components, minimum
 * of maximum sizes of all components or maximum of minimal sizes of all components(depending on the
 * situation). Padding between components can be set during the initialization of the object.
 * @author ltomic
 *
 */
public class CalcLayout implements LayoutManager2 {

	/** Number of rows in the layout **/
	private static final int ROW_NUMBER = 5;
	/** Number of columns in the layout **/
	private static final int COLUMN_NUMBER = 7;

	/** Column width of the button on position (1, 1) **/
	private static final int FIRST_BUTTON_SIZE = 5;

	/** Storage of the components **/
	private Component[][] layout = new Component[ROW_NUMBER][COLUMN_NUMBER];
	/** Padding between components **/
	private int padding;

	/** Cached maximum preferred row height of all components **/
	private int preferredRowHeight = -1;
	/** Cached maximum preferred column width of all components **/
	private int preferredColumnWidth = -1;

	/**
	 * Constructs a {@link CalcLayout} with provided padding.
	 * @param padding - padding to use between components
	 */
	public CalcLayout(int padding) {
		super();
		this.padding = padding;
	}

	/**
	 * Constructs a {@link CalcLayout} with 0 padding between components.
	 */
	public CalcLayout() {
		this(0);
	}

	@Override
	public void addLayoutComponent(String stringPosition, Component component) {
		RCPosition position = parsePosition(stringPosition);
		addLayoutComponent(component, position);
	}

	@Override
	public void addLayoutComponent(Component component, Object constrains) {
		if (constrains instanceof String) {
			addLayoutComponent((String)constrains, component);
		}
		
		if (!(constrains instanceof RCPosition)) throw new CalcLayoutException(
				"position must be a type of RCPosition, but was " + constrains.getClass());
		RCPosition position = (RCPosition) constrains;

		if (invalidPosition(position))
			throw new CalcLayoutException("Invalid position given :" + position);

		Component current = layout[position.row - 1][position.column - 1];
		if (current != null && current != component) {
			throw new CalcLayoutException("Another component already occupies this position");
		}

		layout[position.row - 1][position.column - 1] = component;
	}

	@Override
	public void removeLayoutComponent(Component component) {
		for (int i = 0; i < ROW_NUMBER; ++i) {
			for (int j = 0; j < COLUMN_NUMBER; ++j) {
				if (layout[i][j] == component) {
					layout[i][j] = null;
				}
			}
		}
	}

	@Override
	public void layoutContainer(Container container) {
		Dimension containerSize = container.getSize();
		int cellWidth = (containerSize.width - padding * (COLUMN_NUMBER-1)) / COLUMN_NUMBER;
		int cellHeight = (containerSize.height - padding * (ROW_NUMBER-1)) / ROW_NUMBER;
		Insets insets = container.getInsets();
		
		for (int i = 0; i < ROW_NUMBER; ++i) {
			for (int j = 0; j < COLUMN_NUMBER; ++j) {
				if (layout[i][j] == null) continue;
				int width = (i == 0 && j == 0) ? 
						FIRST_BUTTON_SIZE * cellWidth + padding * (FIRST_BUTTON_SIZE-1) : 
						cellWidth;
				Rectangle bounds = new Rectangle(
						(cellWidth + padding) * j + insets.left,
						(cellHeight + padding) * i + insets.top,
						width,
						cellHeight);
				layout[i][j].setBounds(bounds);
			}
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container container) {
		int minimumRowHeight = 0;
		int minimumColumnWidth = 0;

		for (int i = 0; i < ROW_NUMBER; ++i) {
			for (int j = 0; j < COLUMN_NUMBER; ++j) {
				Component c = layout[i][j];
				if (c == null) continue;
				Dimension d = c.getMinimumSize();
				if (d == null) continue;
				if (i == 0 && j == 0) {
					d.width = (d.width - (FIRST_BUTTON_SIZE - 1) * padding) / FIRST_BUTTON_SIZE;
				}
				minimumRowHeight = Math.max(minimumRowHeight, d.height);
				minimumColumnWidth = Math.max(minimumColumnWidth, d.width);
			}
		}

		Insets insets = container.getInsets();

		return new Dimension(
				COLUMN_NUMBER * minimumColumnWidth + insets.left + insets.right
						+ (COLUMN_NUMBER - 1) * padding,
				ROW_NUMBER * minimumRowHeight + insets.bottom + insets.top
						+ (ROW_NUMBER - 1) * padding);
	}

	@Override
	public Dimension preferredLayoutSize(Container container) {
		preferredRowHeight = 0;
		preferredColumnWidth = 0;

		for (int i = 0; i < ROW_NUMBER; ++i) {
			for (int j = 0; j < COLUMN_NUMBER; ++j) {
				Component c = layout[i][j];
				if (c == null) continue;
				Dimension d = c.getPreferredSize();
				if (d == null) continue;
				if (i == 0 && j == 0) {
					d.width = (d.width - (FIRST_BUTTON_SIZE - 1) * padding) / FIRST_BUTTON_SIZE;
				}

				preferredRowHeight = Math.max(preferredRowHeight, d.height);
				preferredColumnWidth = Math.max(preferredColumnWidth, d.width);
			}
		}

		Insets insets = container.getInsets();

		return new Dimension(
				COLUMN_NUMBER * preferredColumnWidth + insets.left + insets.right
						+ (COLUMN_NUMBER - 1) * padding,
				ROW_NUMBER * preferredRowHeight + insets.bottom + insets.top
						+ (ROW_NUMBER - 1) * padding);
	}

	@Override
	public Dimension maximumLayoutSize(Container container) {
		int maximumRowHeight = 0;
		int maximumColumnWidth = 0;

		for (int i = 0; i < ROW_NUMBER; ++i) {
			for (int j = 0; j < COLUMN_NUMBER; ++j) {
				Component c = layout[i][j];
				if (c == null) continue;
				Dimension d = c.getMaximumSize();
				if (d == null) continue;
				if (i == 0 && j == 0) {
					d.width = (d.width - (FIRST_BUTTON_SIZE - 1) * padding) / FIRST_BUTTON_SIZE;
				}

				maximumRowHeight = Math.min(maximumRowHeight != 0 ? maximumRowHeight : d.height,
						d.height);
				maximumColumnWidth = Math
						.min(maximumColumnWidth != 0 ? maximumColumnWidth : d.width, d.width);
			}
		}

		Insets insets = container.getInsets();

		return new Dimension(
				COLUMN_NUMBER * maximumColumnWidth + insets.left + insets.right
						+ (COLUMN_NUMBER - 1) * padding,
				ROW_NUMBER * maximumRowHeight + insets.bottom + insets.top
						+ (ROW_NUMBER - 1) * padding);
	}

	@Override
	public float getLayoutAlignmentX(Container arg0) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container arg0) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container arg0) {
		preferredColumnWidth = -1;
		preferredRowHeight = -1;
	}

	/**
	 * Parses the provided string to the {@link RCPosition}
	 * Accepted format should be "x_value,y_value".
	 * @param stringPosition - string to be parsed
	 * @return parsed {@link RCPosition}
	 */
	public static RCPosition parsePosition(String stringPosition) {
		String[] splitted = stringPosition.split(",");
		if (splitted.length != 2) {
			throw new IllegalArgumentException(
					"Invalid position description, too many splitted parts : " + stringPosition);
		}
		try {
			return new RCPosition(
					Integer.parseInt(splitted[0].trim()), 
					Integer.parseInt(splitted[1].trim()));
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(
					"Invalid position description, invalid number : " + stringPosition);
		}
	}

	/**
	 * Checks if the provided {@link RCPosition} is invalid.
	 * @param position - {@link RCPosition} to be checked.
	 * @return true if provided position is invalid, else false
	 */
	private boolean invalidPosition(RCPosition position) {
		if (position.row < 1 || position.row > ROW_NUMBER) return true;
		if (position.column < 1 || position.column > COLUMN_NUMBER) return true;

		if (position.row == 1 && 1 < position.column && position.column < COLUMN_NUMBER-1)
			return true;

		return false;
	}

	/**
	 * Represents a position in the {@link CalcLayout}. Position is determined by row index value
	 * and column index value.
	 * @author ltomic
	 *
	 */
	public static class RCPosition {

		/** Row position index **/
		private int row;
		/** Column position index **/
		private int column;

		/** 
		 * Constructs a {@link RCPosition} with provided row and column index values
		 * @param row - row index value
		 * @param column - column index value
		 */
		public RCPosition(int row, int column) {
			super();
			this.row = row;
			this.column = column;
		}

		/**
		 * Returns row index value
		 * @return row index value
		 */
		public int getR() {
			return row;
		}

		/**
		 * Returns column index value
		 * @return column index value
		 */
		public int getC() {
			return column;
		}

		@Override
		public String toString() {
			return String.format("(%d, %d)", row, column);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + column;
			result = prime * result + row;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			RCPosition other = (RCPosition) obj;
			if (column != other.column) return false;
			if (row != other.row) return false;
			return true;
		}
	
	}

}
