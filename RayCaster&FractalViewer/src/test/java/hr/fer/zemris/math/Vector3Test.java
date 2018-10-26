package hr.fer.zemris.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class Vector3Test {

	private static final double PRECISION = 1E-4;
	
	@Test
	public void norm() {
		Vector3 v = new Vector3(0, 0, 0);
		assertEquals(0, v.norm(), PRECISION);
		v = new Vector3(1, 0, 0);
		assertEquals(1, v.norm(), PRECISION);
		v = new Vector3(1, 1, 0);
		assertEquals(Math.sqrt(2), v.norm(), PRECISION);
		v = new Vector3(1, 1, 1);
		assertEquals(Math.sqrt(3), v.norm(), PRECISION);
		v = new Vector3(8, 0, 0);
		assertEquals(8, v.norm(), PRECISION);
		v = new Vector3(10, 10, 10);
		assertEquals(Math.sqrt(300), v.norm(), PRECISION);
		v = new Vector3(20, 20, 20);
		assertEquals(Math.sqrt(1200), v.norm(), PRECISION);
		v = new Vector3(5, 5, 5);
		assertEquals(Math.sqrt(75), v.norm(), PRECISION);
	}
	
	@Test
	public void normalized() {
		Vector3 v = new Vector3(1, 0, 0);
		assertEquals(v, v.normalized());
		v = new Vector3(1, 1, 0);
		assertEquals(v.scale(1 / v.norm()), v.normalized());
		v = new Vector3(1, 1, 1);
		assertEquals(v.scale(1 / v.norm()), v.normalized());
		v = new Vector3(8, 0, 0);
		assertEquals(v.scale(1 / v.norm()), v.normalized());
		v = new Vector3(10, 10, 10);
		assertEquals(v.scale(1 / v.norm()), v.normalized());
		v = new Vector3(20, 20, 20);
		assertEquals(v.scale(1 / v.norm()), v.normalized());
		v = new Vector3(5, 5, 5);
		assertEquals(v.scale(1 / v.norm()), v.normalized());
	}
	
	@Test
	public void add() {
		Vector3 v = new Vector3(0, 0, 0);
		v = v.add(new Vector3(1, 0, 0));
		assertEquals(new Vector3(1, 0, 0), v);
		v = v.add(new Vector3(1, 0, 0));
		assertEquals(new Vector3(2, 0, 0), v);
		v = v.add(new Vector3(1, 1, 1));
		assertEquals(new Vector3(3, 1, 1), v);
		v = v.add(new Vector3(-1, -1, -1));
		assertEquals(new Vector3(2, 0, 0), v);
		v = v.add(new Vector3(-2, 0, 0));
		assertEquals(new Vector3(0, 0, 0), v);
	}
	
	@Test
	public void dot() {
		Vector3 v = new Vector3(1, 0, 0);
		assertEquals(0, v.dot(new Vector3(0, 1, 0)), PRECISION);
		assertEquals(0, v.dot(new Vector3(0, 0, 1)), PRECISION);
		assertEquals(2, v.dot(new Vector3(2, 0, 0)), PRECISION);
		v = new Vector3(1, 1, 1);
		assertEquals(3, v.dot(new Vector3(1, 1, 1)), PRECISION);
		assertEquals(6, v.dot(new Vector3(3, 2, 1)), PRECISION);
		v = new Vector3(5, 10, 2);
		assertEquals(72, v.dot(new Vector3(10, 2, 1)), PRECISION);
		assertEquals(72, (new Vector3(10, 2, 1).dot(v)), PRECISION);
	}
	
	@Test
	public void cross() {
		Vector3 v = new Vector3(1, 0, 0);
		assertEquals(new Vector3(0, 0, 1), v.cross(new Vector3(0, 1, 0)));
		assertEquals(new Vector3(0, -1, 0), v.cross(new Vector3(0, 0, 1)));
		assertEquals(new Vector3(0, 0, 0), v.cross(new Vector3(2, 0, 0)));
		v = new Vector3(1, 1, 1);
		assertEquals(new Vector3(0, 0, 0), v.cross(new Vector3(1, 1, 1)));
		assertEquals(new Vector3(-1, 2, -1), v.cross(new Vector3(3, 2, 1)));
		v = new Vector3(5, 10, 2);
		assertEquals(new Vector3(6, 15, -90), v.cross(new Vector3(10, 2, 1)));
		assertEquals(new Vector3(-6, -15, 90), (new Vector3(10, 2, 1)).cross(v));
	}
	
	@Test
	public void scale() {
		Vector3 v = new Vector3(1, 0, 0);
		assertEquals(new Vector3(2, 0, 0), v.scale(2));
		assertEquals(new Vector3(-2, 0, 0), v.scale(-2));
		assertEquals(new Vector3(0, 0, 0), v.scale(0));
		v = new Vector3(1, 1, 1);
		assertEquals(new Vector3(1, 1, 1), v.scale(1));
		assertEquals(new Vector3(5, 5, 5), v.scale(5));
		v = new Vector3(5, 2, 3);
		assertEquals(new Vector3(10, 4, 6), v.scale(2));
		assertEquals(new Vector3(25, 10, 15), v.scale(5));
	}
	
	@Test
	public void cosAngle() {
		Vector3 v = new Vector3(1, 0, 0);
		assertEquals(0, v.cosAngle(new Vector3(0, 1, 0)), PRECISION);
		assertEquals(0, v.cosAngle(new Vector3(0, 0, 1)), PRECISION);
		assertEquals(1, v.cosAngle(new Vector3(2, 0, 0)), PRECISION);
		v = new Vector3(1, 0, 0);
		assertEquals(0.5774, v.cosAngle(new Vector3(1, 1, 1)), PRECISION);
	}
	
	@Test
	public void toStringTest() {
		assertEquals("(-1.000000, 2.000000, -2.000000)", (new Vector3(-1, 2, -2).toString()));
	}

}
