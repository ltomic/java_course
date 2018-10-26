package hr.fer.zemris.java.custom.scripting.exec;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class is like a {@link Map}, except instead of overwriting a value
 * already stored on some key, it stacks it on that value. When remove/get
 * is called on that key the most recent value is removed/returned. Keys in 
 * this "map" are Strings, and values are {@link ValueWrapper}s.
 * @author ltomic
 *
 */
public class ObjectMultistack {
	
	/**
	 * Map where stacks are stored under key
	 */
	private Map<String, MultistackEntry> map;

	/**
	 * Default constructor
	 */
	public ObjectMultistack() {
		super();
		map = new HashMap<>();
	}
	
	/**
	 * Adds a new value to the stack stored under the given key <code>name</code>
	 * @param name key of the stack on to which the value should be added
	 * @param valueWrapper value to be added
	 */
	public void push(String name, ValueWrapper valueWrapper) {
		Objects.requireNonNull(name, "name cannot be null");
		Objects.requireNonNull(valueWrapper, "value cannot be null");
		
		MultistackEntry entry = map.get(name);
		
		if (entry == null) {
			map.put(name, new MultistackEntry(valueWrapper));
		} else {
			MultistackEntry newEntry = new MultistackEntry(entry, valueWrapper);
			map.put(name, newEntry);
		}
	}
	
	/**
	 * Returns and removes the latest added object on the stack with given key
	 * @param name key under which the stack from which the object should be fetched and removed
	 * @return the latest added object on the stack with given key
	 */
	public ValueWrapper pop(String name) {
		Objects.requireNonNull(name, "name cannot be null");
		
		MultistackEntry entry = map.get(name);
		
		if (entry == null) throw new EmptyStackException();
		
		map.remove(name);
		MultistackEntry nextEntry = entry.next;
		
		if (nextEntry != null) {
			map.put(name, nextEntry);
		}
		
		return entry.value;
	}
	
	/**
	 * Returns the latest added object on the stack with given key
	 * @param name key under which the stack from which the object should be fetched
	 * @return the latest added object on the stack with given key
	 */
	public ValueWrapper peek(String name) {
		Objects.requireNonNull(name, "name cannot be null");
		
		MultistackEntry entry = map.get(name);
		
		if (entry == null) throw new EmptyStackException();
		
		return entry.value;
	}
	
	/**
	 * Tests if a stack under given key is empty
	 * @param name key of the stack to be tested
	 * @return true if stack under given key is empty
	 */
	public boolean isEmpty(String name) {
		return map.get(name) == null;
	}
	
	/**
	 * Class which implements a stack in this map and in which the values are stored.
	 * Value should be wrapped in {@link ValueWrapper}.
	 * @author ltomic
	 *
	 */
	public static class MultistackEntry {
		
		/**
		 * Reference to the next value in stack.
		 */
		private MultistackEntry next;
		/**
		 * Value stored in this entry.
		 */
		private ValueWrapper value;
		
		/**
		 * Constructs an entry with given parameters
		 * @param next next value in stack
		 * @param value value to be stored
		 */
		public MultistackEntry(MultistackEntry next, ValueWrapper value) {
			super();
			this.next = next;
			this.value = Objects.requireNonNull(value);
		}
		
		/**
		 * Constructs an entry with null as next object in stack(thus this is a last element
		 * in stack) and parameter value
		 * @param value value to be stored
		 */
		public MultistackEntry(ValueWrapper value) {
			this(null, value);
		}

		/**
		 * Returns the next entry in stack
		 * @return the next entry in stack
		 */
		public MultistackEntry getNext() {
			return next;
		}

		/**
		 * Returns value stored in this entry.
		 * @return value stored in this entry.
		 */
		public ValueWrapper getValue() {
			return value;
		}

		/**
		 * Sest value stored in this entry. Cannot be null.
		 * Value should be wrapped in {@link ValueWrapper}.
		 * @param value new value to be stored, {@link ValueWrapper}
		 */
		public void setValue(ValueWrapper value) {
			this.value = Objects.requireNonNull(value, "value cannot be null");
		}
		
	}

}
