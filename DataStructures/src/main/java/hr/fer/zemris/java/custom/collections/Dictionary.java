package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * This class implements a dictionary. A dictionary is a collection
 * which stores mappings of one object to another. The first object
 * is called <code>key</code>(or key value) and the other one 
 * <code>value</code>(or mapped value).Together they form a 
 * <code>DictionaryObject</code>.
 * @author ltomic
 *
 */

public class Dictionary {
	
	/**
	 * Collection in which DictionaryObjects are stored.
	 */
	private ArrayIndexedCollection storage;
	
	{
		this.storage = new ArrayIndexedCollection();
	}
	
	/**
	 * This class implements a DictionaryObjects which is a single
	 * mapping of one object to another.
	 * @author ltomic
	 *
	 */
	private class DictionaryObject {
		/**
		 * Used for storing key value
		 */
		private Object key;
		/**
		 * Used for storing mapped value
		 */
		private Object value;
		
		/**
		 * Constructs a new DictionaryObject with <code>key</code> and <code>value</code>
		 * as key value and mapped value respectively. Key cannot be null.
		 * @param key the key value
		 * @param value the mapped value
		 */
		public DictionaryObject(Object key, Object value) {
			this.key = Objects.requireNonNull(key);
			this.value = value;
		}

		/**
		 * Returns the key value
		 * @return key value
		 */
		public Object getKey() {
			return key;
		}

		/**
		 * Returns the mapped value
		 * @return mapped value
		 */
		public Object getValue() {
			return value;
		}

		/**
		 * Assigns the mapped value to <code>value</code>. <code>value</code> cannot be null.
		 * @param value new mapped value, cannot be null
		 */
		public void setValue(Object value) {
			this.value = Objects.requireNonNull(value);
		}
	}
	
	/**
	 * Checks if this is empty
	 * @return <code>true</code> if this is empty, else <code>false</code> 
	 */
	public boolean isEmpty() {
		return storage.isEmpty();
	}
	
	/**
	 * Returns the number of stored mappings in the dictionary.
	 * @return the number of stored mappings in the dictionary
	 */
	public int size() {
		return storage.size();
	}
	
	/**
	 * Deletes all mapping stored in the dictionary.
	 */
	public void clear() {
		storage.clear();
	}
	
	/**
	 * Stores a new mapping into the dictionary. If key value already maps to
	 * some mapped value <code>value</code> overwrites it.
	 * @param key key value
	 * @param value mapped value
	 */
	public void put(Object key, Object value) {
		if (key == null) throw new NullPointerException();
		DictionaryObject object = getDictionaryObject(key);
		
		if (object == null) {
			storage.add(new DictionaryObject(key, value));
		} else {
			object.setValue(value);
		}
	}
	
	/**
	 * Returns the value mapped to key value. Returns null if no mapping
	 * is stored for key value.
	 * @param key key value
	 * @return the value mapped to key value. Null if no mapping is stored
	 * 		   for key value.
	 */
	public Object get(Object key) {
		DictionaryObject object = getDictionaryObject(key);
		
		return object == null ? null : object.getValue();
	}
	
	/**
	 * Returns the DictionaryObject with <code>key</code> equal to the given argument.
	 * Returns null if no mapping is stored for key value.
	 * @param key key value
	 * @return the DictionaryObject with <code>key</code> equal to the given argument.
	 * 		   null if no mapping is stored for key value.
	 */
	private DictionaryObject getDictionaryObject(Object key) {
		for (int i = 0, size = storage.size(); i < size; ++i) {
			DictionaryObject current = (DictionaryObject)storage.get(i);
			if (current.getKey().equals(key)) return current;
		}
		
		return null;
	}
	

}
