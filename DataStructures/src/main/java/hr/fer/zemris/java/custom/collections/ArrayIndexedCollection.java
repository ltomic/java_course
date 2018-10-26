package hr.fer.zemris.java.custom.collections;

/**
 * The ArrayIndexedCollection implements a array of objects with a bit more
 * sophisticated managment of memory. It can contain any <code>Object</code>
 * and those objects can be accesed using an integer index. Capacity is the size
 * of currently allocated array where elements are stored. When size has to 
 * exceed the current capacity, capacity doubles.
 * Duplicate elements are allowed, storage of null references is not.
 * Indexes are 0-indexed.
 * @author ltomic
 * @version 1.0
 */
public class ArrayIndexedCollection extends Collection {
	private int size;
	private int capacity;
	private Object[] elements;
	
	/**
	 * Constructs an empty collection with the specified initial capacity.
	 * @param initialCapacity initial size of allocated array
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		this(new Collection(), initialCapacity);
	}
	
	/**
	 * Constructs an empty collection with initial capacity equal to 16.
	 */
	public ArrayIndexedCollection() {
		this(new Collection(), 16);
	}
	
	/**
	 * Constructs a collection containing the elements of the specified collection,
	 * in order they are ordered in specified collection.
	 * @param copyCollection collection whose elements will be placed into this collection
	 */
	public ArrayIndexedCollection(Collection copyCollection) {
		this(copyCollection, 1);
	}
	
	/**
	 * Constructs a collection containing the elements of the specified collection,
	 * in order they are ordered in specified collection and with the initial capacity
	 * equal to specified capacity or size of given collection(depending on which is greater)
	 * @param other collection from which the elements will be copied
	 * @param initialCapacity suggested size of allocated array
	 * @throws IllegalArgumentException if initial capacity is less than 1
	 * @throws NullPointerException if null pointer is given as <code>other</code>
	 */
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		if (initialCapacity < 1) throw new IllegalArgumentException("Capacity can't be less than 1");
		if (other == null) throw new NullPointerException();
		
		this.capacity = Math.max(other.size(), initialCapacity);
		this.elements = new Object[this.capacity];
		
		this.addAll(other);
	}
	
	/**
	 * Doubles the size of the allocated array.
	 * Allocates a new array whose size is double than the previous one,
	 * and copies the elements from the previous one.
	 */
	private void increaseCapacity() {
		capacity *= 2;
		Object[] newElements = new Object[capacity];
		
		System.arraycopy(elements, 0, newElements, 0, size);
		
		elements = newElements;
	}
	
	
	/**
	 * Appends the specified element to the end of this collection.
	 * Complexity is constant(amortized time). When increase of capacity
	 * happens, the reallocation is itself up to linear in time.
	 * @param value object to be added
	 * @throws NullPointerException if <code>Object value</code> is <code>null</code>
	 */
	@Override
	public void add(Object value) {
		insert(value, size);
	}
	
	/**
	 * Returns the element at the specified position in collection.
	 * @param index index of the element to return
	 * @return object at the specified index
	 * @throws IndexOutOfBoundsException if index is out of range
	 */
	public Object get(int index) {
		if (!(this.isIndexValid(index))) throw new IndexOutOfBoundsException();
		
		return elements[index];
	}
	
	/**
	 * Remove all of the elements from this collection.
	 */
	@Override
	public void clear() {
		for (int i = 0; i < size; ++i) {
			elements[i] = null;
		}
		size = 0;
	}
	
	/**
	 * Inserts the specified object at the specified index in this collection.
	 * Objects whose index are greater or equal to specified index are all moved
	 * one position to the right(their indexes are incremented).
	 * Complexity is equal to the number of elements after <code>position</code>
	 * @param value object to be inserted
	 * @param position index in the collection where new element is inserted
	 * @throws IndexOutOfBoundException if position is out of range(position < 0 || position > size)
	 * @throws NullPointerException if <code>Object value</code> is <code>null</code>
	 */
	public void insert(Object value, int position) {
		if (!(0 <= position && position <= size)) throw new IndexOutOfBoundsException();
		if (value == null) throw new NullPointerException();
		
		if (size == capacity) {
			this.increaseCapacity();
		}
		
		for (int i = size; i > position; --i) {
			elements[i] = elements[i-1];
		}
		
		elements[position] = value;
		size++;
	}
	
	/**
	 * Returns the index of the first occurence of the specified element in this collection.
	 * If specified element is not in this collection -1 is returned. Complexity is linear.
	 * @param value object to search for
	 * @return the index of the first occurence of the specified element in this collection,
	 * 		   -1 if element is not in this collection
	 */
	public int indexOf(Object value) {
		for (int i = 0; i < size; ++i) {
			if (elements[i].equals(value)) return i;
		}
		return -1;
	}
	
	/**
	 * Returns true if specified index is valid, that is between 0 and current size of collection.
	 * @param index index to be checked
	 * @return true if index is valid, else false
	 */
	public boolean isIndexValid(int index) {
		return 0 <= index && index < size;
	}
	
	/**
	 * Removes the element at the specified position in this collection.
	 * Elements after the specified position are all moved to the left(their
	 * index are decremented). Complexity is linear in size.
	 * Note: This function won't be called if argument is an <code>Object</code>
	 * 		 for example : Integer.valueOf(2) as argument
	 * @param index the index of the element to be removed
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	public void remove(int index) {
		if (!(isIndexValid(index))) throw new IndexOutOfBoundsException();
		
		for (int i = index; i < size-1; ++i) {
			elements[i] = elements[i+1];
		}
		size--;
	}
	
	/**
	 * Removes the first occurence of the specified element in this collection.
	 * If the collection does not contain the element, it is unchanged.
	 * Elements after the removed object are all moved to the left(their index are
	 * decremented). Complexity is linear in size.
	 * @param value object to be removed
	 * @return true if object is in collection and removed, false otherwise
	 */
	@Override
	public boolean remove(Object value) {
		int index = indexOf(value);
		if (index == -1) return false;
		
		remove(index);
		return true;
	}
	
	/**
	 * Returns the number of elements in collection
	 * @return the number of elements in collection
	 */
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * Returns true if this collection contains the specified element.
	 * @param element whose presence in this collection is tested
	 * @return true if this collection contain the specified element
	 */
	@Override
	public boolean contains(Object value) {
		return indexOf(value) != -1;
	}
	
	/**
	 * Returns an array containing all of the elements in this collection.
	 */
	@Override
	public Object[] toArray() {
		Object[] returnArray = new Object[size];
		System.arraycopy(elements, 0, returnArray, 0, size);
		return returnArray;
	}
	
	/**
	 * Performs the action specifed by <code>processor.process</code> for each
	 * element of this collection.
	 * @param processor object which specifies action to be performed
	 */
	@Override
	public void forEach(Processor processor) {
		for (int i = 0; i < size; ++i) {
			processor.process(elements[i]);
		}
	}
}
