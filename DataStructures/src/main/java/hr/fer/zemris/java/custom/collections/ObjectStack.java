package hr.fer.zemris.java.custom.collections;

/**
 * ObjectStack is the implementation of the stack data structure. It adapts
 * the ArrayIndexedCollection interface to be used as stack.
 * @author ltomic
 * @version 1.0
 */

public class ObjectStack {

	private ArrayIndexedCollection collection;
	
	/**
	 * Constructs an empty stack.
	 */
	public ObjectStack() {
		collection = new ArrayIndexedCollection();
	}
	
	/**
	 * Tests if stack has no elements.
	 * @return true if there are no element in the stack, else false
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * Return the number of elements in the stack.
	 * @return the number of elements in the stack.
	 */
	public int size() {
		return collection.size();
	}
	
	/**
	 * Pushes the specifed element onto the top of this stack.
	 * @param value value to be placed on the top of this stack.
	 * @throws NullPointerException if value is null
	 */
	public void push(Object value) {
		collection.add(value);
	}
	
	/**
	 * Removes the element from the top of the stack and returns that object.
	 * @return the object at the top of the stack
	 * @throws EmptyStackException if stack is empty
	 */
	public Object pop() {
		Object result = peek();
		collection.remove(size()-1);
		
		return result;
	}
	
	/**
	 * Returns the object at the top of the stack.
	 * @return object at the top of the stack
	 * @throws EmptyStackException if stack is empty
	 */
	public Object peek() {
		if (isEmpty()) throw new EmptyStackException();
		
		Object result = collection.get(size()-1);
		
		return result;		
	}
	
	/**
	 * Removes all elements from the stack.
	 */
	public void clear() {
		collection.clear();
	}
}
