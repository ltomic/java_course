package hr.fer.zemris.java.hw07.shell.commands;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilsTest {

	@Test
	public void empty() {
		String test = "";
		assertArrayEquals(new String[0], Utils.parse_arguments(test));
	}
	
	@Test
	public void whitespace() {
		String test = "    	";
		String[] expected = new String[] {};
		assertArrayEquals(expected, Utils.parse_arguments(test));
	}
	
	@Test
	public void oneChar() {
		String test = "a";
		String[] expected = new String[] {"a"};
		assertArrayEquals(expected, Utils.parse_arguments(test));
	}
	
	@Test
	public void oneWord() {
		String test = "    BLSADW   ";
		String[] expected = new String[] {"BLSADW"};
		assertArrayEquals(expected, Utils.parse_arguments(test));
	}
	
	@Test
	public void multipleWords() {
		String test = "    bla1 bla2    bla3 \n 52";
		String[] expected = new String[] {"bla1", "bla2", "bla3", "52"};
		assertArrayEquals(expected, Utils.parse_arguments(test));
	}
	
	@Test
	public void emptyQuotes() {
		String test = "\"\"";
		String[] expected = new String[] {""};
		assertArrayEquals(expected, Utils.parse_arguments(test));
	}

	@Test
	public void quotes() {
		String test = "\"test1\"";
		String[] expected = new String[] {"test1"};
		assertArrayEquals(expected, Utils.parse_arguments(test));
	}
	
	@Test
	public void multipleQuotes() {
		String test = "\"Test1\"    \"Test2\"";
		String[] expected = new String[] {"Test1", "Test2"};
		assertArrayEquals(expected, Utils.parse_arguments(test)); 
	}
	
	@Test
	public void spaceInQuotes() {
		String test = "\"  te  st 1  \"";
		String[] expected = new String[] {"  te  st 1  "};
		assertArrayEquals(expected, Utils.parse_arguments(test));
	}
	
	@Test
	public void quotesInQuotes() {
		String test = "\"  \\\"te  st 1  \"";
		String[] expected = new String[] {"  \"te  st 1  "};
		assertArrayEquals(expected, Utils.parse_arguments(test));
	}
	
	@Test
	public void escapingInQuotes() {
		String test = "\"  \\\"te  st 1\\\\\\\"  \"";
		String[] expected = new String[] {"  \"te  st 1\\\"  "};
		assertArrayEquals(expected, Utils.parse_arguments(test));
	}
	
	@Test
	public void quotesAndNormal() {
		String test = "\"  \\\"te  st 0  \"  test1 \"test2\" test3 test4";
		String[] expected = new String[] {"  \"te  st 0  ", "test1", "test2", "test3", "test4"};
		assertArrayEquals(expected, Utils.parse_arguments(test));
	}
	
	@Test
	public void testExample() {
		String test = "\"C:/Program Files/Program1/info.txt\" C:/tmp/informacije.txt";
		String[] expected = new String[] {"C:/Program Files/Program1/info.txt", "C:/tmp/informacije.txt"};
		assertArrayEquals(expected, Utils.parse_arguments(test));
	}
}











