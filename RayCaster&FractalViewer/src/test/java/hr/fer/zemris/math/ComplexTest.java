package hr.fer.zemris.math;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class ComplexTest {

	private static final double PRECISION = 1E-4;
	
	@Test
	public void module() {
		Complex c = new Complex(0, 0);
		assertEquals(0, c.module(), PRECISION);
		c = new Complex(1, 0);
		assertEquals(1, c.module(), PRECISION);
		c = new Complex(0, 1);
		assertEquals(1, c.module(), PRECISION);
		c = new Complex(1, 1);
		assertEquals(Math.sqrt(2), c.module(), PRECISION);
		c = new Complex(2, 2);
		assertEquals(Math.sqrt(8), c.module(), PRECISION);
		c = new Complex(4, 4);
		assertEquals(Math.sqrt(32), c.module(), PRECISION);
		c = new Complex(10, 15);
		assertEquals(Math.sqrt(10*10+15*15), c.module(), PRECISION);
	}
	
	@Test
	public void add() {
		Complex c = new Complex(0, 0);
		assertEquals(new Complex(1, 0), c.add(new Complex(1, 0)));
		assertEquals(new Complex(1, 1), c.add(new Complex(1, 1)));
		assertEquals(new Complex(0, 0), c.add(new Complex(0, 0)));
		c = new Complex(1, 1);
		assertEquals(new Complex(0, 0), c.add(new Complex(-1, -1)));
		c = new Complex(5, 2);
		assertEquals(new Complex(10, 4), c.add(c));
	}
	
	@Test
	public void subComplexNumbers() {
		Complex number = new Complex(2.552, -0.75);
		number = number.sub(new Complex(1.335, 0.33));
		
		assertEquals(new Complex(2.552-1.335, -0.75-0.33), number);
	}
	
	@Test
	public void mulComplexNumbers() {
		Complex number = new Complex(2.552, -0.75);
		number = number.multiply(new Complex(1.335, 0.33));
		
		assertTrue(number.equals(new Complex(3.65442, -0.15909)));
	}
	
	@Test
	public void divComplexNumbers() {
		Complex number = new Complex(2.552, -0.75);
		number = number.divide(new Complex(1.335, 0.33));
		
		assertTrue(number.equals(new Complex(1.6706563, -0.9747689)));
	}
	
	
	@Test
	public void powerComplexNumbers() {
		Complex number = new Complex(2.552, -0.75).power(4);
		
		assertTrue(number.equals(new Complex(20.7513436, -45.5547618)));
	}
	
	@Test
	public void rootComplexNumbers() {
		Complex numbers[] = (new Complex(2.552, -0.75)).root(4).toArray(new Complex[0]);
		Complex expected[] = {
			new Complex(0.0911821, 1.2738185),
			new Complex(-1.2738185, 0.0911821),
			new Complex(-0.0911821, 1.2738185),
			new Complex(1.2738185, 0.0911821)
		};
		Assert.assertArrayEquals(expected, numbers);
	}
	
	@Test
	public void parseOne() {
		assertEquals(Complex.ONE, Complex.parse("1"));
	}
	
	@Test
	public void parseNegOne() {
		assertEquals(Complex.ONE_NEG, Complex.parse("-1"));
	}
	
	@Test
	public void parseIm() {
		assertEquals(Complex.IM, Complex.parse("i"));
	}
	
	@Test
	public void parseNegIm() {
		assertEquals(Complex.IM_NEG, Complex.parse("-i"));
	}
	
	@Test
	public void parseZero() {
		assertEquals(Complex.ZERO, Complex.parse("0"));
		assertEquals(Complex.ZERO, Complex.parse("-0"));
		assertEquals(Complex.ZERO, Complex.parse("i0"));
		assertEquals(Complex.ZERO, Complex.parse("-i0"));
	}
	
	@Test
	public void parseComplex() {
		assertEquals(new Complex(1.12, -3.2), Complex.parse("1.12 - i3.2"));
	}
	
}
