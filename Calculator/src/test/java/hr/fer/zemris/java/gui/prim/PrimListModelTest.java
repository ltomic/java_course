package hr.fer.zemris.java.gui.prim;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import hr.fer.zemris.java.gui.prim.PrimDemo.PrimListModel;

@SuppressWarnings("javadoc")
public class PrimListModelTest {

	private static PrimListModel model;
	private static Integer[] expected;
	
	@Before
	public void init() {
		model = new PrimListModel();
	}
	
	@Test
	public void initalTest() {
		List<Integer> numbers = model.getNumbers();
		expected = new Integer[] {1};
		for (Integer i : numbers.toArray(new Integer[0])) {
			System.out.println(i);
		}
		
		assertArrayEquals(expected, numbers.toArray(new Integer[0]));
	}
	
	@Test
	public void oneNew() {
		model.next();
		List<Integer> numbers = model.getNumbers();
		expected = new Integer[] {1, 2};
		
		assertArrayEquals(expected, numbers.toArray(new Integer[0]));
	}
	
	@Test
	public void tenNew() {
		for (int i = 0; i < 10; ++i) {
			model.next();
		}
		List<Integer> numbers = model.getNumbers();
		expected = new Integer[] {1, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29};
		
		
		assertArrayEquals(expected, numbers.toArray(new Integer[0]));
	}

}
