package hr.fer.zemris.java.hw05.db;

import static org.junit.Assert.*;

import org.junit.Test;


@SuppressWarnings("javadoc")
public class ComparisonOperatorTest {

	@Test
	public void less() {
		IComparisonOperator op = ComparisonOperators.LESS;
		assertTrue(op.satisfied("A", "B"));
		assertTrue(op.satisfied("AA", "B"));
		assertTrue(op.satisfied("AB", "BB"));
		assertTrue(op.satisfied("", "A"));
		assertTrue(op.satisfied("Marin", "Martin"));
		
		assertFalse(op.satisfied("B", "A"));
		assertFalse(op.satisfied("B", "AA"));
		assertFalse(op.satisfied("BB", "AB"));
		assertFalse(op.satisfied("A", ""));
		assertFalse(op.satisfied("Martin", "Marin"));
	}
	
	@Test
	public void lessOrEquals() {
		IComparisonOperator op = ComparisonOperators.LESS_OR_EQUALS;
		assertTrue(op.satisfied("A", "B"));
		assertTrue(op.satisfied("AA", "B"));
		assertTrue(op.satisfied("AB", "BB"));
		assertTrue(op.satisfied("", "A"));
		assertTrue(op.satisfied("Marin", "Martin"));
		
		assertTrue(op.satisfied("A", "A"));
		assertTrue(op.satisfied("a", "a"));
		assertTrue(op.satisfied("", ""));
	}
	
	@Test
	public void greater() {
		IComparisonOperator op = ComparisonOperators.GREATER;
		assertFalse(op.satisfied("A", "B"));
		assertFalse(op.satisfied("AA", "B"));
		assertFalse(op.satisfied("AB", "BB"));
		assertFalse(op.satisfied("", "A"));
		assertFalse(op.satisfied("Marin", "Martin"));
	}
	
	@Test
	public void greaterOrEquals() {
		IComparisonOperator op = ComparisonOperators.GREATER_OR_EQUALS;
		assertFalse(op.satisfied("A", "B"));
		assertFalse(op.satisfied("AA", "B"));
		assertFalse(op.satisfied("AB", "BB"));
		assertFalse(op.satisfied("", "A"));
		assertFalse(op.satisfied("Marin", "Martin"));
		
		assertTrue(op.satisfied("A", "A"));
		assertTrue(op.satisfied("a", "a"));
		assertTrue(op.satisfied("", ""));
	}
	
	@Test
	public void equals() {
		IComparisonOperator op = ComparisonOperators.EQUALS;
		assertTrue(op.satisfied("A", "A"));
		assertTrue(op.satisfied("a", "a"));
		assertTrue(op.satisfied("", ""));
		
		assertFalse(op.satisfied("A", "a"));
		assertFalse(op.satisfied("B", "b"));
		assertFalse(op.satisfied("", "A"));
		assertFalse(op.satisfied("A", ""));
		assertFalse(op.satisfied("Marin", "Martin"));
	}
	
	@Test
	public void notEquals() {
		IComparisonOperator op = ComparisonOperators.NOT_EQUALS;
		assertFalse(op.satisfied("A", "A"));
		assertFalse(op.satisfied("a", "a"));
		assertFalse(op.satisfied("", ""));
	}
	// TODO: dodati jos testova
	
	@Test
	public void likeEmptyPattern() {
		String pattern = "";
		IComparisonOperator op = ComparisonOperators.LIKE;
		
		assertFalse(op.satisfied("bla", pattern));
		assertTrue(op.satisfied("", pattern));
		assertFalse(op.satisfied("A", pattern));
		assertFalse(op.satisfied("Marko", pattern));
	}
	
	@Test
	public void likeWildcardPattern() {
		String pattern = "*";
		IComparisonOperator op = ComparisonOperators.LIKE;
		
		assertTrue(op.satisfied("bla", pattern));
		assertTrue(op.satisfied("", pattern));
		assertTrue(op.satisfied("A", pattern));
		assertTrue(op.satisfied("Marko", pattern));
	}
	
	@Test
	public void likeWildcardEnd() {
		String pattern = "bla*";
		IComparisonOperator op = ComparisonOperators.LIKE;
		
		assertTrue(op.satisfied("bla", pattern));
		assertFalse(op.satisfied("abla", pattern));
		assertFalse(op.satisfied("blta", pattern));
		assertTrue(op.satisfied("blaNinja", pattern));
		assertFalse(op.satisfied("", pattern));
	}
	
	@Test
	public void likeWildcardStart() {
		String pattern = "*bla";
		IComparisonOperator op = ComparisonOperators.LIKE;
		
		assertTrue(op.satisfied("bla", pattern));
		assertFalse(op.satisfied("blaa", pattern));
		assertFalse(op.satisfied("btla", pattern));
		assertTrue(op.satisfied("Ninjabla", pattern));
		assertFalse(op.satisfied("", pattern));
	}
	
	@Test
	public void likeWildcardInMiddle() {
		String pattern = "abc*def";
		IComparisonOperator op = ComparisonOperators.LIKE;
		
		assertTrue(op.satisfied("abcdef", pattern));
		assertTrue(op.satisfied("abcaaadef", pattern));
		assertTrue(op.satisfied("abcddef", pattern));
		
		assertFalse(op.satisfied("ABA", "AB*BA"));
		assertFalse(op.satisfied("abdef", pattern));		
		assertFalse(op.satisfied("aabcdef", pattern));
		assertFalse(op.satisfied("abcdeff", pattern));
	}
	
	@Test
	public void generalTest() {
		IComparisonOperator oper = ComparisonOperators.LESS;
		assertTrue(oper.satisfied("Ana", "Jasna"));		
		
		oper = ComparisonOperators.LIKE;
		assertFalse(oper.satisfied("Zagreb", "Aba*")); // false
		assertFalse(oper.satisfied("AAA", "AA*AA")); // false
		assertTrue(oper.satisfied("AAAA", "AA*AA")); // true
	}

}
