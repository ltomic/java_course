package hr.fer.zemris.java.custom.collections.demo;

import static org.junit.Assert.*;

import org.junit.Test;

import hr.fer.zemris.java.custom.collections.EmptyStackException;

public class StackDemoTest {

	@Test(expected = IllegalArgumentException.class)
	public void emptyExpression() {
		StackDemo.evaluateExpression("");
	}
	
	@Test
	public void simplePositiveInteger() {
		int value = StackDemo.evaluateExpression("2");
		
		assertEquals(2, value);
	}
	
	@Test
	public void simpleNegativeInteger() {
		int value = StackDemo.evaluateExpression("-2");
		
		assertEquals(-2, value);
	}
	
	@Test
	public void simpleSum() {
		int value = StackDemo.evaluateExpression("2 3 +");
		
		assertEquals(5, value);
	}
	
	@Test
	public void simpleMinus() {
		int value = StackDemo.evaluateExpression("2 3 -");
		
		assertEquals(-1, value);
	}
	
	@Test
	public void simpleMul() {
		int value = StackDemo.evaluateExpression("2 3 *");
		
		assertEquals(6, value);
	}
	
	@Test
	public void simpleDiv() {
		int value = StackDemo.evaluateExpression("6 2 /");
		
		assertEquals(3, value);
	}
	
	@Test
	public void testExpression() {
		int value = StackDemo.evaluateExpression("-1 8 2 / +");
		
		assertEquals(3, value);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void InvalidExpression1() {
		StackDemo.evaluateExpression("-1 2");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidExpression2() {
		StackDemo.evaluateExpression("2+2");
	}

}
