package hr.fer.zemris.java.hw05.db;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class ConditionalExpressionTest {

	private static ConditionalExpression expr;
	private static StudentRecord record;
	
	@BeforeClass
	public static void init() {
		record = new StudentRecord("0000000000", "test", "name", "5");
		expr = new ConditionalExpression(
				FieldValueGetters.FIRST_NAME,
				"nam*",
				ComparisonOperators.LIKE
				);
	}
	
	@Test
	public void test() {
		assertTrue(expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record),
				expr.getStringLiteral())
				);
	}

}
