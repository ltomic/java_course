package hr.fer.zemris.java.custom.collections;


/**
 * LinkedListIndexedCollection is an implementation of a doubly-linked list.
 * It can contain any <code>Object</code> except <code>null</code>. Duplicate
 * elements are allowed. Methods that index the list iterate through the list
 * from the beginning or the end, depending on what is closer to the specified index.
 * Indexes are 0-indexed.
 * @author ltomic
 * @version 1.0
 */

public class LinkedListIndexedCollection extends Collection {
	
	/**
	 * ListNode represents one node from a doubly-linked list.
	 * Variable <code>left</code> is a reference to the previous node in the list,
	 * <code>right</code> to the next node in the list and <code>value</code> variable
	 * stores the <code>Object</code> 
	 * @author ltomic
	 *
	 */
	private static class ListNode {
		/**
		 * Constructs an ListNode with <code>value</code> stored in the node.
		 * @param value <code>Object></code> to be stored
		 */
		public ListNode(Object value) {
			this.value = value;
		}
		ListNode left;
		ListNode right;
		Object value;
		
		/**
		 * Returns a string representation of this node which is the value it stores.
		 */
		@Override
		public String toString() {
			return value.toString();
		}
	}
	
	private int size;
	private ListNode first;
	private ListNode last;

	/**
	 * Constructs an empty list.
	 */
	public LinkedListIndexedCollection() {
		last = new ListNode(null);
		first = last;
	}
	
	/**
	 * Contructs a list containing the elements from the specified collection, in
	 * order they are ordered in the specified collection
	 * @param other collection whose elements will be placed into this list
	 */
	public LinkedListIndexedCollection(Collection other) {
		if (other == null) throw new NullPointerException();
		
		this.addAll(other);
	}
	
	/**
	 * Return the number of elements in the list
	 * @return number of elements in the list
	 */
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * Appends the specified element to the end of the list.
	 * @param val object to be appended into this list
	 * @throws NullPointerException if val is <code>null</code>
	 */
	@Override
	public void add(Object val) {
		insert(val, size);
	}
	
	/**
	 * Returns <code>ListNode</code> at the specified index in this list.
	 * Complexity is up to half the size of the list. 
	 * @param index index of the node to return
	 * @return <code>ListNode</code> at the specified index
	 * @throws IndexOutOfBoundsException if index is out of range
	 */
	private ListNode getNode(int index) {
		if (!(0 <= index && index <= size)) throw new IndexOutOfBoundsException();
		
		boolean direction = index > size/2;
		ListNode current = direction ? last : first;
		
		for (int i = direction ? size : 0; i != index; i += direction ? -1 : 1) {
			current = direction ? current.left : current.right;
		}
		
		return current;
	}
	
	/**
	 * Returns the element stored in the node at the specified index.
	 * Complexity is up to half the size of the list
	 * @param index index of the element to return
	 * @return element at the specified index
	 * @throws IndexOutOfBoundsException if index out of range
	 */
	public Object get(int index) {
		return getNode(index).value;
	}
	
	/**
	 * Removes all the elements from this list.
	 */
	@Override
	public void clear() {
		first = last;
		size = 0;
	}
	
	/**
	 * Inserts the specified object at the specified index in this list.
	 * Objects whose index are greater or equal to specified index are all moved
	 * one position to the right(their indexes are incremented).
	 * Complexity is up to half the size of the list.
	 * @param value object to be inserted
	 * @param position index in list where new object is inserted
	 * @throws IndexOutOfBoundsException if index is out of range
	 * @throws NullPointerException if <code>Object value</code> is <code>null</code>
	 */
	public void insert(Object value, int position) {
		if (value == null) throw new NullPointerException();
		if (!(0 <= position && position <= size)) throw new IndexOutOfBoundsException();
		
		ListNode newNode = new ListNode(value);
		ListNode current = getNode(position);
		
		newNode.right = current;
		if (position != 0) {
			current.left.right = newNode;
			newNode.left = current.left;
		} else {
			first = newNode;
		}
		current.left = newNode;
		
		size++;		
	}
	
	/**
	 * Returns the index of the first occurence of the specified element in this list.
	 * If specified element is not in this list -1 is returned. Complexity is linear.
	 * @param value object to search for
	 * @return the index of the first occurence of the specified element in this list,
	 * 		   -1 if element is not in this list
	 */
	public int indexOf(Object value) {
		ListNode curr = first;
		
		for (int i = 0; i < size; ++i) {
			if (curr.value.equals(value)) return i;
			curr = curr.right;
		}
		
		return -1;
	}
	
	/**
	 * Returns true if this list contains the specified element.
	 * @param element whose presence in this list is tested
	 * @return true if this list contain the specified element
	 */
	@Override
	public boolean contains(Object value) {
		return indexOf(value) != -1;
	}
	
	/**
	 * Removes the element at the specified position in this list.
	 * Elements after the specified position are all moved to the left(their
	 * index are decremented). Complexity is linear in size.
	 * Note: This function won't be called if argument is an <code>Object</code>
	 * 		 for example : Integer.valueOf(2) as argument
	 * @param index the index of the element to be removed
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	public void remove(int index) {
		if (!(0 <= index && index < size)) throw new IndexOutOfBoundsException();
		
		ListNode current = getNode(index);
		
		if (index != 0) {
			current.left.right = current.right;
		} else {
			first = current.right;
		}
		current.right.left = current.left;
		
		size--;
	}
	
	/**
	 * Removes the first occurence of the specified element in this list.
	 * If the list does not contain the element, it is unchanged.
	 * Elements after the removed object are all moved to the left(their index are
	 * decremented). Complexity is linear in size.
	 * @param value object to be removed
	 * @return true if object is in list and removed, false otherwise
	 */
	@Override
	public boolean remove(Object value) {

		int index = indexOf(value);
		if (index == -1) return false;
		
		remove(index);
		return true;
	}
	
	/**
	 * Returns an array containing all of the elements in this list.
	 */
	@Override
	public Object[] toArray() {
		Object[] returnArray = new Object[size];
		ListNode curr = first;
		for (int i = 0; i < size; ++i) {
			returnArray[i] = curr.value;
			curr = curr.right;
		}
		
		return returnArray;
	}
	
	/**
	 * Performs the action specifed by <code>processor.process</code> for each
	 * element of this list.
	 * @param processor object which specifies action to be performed
	 */
	@Override
	public void forEach(Processor processor) {
		for (ListNode i = first; i != last; i = i.right) {
			processor.process(i);
		}
	}
}
