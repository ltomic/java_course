package hr.fer.zemris.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplexRootedPolynomialTest {

	@Test(expected = IllegalArgumentException.class)
	public void noRoots() {
		new ComplexRootedPolynomial();
	}
	
	@Test
	public void oneRoot() {
		ComplexRootedPolynomial poly = new ComplexRootedPolynomial(Complex.ONE);
		
		assertEquals(1, poly.order());
		assertEquals(Complex.ZERO, poly.apply(Complex.ONE));
		assertEquals(Complex.ONE, poly.apply(new Complex(2, 0)));
		assertEquals("(z-(1.000000, 0.000000))", poly.toString());
		assertTrue(poly.toComplexPolynom().equals(new ComplexPolynomial(Complex.ONE_NEG, Complex.ONE)));
	}

	@Test
	public void multipleRootsSimple() {
		ComplexRootedPolynomial poly = new ComplexRootedPolynomial(
				Complex.ONE, Complex.IM, Complex.IM_NEG, Complex.ONE_NEG);
	
		assertEquals(Complex.ZERO, poly.apply(Complex.ONE));
		assertEquals(Complex.ZERO, poly.apply(Complex.IM));
		assertEquals(Complex.ZERO, poly.apply(Complex.IM_NEG));
		assertEquals(Complex.ZERO, poly.apply(Complex.ONE_NEG));
		assertEquals(new Complex(15, 0), poly.apply(new Complex(2, 0)));
		assertEquals(String.format("(z-%s)(z-%s)(z-%s)(z-%s)", 
				Complex.ONE, Complex.IM, Complex.IM_NEG, Complex.ONE_NEG), poly.toString());
		assertTrue(poly.toComplexPolynom().equals(
				new ComplexPolynomial(Complex.ONE_NEG, Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ONE)));
	}
	
	@Test
	public void multipleRoots() {
		Complex[] roots = new Complex[] {
				Complex.ONE, new Complex(1, -1.5), new Complex(2, 0.5), new Complex(5, 2)};
		ComplexRootedPolynomial poly = new ComplexRootedPolynomial(roots);
		
		for (Complex i : roots) {
			assertEquals(Complex.ZERO, poly.apply(i));
		}
		
		Complex[] expectedCoefs = new Complex[] {
				new Complex(18.75, -7),
				new Complex(-38.5, 8.5), 
				new Complex(27.75, -0.5), 
				new Complex(-9, -1), 
				Complex.ONE
		};
		assertTrue(poly.toComplexPolynom().equals(new ComplexPolynomial(expectedCoefs)));
	}
		
	@Test
	public void closestRootForRoots() {
		Complex[] roots = new Complex[] {
				Complex.ONE, new Complex(1, -1.5), new Complex(2, 0.5), new Complex(5, 2)};
		ComplexRootedPolynomial poly = new ComplexRootedPolynomial(roots);
		
		for (int i = 0; i < roots.length; ++i) {
			assertEquals(i, poly.indexOfClosestRootFor(roots[i], 0.0001));
		}
	}
	
	@Test
	public void closestRootTooFar() {
		Complex[] roots = new Complex[] {
				Complex.ONE, new Complex(1, -1.5), new Complex(2, 0.5), new Complex(5, 2)};
		ComplexRootedPolynomial poly = new ComplexRootedPolynomial(roots);
		Complex[] testPoints = new Complex[] {
				new Complex(0, 0), new Complex(1, 1), new Complex(2.5, 0), 
				new Complex(1, -0.9999), new Complex(1.5, 0.26), new Complex(100, 100),
				new Complex(10000, 10000), new Complex(5.4, 2.4)
		};
		
		for (Complex i : testPoints) {
			assertEquals(-1, poly.indexOfClosestRootFor(i, 0.5));
		}
 	}
	
	@Test
	public void closestRoot() {
		Complex[] roots = new Complex[] {
				Complex.ONE, new Complex(1, -1.5), new Complex(2, 0.5), new Complex(5, 2)};
		ComplexRootedPolynomial poly = new ComplexRootedPolynomial(roots);
		Complex[] testPoints = new Complex[] {
				new Complex(0.6, 0), new Complex(1, 0.4), new Complex(1.2, 0.2), new Complex(0.5, 0),
				new Complex(1, -1), new Complex(1, -1.4), new Complex(1.2, -1.4), new Complex(0.8, -1.8),
				new Complex(2, 0), new Complex(2, 0.4), new Complex(1.6, 0.2), new Complex(1.8, 0.8),
				new Complex(5, 1.5), new Complex(5.4, 2), new Complex(4.8, 2.4), new Complex(5.28, 1.72)
		};
		short[] expectedIndexes = new short[] {
				0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3
		};
		
		for (int i = 0; i < testPoints.length; ++i) {
			assertEquals(expectedIndexes[i], poly.indexOfClosestRootFor(testPoints[i], 0.5));
		}
	}
}
