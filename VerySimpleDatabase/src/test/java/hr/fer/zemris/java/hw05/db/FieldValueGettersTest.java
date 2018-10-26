package hr.fer.zemris.java.hw05.db;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class FieldValueGettersTest {

	private static StudentRecord record;
	@Before
	public void setUpRecord() {
		record = new StudentRecord("0000000000", "test", "name", "5");
	}
	
	@Test
	public void getFirstName() {
		assertEquals("name", FieldValueGetters.FIRST_NAME.get(record));
	}
	
	@Test
	public void getLastName() {
		assertEquals("test", FieldValueGetters.LAST_NAME.get(record));
	}
	
	@Test
	public void getJMBAGName() {
		assertEquals("0000000000", FieldValueGetters.JMBAG.get(record));
	}

}
