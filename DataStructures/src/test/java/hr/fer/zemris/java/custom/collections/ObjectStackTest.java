package hr.fer.zemris.java.custom.collections;

import org.junit.Assert;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ObjectStackTest {

	@Test
	public void emptyStack() {
		ObjectStack stack = new ObjectStack();
		
		assertEquals(0, stack.size());
		assertEquals(true, stack.isEmpty());
	}
	
	@Test(expected = EmptyStackException.class)
	public void popEmptyStack() {
		ObjectStack stack = new ObjectStack();
		
		stack.pop();
	}
	
	@Test(expected = EmptyStackException.class)
	public void peekEmptyStack() {
		ObjectStack stack = new ObjectStack();
		
		stack.peek();
	}
	
	@Test
	public void insertSingleElement() {
		ObjectStack stack = new ObjectStack();
		stack.push(Integer.valueOf(1));
		
		assertEquals(1, stack.size());
		assertEquals(false, stack.isEmpty());
	}
	
	@Test
	public void peekSingleElement() {
		ObjectStack stack = new ObjectStack();
		stack.push(Integer.valueOf(1));
		Object value = stack.peek();
		
		assertEquals(1, stack.size());
		assertEquals(Integer.valueOf(1), value);
	}
	
	@Test
	public void peekMultipleElements() {
		ObjectStack stack = new ObjectStack();
		stack.push("San Francisco");
		stack.push(Integer.valueOf(1));
		stack.push(true);
		
		assertEquals(3, stack.size());
		assertEquals(true, stack.peek());
	}

	@Test
	public void removeOnlyElement() {
		ObjectStack stack = new ObjectStack();
		stack.push(Integer.valueOf(1));
		Object value = stack.pop();
		
		assertEquals(0, stack.size());
		assertEquals(Integer.valueOf(1), value);
	}
		
	@Test
	public void removeMultipleElements() {
		ObjectStack stack = new ObjectStack();
		stack.push("San Francisco");
		stack.push(Integer.valueOf(1));
		stack.push(true);
		
		Object value = stack.pop();
		assertEquals(2, stack.size());
		assertEquals(true, value);
		
		value = stack.pop();
		assertEquals(1, stack.size());
		assertEquals(Integer.valueOf(1), value);
		
		value = stack.pop();
		assertEquals(0, stack.size());
		assertEquals("San Francisco", value);
	}
	
	@Test
	public void clearStack() {
		ObjectStack stack = new ObjectStack();
		stack.push("San Francisco");
		stack.push(Integer.valueOf(1));
		stack.push(true);
		
		stack.clear();
		
		assertEquals(0, stack.size());
	}

}
