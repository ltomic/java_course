package hr.fer.zemris.java.hw05.db;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class StudentDatabaseTest {

	private static List<String> listTextFile;
	private static StudentDatabase database;
	
	public static List<String> loader(String filename) {
		try {
			return Files.readAllLines(
				 Paths.get(projectPath + filename),
				 StandardCharsets.UTF_8
				);
		} catch (IOException ex) {
			throw new IllegalArgumentException("Could not load database text file, " +
				"possibly wrong file path given");
		}
	}
	
	private static String projectPath = System.getProperty("user.dir");
	private static String databaseFilePath = "/src/main/resources/database.txt";
	
	@BeforeClass 
	public static void init() {
		listTextFile = loader(databaseFilePath);
		database = new StudentDatabase(listTextFile);
	}
	
	@Test
	public void testLoading() {
		StringBuilder textFileBuilder = new StringBuilder();
		for (String line: listTextFile) {
			textFileBuilder.append(line + "\n");
		}
		String textFile = textFileBuilder.toString();
		
		assertTrue(database.toString().replaceAll("\\s+", "").equals(
				textFile.replaceAll("\\s+", "")));
	}

	@Test
	public void filterFalse() {
		IFilter alwaysFalse = (record)->false;
		
		List<StudentRecord> students = database.filter(alwaysFalse);
		
		assertEquals(0, students.size());
	}
	
	@Test
	public void filterTrue() {
		IFilter alwaysTrue = (record)->true;
		
		List <StudentRecord> filteredDatabaseList = database.filter(alwaysTrue);
		StudentRecord[] filteredDatabaseArray = filteredDatabaseList.toArray(new StudentRecord[0]);
		
		assertArrayEquals(database.toArray(), filteredDatabaseArray);
	}
	
	@Test
	public void getByJBMAG() {
		assertEquals("Akšamović", database.forJMBAG("0000000001").getLastName());
		assertEquals("Orešković", database.forJMBAG("0000000041").getLastName());
		assertEquals("Dokleja", database.forJMBAG("0000000010").getLastName());
		assertEquals("Glavinić Pecotić", database.forJMBAG("0000000015").getLastName());
		assertEquals("Kos-Grabar", database.forJMBAG("0000000029").getLastName());
		assertEquals("Markoč", database.forJMBAG("0000000037").getLastName());
		assertEquals("Žabčić", database.forJMBAG("0000000063").getLastName());
	}
	
	@Test
	public void getByJBMAGNotInDatabase() {
		assertEquals(null, database.forJMBAG("1234567890"));
	}
}
