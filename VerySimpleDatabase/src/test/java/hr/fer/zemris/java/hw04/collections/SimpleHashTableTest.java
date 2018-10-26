package hr.fer.zemris.java.hw04.collections;

import static org.junit.Assert.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;

import hr.fer.zemris.java.hw05.collections.SimpleHashTable;
import hr.fer.zemris.java.hw05.collections.SimpleHashTable.TableEntry;

@SuppressWarnings("javadoc")
public class SimpleHashTableTest {

	@Test
	public void DefaultConstructor() {
		SimpleHashTable<String, String> table = new SimpleHashTable<String, String>();
		
		assertEquals(16, table.getCapacity());
	}

	@Test(expected = IllegalArgumentException.class)
	public void ZeroCapacity() {
		new SimpleHashTable<String, String>(0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void NegativeCapacity() {
		new SimpleHashTable<String, String>(-1);
	}
	
	@Test
	public void Capacity128() {
		SimpleHashTable<String, String> table = new SimpleHashTable<String, String>(128);
		
		assertEquals(128, table.getCapacity());
	}
	
	@Test
	public void Capacity100() {
		SimpleHashTable<String, String> table = new SimpleHashTable<String, String>(100);
		
		assertEquals(128, table.getCapacity());
	}
	
	@Test
	public void emptyTable() {
		SimpleHashTable<String, Integer> table = new SimpleHashTable<String, Integer>();
		
		assertTrue(table.isEmpty());
		assertEquals(0, table.size());
		
		assertEquals(null, table.get("test"));
	}
	
	@Test
	public void getNullKey() {
		SimpleHashTable<String, Integer> table = new SimpleHashTable<String, Integer>();
		
		table.get(null);
	}
	
	@Test
	public void insertOneEntry() {
		SimpleHashTable<String, Integer> table = new SimpleHashTable<String, Integer>(2);
		
		table.put("test", 1);

		assertEquals(1, table.size());
		assertEquals(Integer.valueOf(1), table.get("test"));
	}
	
	@Test
	public void overwriteEntry() {
		SimpleHashTable<String, Integer> table = new SimpleHashTable<String, Integer>(2);
		
		table.put("test", 1);
		table.put("test", 2);

		assertEquals(1, table.size());
		assertEquals(Integer.valueOf(2), table.get("test"));
	}	
	
	public SimpleHashTable<String, Integer> setUpTableFourElements() {
		SimpleHashTable<String, Integer> table = new SimpleHashTable<String, Integer>(2);
		
		table.put("jedan", 1);
		table.put("dva", 2);
		table.put("tri", 3);
		table.put("cetiri", 4);

		return table;
	}
	
	@Test
	public void insertMultiple() {
		SimpleHashTable<String, Integer> table = setUpTableFourElements();

		assertEquals(4, table.size());
		assertEquals(Integer.valueOf(1), table.get("jedan"));
		assertEquals(Integer.valueOf(2), table.get("dva"));
		assertEquals(Integer.valueOf(3), table.get("tri"));
		assertEquals(Integer.valueOf(4), table.get("cetiri"));
	}
	
	@Test
	public void containsKeyNull() {
		SimpleHashTable<String, Integer> table = setUpTableFourElements();
		
		assertFalse(table.containsKey(null));
	}
	
	@Test
	public void containsKey() {
		SimpleHashTable<String, Integer> table = setUpTableFourElements();

		assertTrue(table.containsKey("jedan"));
		assertTrue(table.containsKey("dva"));
		assertTrue(table.containsKey("tri"));
		assertTrue(table.containsKey("cetiri"));
	}
		
	@Test
	public void containsValueNullFalse() {
		SimpleHashTable<String, Integer> table = setUpTableFourElements();
		
		assertFalse(table.containsValue(null));
	}
	
	@Test
	public void containsValueNullTrue() {
		SimpleHashTable<String, Integer> table = setUpTableFourElements();
		
		table.put("test", null);
		
		assertTrue(table.containsValue(null));
	}
	
	@Test
	public void containsValue() {
		SimpleHashTable<String, Integer> table = setUpTableFourElements();

		assertFalse(table.containsValue(5));
		assertFalse(table.containsValue("test"));
		assertTrue(table.containsValue(1));
		assertTrue(table.containsValue(2));
		assertTrue(table.containsValue(3));
		assertTrue(table.containsValue(4));
	}
	
	@Test
	public void removeKeyNull() {
		SimpleHashTable<String, Integer> table = setUpTableFourElements();
		
		table.remove(null);
		
		assertEquals(4, table.size());
		assertEquals(Integer.valueOf(1), table.get("jedan"));
		assertEquals(Integer.valueOf(2), table.get("dva"));
		assertEquals(Integer.valueOf(3), table.get("tri"));
		assertEquals(Integer.valueOf(4), table.get("cetiri"));
	}
	
	@Test
	public void removeKeyNotInTable() {
		SimpleHashTable<String, Integer> table = setUpTableFourElements();
		
		table.remove(5);
		
		assertEquals(4, table.size());
		assertEquals(Integer.valueOf(1), table.get("jedan"));
		assertEquals(Integer.valueOf(2), table.get("dva"));
		assertEquals(Integer.valueOf(3), table.get("tri"));
		assertEquals(Integer.valueOf(4), table.get("cetiri"));
	}
	
	@Test
	public void removeKey() {
		SimpleHashTable<String, Integer> table = setUpTableFourElements();
		
		table.remove("jedan");
		
		assertEquals(3, table.size());
		assertFalse(table.containsKey("jedan"));
		assertFalse(table.containsValue(1));
		assertEquals(Integer.valueOf(2), table.get("dva"));
		assertEquals(Integer.valueOf(3), table.get("tri"));
		assertEquals(Integer.valueOf(4), table.get("cetiri"));
	}
	
 	public SimpleHashTable<Integer, String> setUpTableFourIntString() {
		SimpleHashTable<Integer, String> table = new SimpleHashTable<Integer, String>(2);
		
		table.put(1, "jedan");
		table.put(2, "dva");
		table.put(3, "tri");
		table.put(4, "cetiri");

		return table;
 	}
 	
 	public SimpleHashTable<Integer, String> setUpTableHash() {
		SimpleHashTable<Integer, String> table = new SimpleHashTable<Integer, String>(8);
		
		table.put(1, "jedan");
		table.put(2, "dva");
		table.put(9, "devet");
		table.put(10, "deset");

		return table;
 	}
 	
	 
	@Test
	public void removeFirstEntryInSlot() {
		SimpleHashTable<Integer, String> table = setUpTableHash();
		
		table.remove(1);
		
		assertEquals(3, table.size());
		assertFalse(table.containsKey(1));
		assertFalse(table.containsValue("jedan"));
		assertEquals("dva", table.get(2));
		assertEquals("devet", table.get(9));
		assertEquals("deset", table.get(10));
	}
	
	@Test
	public void removeLastEntryInSlot() {
		SimpleHashTable<Integer, String> table = setUpTableHash();
		
		table.remove(9);
		
		assertEquals(3, table.size());
		assertFalse(table.containsKey(9));
		assertFalse(table.containsValue("devet"));
		assertEquals("dva", table.get(2));
		assertEquals("jedan", table.get(1));
		assertEquals("deset", table.get(10));
	}

	@Test
	public void testToString() {
		SimpleHashTable<Integer, String> table = setUpTableHash();
		
		assertEquals("[1=jedan, 9=devet, 2=dva, 10=deset]", table.toString());
	}
	
	@Test
	public void increaseCapacity() {
		SimpleHashTable<Integer, String> table = new SimpleHashTable<Integer, String>(4);
		
		table.put(1, "jedan");
		
		assertEquals(4, table.getCapacity());
		
		table.put(2, "dva");
		
		assertEquals(4, table.getCapacity());
		
		table.put(3, "tri");
		
		assertEquals(8, table.getCapacity());
		
		assertEquals(3, table.size());
		assertEquals("dva", table.get(2));
		assertEquals("jedan", table.get(1));
		assertEquals("tri", table.get(3));
	}
	
	@Test
	public void clear() {
		SimpleHashTable<String, Integer> table = setUpTableFourElements();
		
		table.clear();
		
		assertEquals(0, table.size());
		assertEquals(8, table.getCapacity());
		
		assertFalse(table.containsKey("jedan"));
		assertFalse(table.containsKey("dva"));
		assertFalse(table.containsKey("tri"));
		assertFalse(table.containsKey("cetiri"));
		
		assertEquals(null, table.get("jedan"));
		assertEquals(null, table.get("dva"));
		assertEquals(null, table.get("tri"));
		assertEquals(null, table.get("cetiri"));
	}
	
	@Test
	public void iterateOverAllValues() {
		SimpleHashTable<Integer, String> table = setUpTableHash();
		Iterator<SimpleHashTable.TableEntry<Integer, String>> iter = table.iterator();
		
		StringBuilder string = new StringBuilder();
		while (iter.hasNext()) {
			string.append(iter.next() + " ");
		}
		
		assertEquals("1=jedan 9=devet 2=dva 10=deset ", string.toString());
	}
	
	@Test
	public void removeFirstEntryInSlotUsingIterator() {
		SimpleHashTable<Integer, String> table = setUpTableHash();
		Iterator<SimpleHashTable.TableEntry<Integer, String>> iter = table.iterator();
		
		while (iter.hasNext()) {
			TableEntry<Integer, String> entry = iter.next();
			if (entry.getKey().equals(1)) {
				iter.remove();
			}
		}
		
		assertEquals(3, table.size());
		assertFalse(table.containsKey(1));
		assertFalse(table.containsValue("jedan"));
		assertEquals("dva", table.get(2));
		assertEquals("devet", table.get(9));
		assertEquals("deset", table.get(10));
	}
	
	@Test
	public void removeLastEntryInSlotUsingIterator() {
		SimpleHashTable<Integer, String> table = setUpTableHash();
		Iterator<SimpleHashTable.TableEntry<Integer, String>> iter = table.iterator();
		
		while (iter.hasNext()) {
			TableEntry<Integer, String> entry = iter.next();
			if (entry.getKey().equals(9)) {
				iter.remove();
			}
		}
		
		assertEquals(3, table.size());
		assertFalse(table.containsKey(9));
		assertFalse(table.containsValue("devet"));
		assertEquals("dva", table.get(2));
		assertEquals("jedan", table.get(1));
		assertEquals("deset", table.get(10));
	}
	
	@Test(expected = NoSuchElementException.class)
	public void iterateWithoutChecking() {
		SimpleHashTable<Integer, String> table = setUpTableHash();
		Iterator<SimpleHashTable.TableEntry<Integer, String>> iter = table.iterator();
		
		while (true) {
			iter.next();
		}
	}
	
	@Test(expected = IllegalStateException.class)
	public void iteratorRemoveTwice() {
		SimpleHashTable<Integer, String> table = setUpTableHash();
		Iterator<SimpleHashTable.TableEntry<Integer, String>> iter = table.iterator();
		
		while (iter.hasNext()) {
			TableEntry<Integer, String> entry = iter.next();
			if (entry.getKey().equals(9)) {
				iter.remove();
				iter.remove();
			}
		}
	}
	
	@Test(expected = ConcurrentModificationException.class)
	public void removeFromTableWhileIterating() {
		SimpleHashTable<Integer, String> table = setUpTableHash();
		Iterator<SimpleHashTable.TableEntry<Integer, String>> iter = table.iterator();
		
		while (iter.hasNext()) {
			TableEntry<Integer, String> entry = iter.next();
			if (entry.getKey().equals(1)) {
				table.remove(1);
			}
		}
	}
	
	@Test
	public void removeAllWhileIterating() {
		SimpleHashTable<Integer, String> table = setUpTableHash();
		Iterator<SimpleHashTable.TableEntry<Integer, String>> iter = table.iterator();
		
		while (iter.hasNext()) {
			iter.next();
			iter.remove();
		}
		
		assertEquals(0, table.size());
		assertFalse(table.containsKey(9));
		assertFalse(table.containsValue("devet"));
		assertFalse(table.containsKey(1));
		assertFalse(table.containsValue("jedan"));
		assertFalse(table.containsKey(2));
		assertFalse(table.containsValue("dva"));
		assertFalse(table.containsKey(10));
		assertFalse(table.containsValue("deset"));
	}
}















