package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * This class is an implementation of hashtable which maps unique keys of type
 * <code><K></code> to values of type <code><V></code>Average complexity of inserting and
 * retrieving a value by key is O(1). 
 * @author ltomic
 *
 * @param <K> parameter that defines the key class
 * @param <V> parameter that defines the value class
 */

public class SimpleHashTable<K, V> implements Iterable<SimpleHashTable.TableEntry<K, V>>{
	
	/**
	 * <code>table</code> Table stores the mappings.
	 */
	private TableEntry<K, V>[] table;
	/**
	 * Number of mappings stored in hashtable.
	 */
	private int size;
	
	/**
	 * Keeps track of number of modifications(inserting new mapping or removing a mapping)
	 * Iterator uses it to check if table was modified during iteration.
	 */
	private int modificationCount = 0;
	
	/**
	 * Defines the percentage calculated as size/capacity when the hashtable will double in capacity. 
	 */
	private static double OCCUPANCY_LIMIT = 0.75;
	
	/**
	 * Constructs a SimpleHashtable with capacity equal to the first power of two greater than
	 * the given argument capacity.
	 * @param capacity first power of two greater than this argument will be a capacity of the
	 * 			hash table
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashTable(int capacity) {
		super();
		if (capacity < 1) throw new IllegalArgumentException(
				"Capacity of collection must be a positive integer. Given: " + capacity);
		
		int tableCapacity = 1;
		while (tableCapacity < capacity) tableCapacity *= 2;
		
		table = (TableEntry<K, V>[])new TableEntry[tableCapacity];
	}
	
	/**
	 * Default constructor of {@link SimpleHashTable}. Constructs a {@link SimpleHashTable} 
	 * with capacity equal to 16.
	 */
	public SimpleHashTable() {
		this(16);
	}
	
	/**
	 * Doubles the table capacity by allocating new array to store the mappings with size
	 * equal to double the current size. Mappings from the old array are reputed by calling
	 * put on every mapping from the old array.
	 */
	@SuppressWarnings("unchecked")
	public void increaseCapacity() {
		modificationCount++;
		TableEntry<K, V>[] oldTable = table;
		table = new TableEntry[oldTable.length*2];
		size = 0;

		for (TableEntry<K, V> entry : oldTable) {
			while (entry != null) {
				put(entry);
				entry = entry.next;
			}
		}
		
	}
	
	/**
	 * Inserts a value under a unique key.
	 * @param key key under which the value will be mapped
	 * @param value value to map
	 */
	public void put(K key, V value) {
		Objects.requireNonNull(key, "Object key cannot be null");
		
		int index = calcIndexTableSlot(key);
		TableEntry<K, V> entry = table[index];
		
		if (entry == null) {
			table[index] = new TableEntry<K, V>(key, value);
			size++;
			modificationCount++;
			if ((double)size / table.length >= OCCUPANCY_LIMIT) increaseCapacity();
			return;
		}
		
		if (entry.getKey().equals(key)) {
			entry.setValue(value);
			return;
		}
		
		while (entry.next != null && !entry.next.getKey().equals(key)) {
			entry = entry.next;
		}
		
		if (entry.next == null) {
			entry.next = new TableEntry<K, V>(key, value);
			size++;
			modificationCount++;
			if ((double)size / table.length >= OCCUPANCY_LIMIT) increaseCapacity();
		} else {
			entry = entry.next;
			entry.setValue(value);
		}
		
	}
	
	/**
	 * Inserts a value under a unique with mapping given as {@link TableEntry}
	 * @param entry mapping to map
	 */
	public void put(TableEntry<K, V> entry) {
		put(entry.getKey(), entry.getValue());
	}
	
	/**
	 * Retrieves a value stored under the given key.
	 * @param key key under which to search for value
	 * @return a value stored under the given key
	 */
	public V get(Object key) {
		if (key == null) return null;
		
		TableEntry<K, V> entry = table[calcIndexTableSlot(key)];
		
		while (entry != null && entry.getKey().equals(key) == false) {
			entry = entry.next;
		}
		
		return entry == null ? null : entry.getValue();
	}
	
	/**
	 * Returns the number of mappings stored in hashtable.
	 * @return size of the hashtable
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Returns the size of the array in which the mappings are stored.
	 * @return the size of the array in which the mappings are stored
	 */
	public int getCapacity() {
		return table.length;
	}
	
	/**
	 * Check if a value is stored under a given key
	 * @param key key under which is checked if a value stored
	 * @return true if value is stored under a given key else false
	 */
	public boolean containsKey(Object key) {
		if (key == null) return false;
		
		TableEntry<K, V> entry = table[calcIndexTableSlot(key)];
		
		while (entry != null && entry.getKey().equals(key) == false) {
			entry = entry.next;
		}
		
		return entry != null;
	}
	
	/**
	 * Checks if a given value is stored in any mapping in hashtable.
	 * @param value the value to be checked
	 * @return true if value is stored in some mapping in hashtable else false
	 */
	public boolean containsValue(Object value) {
		Iterator<SimpleHashTable.TableEntry<K, V>> iter = iterator();
		while (iter.hasNext()) {
			if (iter.next().equalsValue(value)) return true;
		}
		
		return false;
	}
	
	/**
	 * Removes a mapping under given key.
	 * @param key the key to be removed
	 */
	public void remove(Object key) {
		if (key == null) return;
		
		IteratorImpl iter = (IteratorImpl)iterator(calcIndexTableSlot(key));
		
		while (iter.hasNext()) {
			TableEntry<K, V> entry = iter.next();
			
			if (entry.getKey().equals(key)) {
				iter.remove();
				break;
			}
			if (!iter.hasNextInSlot()) break;
		}
	}
	
	/**
	 * Removes all mappings from the hashtable.
	 */
	public void clear() {
		for (int i = 0; i < table.length; ++i) {
			table[i] = null;
		}
		
		modificationCount++;
		size = 0;
	}
	
	/**
	 * Tests if this hashtable has any mappings.
	 * @return
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	@SuppressWarnings("javadoc")
	@Override
	public String toString() {
		StringBuilder string = new StringBuilder("[");
		for (TableEntry<K, V> entry: table) {
			while (entry != null) {
				string.append(entry.toString() + ", ");
				entry = entry.next;
			}
		}
		
		if (size > 0) {
			string.deleteCharAt(string.length()-1);
			string.deleteCharAt(string.length()-1);
		}
		string.append("]");
		return string.toString();
	}
	
	@SuppressWarnings("javadoc")
	public String debugString() {
		StringBuilder string = new StringBuilder("[");
		
		for (TableEntry<K, V> entry: table) {
			if (entry == null) continue;
			while (entry != null) {
				string.append(entry.toString() + ",");
				entry = entry.next;
			}
			string.append("\n");
		}
		
		string.append("]");
		return string.toString();
	}
	
	/**
	 * Returns the index of a table slot in which the mapping with given key should be stored.
	 * @param key the key to be stored
	 * @return the index of a table slot in which the mapping with given key should be stored
	 */
	private int calcIndexTableSlot(Object key) {
		Objects.requireNonNull(key, "key cannot be null");

		return Math.abs(key.hashCode()) % table.length;
	}
	
	/**
	 * Returns a {@link Iterator} over this hashtable that starts at the beginning of the
	 * hashtable.
	 */
	public Iterator<SimpleHashTable.TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}
	
	/**
	 * Returns a {@link Iterator} over this hashtable that starts at the given index.
	 * @param index index of table slot from which iterator starts iterating.
	 * @return a {@link Iterator} over this hashtable that starts at the given index
	 */
	public Iterator<SimpleHashTable.TableEntry<K, V>> iterator(int index) {
		return new IteratorImpl(index);
	}
	
	/**
	 * This class implements an iterator over this hashtable. It is a forward only iterator
	 * and supports the removal of the object it is currently at.
	 * @author ltomic
	 *
	 */
	private class IteratorImpl implements Iterator<SimpleHashTable.TableEntry<K, V>> {
		/**
		 * Internal hashtable modification counter.
		 */
		private int modificationCountIterator = modificationCount;
		
		/**
		 * Table slot index of a mapping the iterator is at.
		 */
		int currentIndex = -1;
		/**
		 * Table slot index of a next mapping.
		 */
		int nextIndex = -1;
		
		/**
		 * Mapping the iterator was previously at.
		 */
		TableEntry<K, V> prevEntry;
		/**
		 * Mapping the iterator is currently at.
		 */
		TableEntry<K, V> currentEntry; 
		/**
		 * Next mapping.
		 */
		TableEntry<K, V> nextEntry;
		/**
		 * Keeps track if next is called before calling the remove method.
		 */
		boolean newNext = false;
		
		/**
		 * Constructs an iterator over this hashtable that starts at the beginning
		 * of the hashtable.
		 */
		public IteratorImpl() {
			super();
			nextEntry = nextSlot();
		}
		
		/**
		 * Constructs an iterator over this hashtable that starts the table slot with
		 * given index
		 * @param index the table slot index from which the iterator will start
		 */
		public IteratorImpl(int index) {
			super();
			nextIndex = index-1;
			nextEntry = nextSlot();
		}
		
		/**
		 * Returns a reference to the next non-empty table slot.
		 * @return a reference to the next non-empty table slot
		 */
		private TableEntry<K, V> nextSlot() {
			nextIndex++;
			for ( ; nextIndex < table.length; ++nextIndex) {
				if (table[nextIndex] != null) {
					return table[nextIndex];
				}
			}
			
			return null;
		}
		
		/**
		 * Tests if there exist a next mapping
		 * @return true if there exist a next mapping, else false.
		 */
		public boolean hasNext() {
			checkModification();
			return nextEntry != null;
		}
		
		/**
		 * Tests if there exist a next mapping that is in the same slot.
		 * @return if there exist a next mapping that is in the same slot
		 */
		private boolean hasNextInSlot() {
			if (newNext == false) throw new NoSuchElementException();
			
			return currentEntry.next != null;
		}
		
		/**
		 * Returns the next mapping.
		 * @return the next mapping
		 */
		public TableEntry<K, V> next() {
			if (!hasNext()) throw new NoSuchElementException();
			newNext = true;
			
			prevEntry = currentEntry;
			currentEntry = nextEntry;
			
			currentIndex = nextIndex;
			nextEntry = (nextEntry.next == null) ? nextSlot() : nextEntry.next;
			
			return currentEntry;
		}
		
		/**
		 * Removes the mapping the iterator is currently at. Can be called 
		 * only after next() is called.
		 */
		public void remove() {
			checkModification();
			if (newNext == false) throw new IllegalStateException("next method has not yet" +
					"been called, or the remove method has already been called" +
					"after the last call to the next method");
			
			newNext = false;
			modificationCountIterator = ++modificationCount;
			size--;
			
			if (table[currentIndex] == currentEntry) {
				table[currentIndex] = table[currentIndex].next;
				return;
			}
			
			prevEntry.next = null;
			currentEntry = null;
		}
		
		/**
		 * Throws an expection if the table was modified "from the outside",
		 * during iteration.
		 */
		private void checkModification() {
			if (modificationCountIterator != modificationCount)
				throw new ConcurrentModificationException();
		}

	}
	
	/**
	 * This class impelments a mapping. That is stores a key under which a value is stored.
	 * @author ltomic
	 *
	 * @param <K> key class type
	 * @param <V> value class type
	 */
	public static class TableEntry<K, V> {
		
		/**
		 * key of mapping
		 */
		private K key;
		/**
		 * value of mapping
		 */
		private V value;
		/**
		 * Reference to the next mapping in the same table slot.
		 */
		private TableEntry<K, V> next;
		
		/**
		 * Constructs a mapping with <code>key</code> as key and <code>value</code> as value.
		 * @param key
		 * @param value
		 */
		public TableEntry(K key, V value) {
			this.key = Objects.requireNonNull(key, "Object key cannot be null");
			this.value = value;
		}
		
		/**
		 * Returns the mapping value.
		 * @return the mapping value
		 */
		public V getValue() {
			return value;
		}
		
		/**
		 * Sets the mapping value.
		 * @param value value to be set
		 */
		public void setValue(V value) {
			this.value = value;
		}
		
		/**
		 * Returns the mapping key.
		 * @return the mapping key
		 */
		public K getKey() {
			return key;
		}
				
		@SuppressWarnings("javadoc")
		@Override
		public String toString() {
			return key.toString() + "=" + value.toString();
		}
		
		/**
		 * Tests if mapping value is equal to the given <code>value</code> object.
		 * @param value value to be tested
		 * @return true if mapping value and given value are equal
		 */
		public boolean equalsValue(Object value) {
			if (this.value == null) return value == null;
			
			return this.value.equals(value);
		}
		
	}
}
