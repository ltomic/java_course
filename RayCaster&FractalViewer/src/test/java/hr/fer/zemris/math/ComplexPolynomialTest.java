package hr.fer.zemris.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplexPolynomialTest {

	@Test(expected = IllegalArgumentException.class)
	public void noFactors() {
		new ComplexPolynomial();
	}
	
	@Test
	public void oneFactor() {
		ComplexPolynomial poly = new ComplexPolynomial(Complex.ONE);
		
		assertEquals(0, poly.order());
		
		assertEquals(Complex.ONE, poly.apply(Complex.ZERO));
		assertEquals(Complex.ONE, poly.apply(Complex.ONE));
		assertEquals(Complex.ONE, poly.apply(Complex.IM));
		
		assertEquals(new ComplexPolynomial(Complex.ZERO), poly.derive());
	
		assertEquals(String.format("(%s)", Complex.ONE), poly.toString());
	}
	
	@Test
	public void multipleFactors() {
		Complex[] factors = new Complex[] {new Complex(5, 2), Complex.ONE_NEG, Complex.IM, Complex.ONE};
		ComplexPolynomial poly = new ComplexPolynomial(factors);
		
		assertEquals(3, poly.order());
		
		assertEquals(new Complex(5, 2), poly.apply(Complex.ZERO));
		assertEquals(new Complex(11, 6), poly.apply(new Complex(2, 0)));
		assertEquals(new Complex(-1, 6), poly.apply(new Complex(-2, 0)));
		assertEquals(new Complex(169, -89), poly.apply(new Complex(-2, 5)));
		assertEquals(new Complex(5.798, 1.35), poly.apply(new Complex(-0.3, 0.5)));
		
	}
	
	@Test
	public void multiplySimple() {
		Complex[] factors1 = new Complex[] {Complex.ONE, Complex.ONE, Complex.IM, Complex.IM_NEG};
		Complex[] factors2 = new Complex[] {Complex.ONE_NEG, Complex.IM, Complex.IM, Complex.IM_NEG};
		
		Complex[] expectedFactors = new Complex[] {
				Complex.ONE_NEG, new Complex(-1, 1), new Complex(0, 1), new Complex(-1, 1),
				new Complex(0, -1), new Complex(2, 0), new Complex(-1, 0)
		};
		ComplexPolynomial poly1 = new ComplexPolynomial(factors1);
		ComplexPolynomial poly2 = new ComplexPolynomial(factors2);
		ComplexPolynomial expectedPoly = new ComplexPolynomial(expectedFactors);

		assertEquals(expectedPoly, poly1.multiply(poly2));
	}
	
	@Test
	public void multiply() {
		Complex[] factors1 = new Complex[] {
				new Complex(0.5, 1), new Complex(2.5, 4), new Complex(1.2, 5), new Complex(1, 1)
		};
		Complex[] factors2 = new Complex[] {
				new Complex(1, 2), new Complex(0.5, 0.5), new Complex(1.3, 5.7), new Complex(1, 1)
		};

		Complex[] expectedFactors = new Complex[] {
				new Complex(-1.5, 2), new Complex(-5.75, 9.75), new Complex(-14.6, 14.8), new Complex(-22.95, 27.05),
				new Complex(-28.44, 20.84), new Complex(-8.2, 13.2), new Complex(0, 2)
		};
		ComplexPolynomial poly1 = new ComplexPolynomial(factors1);
		ComplexPolynomial poly2 = new ComplexPolynomial(factors2);
		ComplexPolynomial expectedPoly = new ComplexPolynomial(expectedFactors);
		
		assertEquals(expectedPoly, poly1.multiply(poly2));
	}

}
