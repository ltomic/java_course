package hr.fer.zemris.java.custom.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

public class ComplexNumberTest {

	@Test
	public void zeroComplexNumber() {
		ComplexNumber number = new ComplexNumber();
		
		assertEquals(0, number.getReal(), ComplexNumber.calcPrecision);
		assertEquals(0, number.getImaginary(), ComplexNumber.calcPrecision);
	}
	
	@Test
	public void fromRealComplexNumber() {
		ComplexNumber number = ComplexNumber.fromReal(2.225);
	
		assertEquals(2.225, number.getReal(), ComplexNumber.calcPrecision);
		assertEquals(0, number.getImaginary(), ComplexNumber.calcPrecision);
	}
	
	@Test
	public void fromImaginaryComplexNumber() {
		ComplexNumber number = ComplexNumber.fromImaginary(2.225);
		
		assertEquals(0, number.getReal(), ComplexNumber.calcPrecision);
		assertEquals(2.225, number.getImaginary(), ComplexNumber.calcPrecision);
	}
	
	@Test
	public void fromMagnitudeAndAngleComplexNumber() {
		double magnitude = 5;
		double angle = Math.PI/3;
		ComplexNumber number = ComplexNumber.fromMagnitudeAndAngle(5, Math.PI/3);
		
		assertEquals(magnitude * Math.cos(angle), number.getReal(), ComplexNumber.calcPrecision);
		assertEquals(magnitude * Math.sin(angle), number.getImaginary(), ComplexNumber.calcPrecision);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void magnitudeNegative() {
		ComplexNumber number = ComplexNumber.fromMagnitudeAndAngle(-1, 0);
	}
	
	@Test
	public void magnitudeAndAngleTest() {
		ComplexNumber number = new ComplexNumber(2.552, 0.75);
		
		assertEquals(2.6599255, number.getMagnitude(), ComplexNumber.calcPrecision);
		assertEquals(0.2858392, number.getAngle(), ComplexNumber.calcPrecision);
	}
	
	@Test
	public void angleGreaterThanPI() {
		ComplexNumber number = new ComplexNumber(2.552, -0.75);
		
		assertEquals(2.6599255, number.getMagnitude(), ComplexNumber.calcPrecision);
		assertEquals(5.9973460, number.getAngle(), ComplexNumber.calcPrecision);
	}
	
	@Test
	public void addComplexNumbers() {
		ComplexNumber number = new ComplexNumber(2.552, -0.75);
		number = number.add(new ComplexNumber(1.335, 0.33));
		
		assertEquals(2.552+1.335, number.getReal(), ComplexNumber.calcPrecision);
		assertEquals(-0.75+0.33, number.getImaginary(), ComplexNumber.calcPrecision);
	}
	
	@Test
	public void subComplexNumbers() {
		ComplexNumber number = new ComplexNumber(2.552, -0.75);
		number = number.sub(new ComplexNumber(1.335, 0.33));
		
		assertEquals(2.552-1.335, number.getReal(), ComplexNumber.calcPrecision);
		assertEquals(-0.75-0.33, number.getImaginary(), ComplexNumber.calcPrecision);
	}
	
	@Test
	public void mulComplexNumbers() {
		ComplexNumber number = new ComplexNumber(2.552, -0.75);
		number = number.mul(new ComplexNumber(1.335, 0.33));
		
		assertTrue(number.equals(3.65442, -0.15909));
	}
	
	@Test
	public void divComplexNumbers() {
		ComplexNumber number = new ComplexNumber(2.552, -0.75);
		number = number.div(new ComplexNumber(1.335, 0.33));
		
		assertTrue(number.equals(1.6706563, -0.9747689));
	}
	
	@Test
	public void powerComplexNumbers() {
		ComplexNumber number = new ComplexNumber(2.552, -0.75).power(4);
		
		assertTrue(number.equals(20.7513436, -45.5547618));
	}
	
	@Test
	public void rootComplexNumbers() {
		ComplexNumber numbers[] = new ComplexNumber(2.552, -0.75).root(4);
		ComplexNumber expected[] = {
			new ComplexNumber(0.0911821, 1.2738185),
			new ComplexNumber(-1.2738185, 0.0911821),
			new ComplexNumber(-0.0911821, 1.2738185),
			new ComplexNumber(1.2738185, 0.0911821)
		};
		Assert.assertArrayEquals(expected, numbers);
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void parseEmptyString() {
		ComplexNumber number = ComplexNumber.parse("");
	}
	
	@Test
	public void parseOne() {
		ComplexNumber number = ComplexNumber.parse("1");
	
		assertEquals(1, number.getReal(), 0);
		assertEquals(0, number.getImaginary(), 0);
	}
	
	@Test
	public void parseOneImaginary() {
		ComplexNumber number = ComplexNumber.parse("i");
		
		assertEquals(0, number.getReal(), 0);
		assertEquals(1, number.getImaginary(), 0);
	}
	
	@Test
	public void parseNegativeOneImaginary() {
		ComplexNumber number = ComplexNumber.parse("-i");
		
		assertEquals(0, number.getReal(), 0);
		assertEquals(-1, number.getImaginary(), 0);
	}
	
	@Test
	public void parseNegativeOne() {
		ComplexNumber number = ComplexNumber.parse("-1");
		
		assertEquals(-1, number.getReal(), 0);
		assertEquals(0, number.getImaginary(), 0);
	}
	
	@Test
	public void parseSimplePositiveAndPositive() {
		ComplexNumber number = ComplexNumber.parse("1+i");
		
		assertEquals(1, number.getReal(), 0);
		assertEquals(1, number.getImaginary(), 0);
	}
	
	@Test
	public void parseSimpleNegativeAndPositive() {
		ComplexNumber number = ComplexNumber.parse("-1+i");
		
		assertEquals(-1, number.getReal(), 0);
		assertEquals(1, number.getImaginary(), 0);
	}
	
	@Test
	public void parseSimplePositiveAndNegative() {
		ComplexNumber number = ComplexNumber.parse("1-i");
		
		assertEquals(1, number.getReal(), 0);
		assertEquals(-1, number.getImaginary(), 0);
	}
	
	@Test
	public void parseSimpleNegativeAndNegative() {
		ComplexNumber number = ComplexNumber.parse("-1-i");
		
		assertEquals(-1, number.getReal(), 0);
		assertEquals(-1, number.getImaginary(), 0);
	}
	
	@Test
	public void parsePositiveReal() {
		ComplexNumber number = ComplexNumber.parse("12345");
		
		assertEquals(12345, number.getReal(), 0);
		assertEquals(0, number.getImaginary(), 0);
	}
	
	@Test
	public void parseNegativeReal() {
		ComplexNumber number = ComplexNumber.parse("-12345.062");
		
		assertEquals(-12345.062, number.getReal(), 0);
		assertEquals(0, number.getImaginary(), 0);
	}
	
	@Test
	public void parsePositiveImaginary() {
		ComplexNumber number = ComplexNumber.parse("12345.062i");
		
		assertEquals(0, number.getReal(), 0);
		assertEquals(12345.062, number.getImaginary(), 0);
	}
	
	@Test
	public void parseNegativeImaginary() {
		ComplexNumber number = ComplexNumber.parse("-12345.062i");
		
		assertEquals(0, number.getReal(), 0);
		assertEquals(-12345.062, number.getImaginary(), 0);
	}
	
	@Test
	public void parseRealAndImaginary() {
		ComplexNumber number = ComplexNumber.parse("-123.44-4.2i");
		
		assertEquals(-123.44, number.getReal(), 0);
		assertEquals(-4.2, number.getImaginary(), 0);
	}
		
	@Test
	public void parseZeroReal() {
		ComplexNumber number = ComplexNumber.parse("0");
		
		assertEquals(0, number.getReal(), 0);
		assertEquals(0, number.getImaginary(), 0);
	}
	
	@Test
	public void parseZeroImaginary() {
		ComplexNumber number = ComplexNumber.parse("0i");
		
		assertEquals(0, number.getReal(), 0);
		assertEquals(0, number.getImaginary(), 0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void duplicateReal() {
		ComplexNumber number = ComplexNumber.parse("42-123");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void duplicateImaginary() {
		ComplexNumber number = ComplexNumber.parse("42i-123i");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void reverseOrderofRealAndImaginary() {
		ComplexNumber number = ComplexNumber.parse("42i+123");
	}
	
}



