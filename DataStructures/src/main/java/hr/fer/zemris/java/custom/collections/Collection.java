package hr.fer.zemris.java.custom.collections;

/**
 * A collection represents general collection of objects. This class
 * is more of a template for more specific collection than implementation
 * of a collection. Its shows what methods should every collection have, but
 * most of the methods are not actually implemented.
 * @author ltomic
 *
 */
public class Collection {

	/**
	 * Default construtor for collection.
	 */
	protected Collection() {	
	}
	
	/**
	 * Checks if collection is empty.
	 * @return true if collection has no objects, else false
	 */
	public boolean isEmpty() {
		return this.size() == 0;
	}
	
	/**
	 * Returns the number of currently stored objects in this collection.
	 * Implemeted to always return 0 since this is a template for more
	 * specific collections.
	 * @return number of currently stored objects
	 */
	public int size() {
		return 0;
	}
	
	/**
	 * Adds the given object into this collection. Here this method does nothing.
	 * @param value object to be added
	 */
	public void add(Object value) {
	}
	
	/**
	 * Returns true if the collection contains given <code>value</code>, as determined by
	 * <code>equals</code> method. Here always returns false. <code>Value</code> can be null.
	 * @param value object whose presence in this collection is to be tested
	 * @return true if this collection contains the object, else false
	 */
	public boolean contains(Object value) {
		return false;
	}
	
	/**
	 * Returns true if the collection contains given <code>value</code>, as determined by
	 * <code>equals</code> method and removes one occurrence of it. Here always returns false.
	 * @param value object to be removed
	 * @return true if this collection contains given object, else false
	 */
	public boolean remove(Object value) {
		return false;
	}
	
	/**
	 * Allocates new array with size equals to the size of this collections, fills it
	 * with collection content and returns the array. This method never returns null. 
	 * Unsupported operation.
	 * @return array filled with collection content
	 * @throws UnsupportedOperationException if called
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method calls processor.process(.) for each element of this collection.
	 * The order in which elements will be sent is undefined in this class. 
	 * Implemented here as an empty method.
	 * @param processor object on which process is called
	 */
	public void forEach(Processor processor) {
		
	}
	
	/**
	 * Method adds into the current collection all elements from the given collection.
	 * The other collection remains unchanged. Here implemented as for every object 
	 * <code>obj</code> in the Collection<code>other</code>, a method <code>add</code>
	 * on this collection is called with <code>obj</code> as argument.
	 * @param other
	 */
	public void addAll(final Collection other) {
		/**
		 * Local class to override process method in Processor class.
		 */
		class LocalProcessor extends Processor {

			/**
			 * Method of local class which calls a method <code>add</code> on
			 * this collection with <code>val</code> as argument.
			 * @param val object to be added to this collection
			 */
			@Override
			public void process(Object val) {
				Collection.this.add(val);
			}
			
		}
		
		other.forEach(new LocalProcessor());
	}
	
	/**
	 * Removes all elements from this collection. Implemented here as an empty method.
	 */
	public void clear() {
		
	}
}


