package hr.fer.zemris.java.custom.collections;

import org.junit.Assert;
import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class LinkedListIndexedCollectionTest {

	@Test(expected = NullPointerException.class)
	public void givenCollectionIsNull() {
		new LinkedListIndexedCollection(null);
	}

	@Test
	public void emptyList() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		
		assertEquals(0, list.size());
		assertEquals(-1, list.indexOf(1));
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void getIndexOutOfBounds() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		
		list.get(1);
	}
	
	@Test
	public void insertOneElement() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add('a');
		
		assertEquals(1, list.size());
		assertEquals('a', list.get(0));
		assertEquals(0, list.indexOf('a'));
	}
	
	public LinkedListIndexedCollection setUpThreeElements() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add('a');
		list.add(Integer.valueOf(42));
		list.add("Ninja");
		
		return list;
	}
	
	@Test
	public void addDuplicateElements() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add('a');
		list.add('a');
		
		assertEquals(2, list.size());
		assertEquals('a', list.get(0));
		assertEquals('a', list.get(1));
		assertEquals(0, list.indexOf('a'));
	}
	
	@Test
	public void addMultipleElements() {
		LinkedListIndexedCollection list = setUpThreeElements();
		
		assertEquals(3, list.size());
		assertEquals(42, list.get(1));
		assertEquals("Ninja", list.get(2));
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void removeByIndexFromEmptyList() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		
		list.remove(0);
	}
	
	@Test
	public void removeByIndexOnlyElement() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add('a');
		list.remove(0);
		
		assertEquals(0, list.size());
		assertEquals(-1, list.indexOf('a'));
	}
	
	@Test
	public void removeByIndexFirstElement() {
		LinkedListIndexedCollection list = setUpThreeElements();
		list.remove(0);
		
		assertEquals(2, list.size());
		assertEquals(42, list.get(0));
		assertEquals("Ninja", list.get(1));
		assertEquals(-1, list.indexOf('a'));
	}
	
	@Test
	public void removeByIndexMiddleElement() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add('a');
		list.add('b');
		list.add('c');
		list.add('d');
		
		list.remove(1);
		
		assertEquals(3, list.size());
		assertEquals('c', list.get(1));
		assertEquals('d', list.get(2));
		assertEquals(-1, list.indexOf('b'));
	}
	
	@Test
	public void removeByIndexLastElement() {
		LinkedListIndexedCollection list = setUpThreeElements();
		list.remove(2);
		
		assertEquals(2, list.size());
		assertEquals('a', list.get(0));
		assertEquals(42, list.get(1));
		assertEquals(-1, list.indexOf("Ninja"));
	}
	
	@Test
	public void removeObjectEmptyList() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.remove("Ninja");
		
		assertEquals(0, list.size());
		assertEquals(-1, list.indexOf("Ninja"));
	}
	
	@Test
	public void removeObjectList() {
		LinkedListIndexedCollection list = setUpThreeElements();
		
		Object value = Character.valueOf('a');
		
		list.remove(value);
		
		assertEquals(2, list.size());
		assertEquals(-1, list.indexOf(value));
		assertEquals(42, list.get(0));
		assertEquals("Ninja", list.get(1));
	}
	
	@Test
	public void removeMultipleObjectsList() {
		LinkedListIndexedCollection list = setUpThreeElements();
		
		Object value1 = Character.valueOf('a');
		Object value2 = String.valueOf("Ninja");
		
		list.remove(value1);
		list.remove(value2);
		
		assertEquals(1, list.size());
		assertEquals(42, list.get(0));
		assertEquals(-1, list.indexOf(value1));
		assertEquals(-1, list.indexOf(value2));
	}
	
	@Test
	public void insertElementAtFirstPosition() {
		LinkedListIndexedCollection list = setUpThreeElements();
		list.insert(true, 0);
		
		assertEquals(4, list.size());
		assertEquals(true, list.get(0));
		assertEquals('a', list.get(1));
		assertEquals(0, list.indexOf(true));
	}
	
	@Test
	public void insertElementAtMiddlePosition() {
		LinkedListIndexedCollection list = setUpThreeElements();
		list.insert(true, 1);
		
		assertEquals(4, list.size());
		assertEquals(true, list.get(1));
		assertEquals('a', list.get(0));
		assertEquals(42, list.get(2));
		assertEquals(1, list.indexOf(true));
	}
	
	@Test
	public void insertMultipleElementsAtPosition() {
		LinkedListIndexedCollection list = setUpThreeElements();
		list.insert('e', 1);
		list.insert('f', 1);
		
		assertEquals(5, list.size());
		assertEquals('f', list.get(1));
		assertEquals('e', list.get(2));
		assertEquals(42, list.get(3));
	}
	
}
