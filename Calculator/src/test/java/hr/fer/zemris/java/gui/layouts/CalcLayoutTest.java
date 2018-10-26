package hr.fer.zemris.java.gui.layouts;

import static org.junit.Assert.*;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import hr.fer.zemris.java.gui.layouts.CalcLayout.RCPosition;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class CalcLayoutTest {
	
	private static JFrame frame;
	
	@Before
	public void init() {
		frame = new JFrame();
		frame.setLayout(new CalcLayout());
	}

	@Test(expected = CalcLayoutException.class)
	public void InvalidPositionZeroRow() {
		frame.add(new JLabel("test"), new RCPosition(0, 5));
	}
	
	@Test(expected = CalcLayoutException.class)
	public void InvalidPositionNegativeRow() {
		frame.add(new JLabel("test"), new RCPosition(-1, 5));
	}
	
	@Test(expected = CalcLayoutException.class)
	public void InvalidPositionZeroColumn() {
		frame.add(new JLabel("test"), new RCPosition(1, 0));
	}
	
	@Test(expected = CalcLayoutException.class)
	public void InvalidPositionNegativeColumn() {
		frame.add(new JLabel("test"), new RCPosition(1, -1));
	}
	
	@Test(expected = CalcLayoutException.class)
	public void InvalidPosition12() {
		frame.add(new JLabel("test"), new RCPosition(1, 2));
	}
	
	@Test(expected = CalcLayoutException.class)
	public void InvalidPosition13() {
		frame.add(new JLabel("test"), new RCPosition(1, 3));
	}
	
	@Test(expected = CalcLayoutException.class)
	public void InvalidPosition14() {
		frame.add(new JLabel("test"), new RCPosition(1, 4));
	}
	
	@Test(expected = CalcLayoutException.class)
	public void InvalidPosition15() {
		frame.add(new JLabel("test"), new RCPosition(1, 5));
	}
	
	@Test(expected = CalcLayoutException.class)
	public void InvalidPositionPositionOccupied() {
		frame.add(new JLabel("test"), new RCPosition(1, 1));
		frame.add(new JLabel("test"), new RCPosition(1, 1));
	}
	
	private boolean checkPosition(int row, int column, String position) {
		return CalcLayout.parsePosition(position).equals(new RCPosition(row, column));
	}
	
	@Test
	public void parseRCPositions() {
		assertTrue(checkPosition(1, 0, "1,0"));
		assertTrue(checkPosition(5, 2, "5,2"));
		assertTrue(checkPosition(3, 4, "3,4"));
		assertTrue(checkPosition(100, 0, "100 ,0"));
		assertTrue(checkPosition(0, 0, "0 , 0"));
		assertTrue(checkPosition(0, 0, " 0,0 "));
		assertTrue(checkPosition(0, 0, " 0 , 0 "));
		assertTrue(checkPosition(-1, 0, "-1,0"));
	}

	@Test
	public void ValidPosition11() {
		frame.add(new JLabel("test"), new RCPosition(1, 1));
	}
	
	@Test
	public void testDimensions1() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(10,30));
		JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(20,15));
		p.add(l1, new RCPosition(2,2));
		p.add(l2, new RCPosition(3,3));
		Dimension dim = p.getPreferredSize();
		
		assertEquals(152, dim.width);
		assertEquals(158, dim.height);
	}
	
	@Test
	public void testDimensions2() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108,15));
		JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16,30));
		p.add(l1, new RCPosition(1,1));
		p.add(l2, new RCPosition(3,3));
		Dimension dim = p.getPreferredSize();
		
		assertEquals(152, dim.width);
		assertEquals(158, dim.height);
	}
}
