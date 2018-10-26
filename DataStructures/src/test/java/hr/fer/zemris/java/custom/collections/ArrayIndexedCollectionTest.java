package hr.fer.zemris.java.custom.collections;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArrayIndexedCollectionTest {
	
	@Test(expected = IllegalArgumentException.class)
	public void capacityNotPositive() {
		new ArrayIndexedCollection(0);
	}

	@Test(expected = NullPointerException.class)
	public void givenCollectionIsNull() {
		new ArrayIndexedCollection(null);
	}
	
	
	@Test
	public void insertOneElement() {
		ArrayIndexedCollection collection = new ArrayIndexedCollection();
		collection.add('a');
		
		assertEquals(1, collection.size());
		assertEquals('a', collection.get(0));
		assertEquals(0, collection.indexOf('a'));
	}
		
	@Test
	public void insertDuplicateElements() {
		ArrayIndexedCollection collection = new ArrayIndexedCollection();
		collection.add('a');
		collection.add('a');
		
		assertEquals(2, collection.size());
		assertEquals('a', collection.get(0));
		assertEquals('a', collection.get(1));
		assertEquals(0, collection.indexOf('a'));
	}
	
	public ArrayIndexedCollection setUpThreeElements() {
		ArrayIndexedCollection collection = new ArrayIndexedCollection();
		collection.add('a');
		collection.add(42);
		collection.add(new String("Ninja"));
		return collection;
	}
	
	@Test
	public void insertDifferentElements() {
		ArrayIndexedCollection collection = setUpThreeElements();
		
		assertEquals(3, collection.size());
		assertEquals('a', collection.get(0));
		assertEquals(42, collection.get(1));
		assertEquals("Ninja", collection.get(2));
		assertEquals(2, collection.indexOf("Ninja"));
	}
	
	@Test
	public void clearCollection() {
		ArrayIndexedCollection collection = setUpThreeElements();
		
		collection.clear();
		
		assertEquals(0, collection.size());
		assertEquals(-1, collection.indexOf('a'));
		assertEquals(-1, collection.indexOf("Ninja"));
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void removeFromEmptyCollection() {
		ArrayIndexedCollection collection = new ArrayIndexedCollection();
		collection.remove(0);
	}
	
	@Test
	public void insertOneElementAtPosition() {
		ArrayIndexedCollection collection = setUpThreeElements();
		
		collection.insert('d', 1);
		
		assertEquals(4, collection.size());
		assertEquals(1, collection.indexOf('d'));
		assertEquals('d', collection.get(1));
		assertEquals(42, collection.get(2));
		assertEquals("Ninja", collection.get(3));
	}
	
	@Test
	public void insertElementAtFirstPosition() {
		ArrayIndexedCollection collection = setUpThreeElements();
		
		collection.insert('d', 0);
		
		assertEquals(4, collection.size());
		assertEquals('a', collection.get(1));
		assertEquals("Ninja", collection.get(3));
		assertEquals('d', collection.get(0));
	}
	
	@Test
	public void insertMultipleElementsAtPosition() {
		ArrayIndexedCollection collection = setUpThreeElements();
		
		collection.insert('d', 1);
		collection.insert('e', 1);
		
		assertEquals(5, collection.size());
		assertEquals('d', collection.get(2));
		assertEquals('e', collection.get(1));
		assertEquals("Ninja", collection.get(4));
		assertEquals(2, collection.indexOf('d'));
	}
	
	@Test
	public void removeByIndexOnlyElement() {
		ArrayIndexedCollection collection = new ArrayIndexedCollection();
		collection.add('a');
		collection.remove(0);
		
		assertEquals(0, collection.size());
		assertEquals(-1, collection.indexOf('a'));
	}
	
	@Test
	public void removeByIndexFirstElement() {
		ArrayIndexedCollection collection = setUpThreeElements();
		collection.remove(0);
		
		assertEquals(2, collection.size());
		assertEquals(42, collection.get(0));
		assertEquals("Ninja", collection.get(1));
		assertEquals(-1, collection.indexOf('a'));
	}
	
	@Test
	public void removeByIndexMiddleElement() {
		ArrayIndexedCollection collection = new ArrayIndexedCollection();
		collection.add('a');
		collection.add('b');
		collection.add('c');
		collection.add('d');
		
		collection.remove(1);
		
		assertEquals(3, collection.size());
		assertEquals('a', collection.get(0));
		assertEquals('c', collection.get(1));
		assertEquals('d', collection.get(2));
		
		assertEquals(2, collection.indexOf('d'));
	}
	
	@Test
	public void removeByIndexLastElement() {
		ArrayIndexedCollection collection = setUpThreeElements();
		
		collection.remove(2);
		
		assertEquals(2, collection.size());
		assertEquals('a', collection.get(0));
		assertEquals(42, collection.get(1));
		assertEquals(-1, collection.indexOf("Ninja"));
	}
	
	@Test
	public void removeByObjectEmpty() {
		ArrayIndexedCollection collection = new ArrayIndexedCollection();
		collection.remove(Character.valueOf('a'));
	}
	
	@Test
	public void removeByObject() {
		ArrayIndexedCollection collection = setUpThreeElements();
		
		collection.remove("Ninja");
		
		assertEquals(2, collection.size());
		assertEquals(42, collection.get(1));
		Assert.assertFalse(collection.contains("Ninja"));
	}
	
	@Test
	public void increaseCapacity() {
		ArrayIndexedCollection collection = new ArrayIndexedCollection(2);
		collection.add('a');
		collection.add('b');
		collection.add('c');
		
		assertEquals(3, collection.size());
		assertEquals('a', collection.get(0));
		assertEquals('b', collection.get(1));
		assertEquals('c', collection.get(2));
		
	}
}
