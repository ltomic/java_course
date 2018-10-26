package hr.fer.zemris.java.hw05.db;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class QueryParserTest {

	private static String projectPath = System.getProperty("user.dir");
	
	public static List<String> loader(String pathToFile) {
		try {
			return Files.readAllLines(
				 Paths.get(projectPath + pathToFile),
				 StandardCharsets.UTF_8
				);
		} catch (IOException ex) {
			throw new IllegalArgumentException("Could not load database text file, " +
				"possibly wrong file path given");
		}
	}
	
	@Test
	public void testSpaces() {
		List<String> testFile = loader("/src/test/resources/testSpaces.txt");
				
		ConditionalExpression[][] expectedExpr = new ConditionalExpression[][] {
				new ConditionalExpression[] {
						new ConditionalExpression(FieldValueGetters.LAST_NAME, "Bosnić", ComparisonOperators.EQUALS)
				},
				new ConditionalExpression[] {
						new ConditionalExpression(FieldValueGetters.LAST_NAME, "Bosnić", ComparisonOperators.EQUALS)
				},
				new ConditionalExpression[] {
						new ConditionalExpression(FieldValueGetters.LAST_NAME, "Bosnić", ComparisonOperators.EQUALS)
				},
				new ConditionalExpression[] {
						new ConditionalExpression(FieldValueGetters.LAST_NAME, "Bosnić", ComparisonOperators.EQUALS)
				}
		};
		
		for (int i = 0; i < testFile.size(); ++i) {
			String query = testFile.get(i);
			QueryParser parser = new QueryParser(query);
			assertArrayEquals(expectedExpr[i], parser.getQuery().toArray());
		}
		
	}
	
	@Test
	public void testAnd() {
		List<String> testFile = loader("/src/test/resources/testAnd.txt");
				
		ConditionalExpression[][] expectedExpr = new ConditionalExpression[][] {
				new ConditionalExpression[] {
						new ConditionalExpression(FieldValueGetters.FIRST_NAME, "A", ComparisonOperators.GREATER),
						new ConditionalExpression(FieldValueGetters.LAST_NAME, "B*ć", ComparisonOperators.LIKE)
				},
		};
		
		for (int i = 0; i < testFile.size(); ++i) {
			String query = testFile.get(i);
			QueryParser parser = new QueryParser(query);
			assertArrayEquals(expectedExpr[i], parser.getQuery().toArray());
		}
		
	}
	
	@Test
	public void testAndMultiple() {
		List<String> testFile = loader("/src/test/resources/testAndMultiple.txt");
				
		ConditionalExpression[][] expectedExpr = new ConditionalExpression[][] {
				new ConditionalExpression[] {
						new ConditionalExpression(FieldValueGetters.FIRST_NAME, "A", ComparisonOperators.GREATER),
						new ConditionalExpression(FieldValueGetters.LAST_NAME, "B*ć", ComparisonOperators.LIKE),
						new ConditionalExpression(FieldValueGetters.JMBAG, "0000000001", ComparisonOperators.EQUALS)
				},
		};
		
		for (int i = 0; i < testFile.size(); ++i) {
			String query = testFile.get(i);
			QueryParser parser = new QueryParser(query);
			assertArrayEquals(expectedExpr[i], parser.getQuery().toArray());
		}
		
	}
	
	@Test
	public void testGeneral() {
		List<String> testFile = loader("/src/test/resources/testGeneral.txt");
				
		ConditionalExpression[][] expectedExpr = new ConditionalExpression[][] {
				new ConditionalExpression[] {
						new ConditionalExpression(FieldValueGetters.JMBAG, "0000000003", ComparisonOperators.EQUALS)
				},
				new ConditionalExpression[] {
						new ConditionalExpression(FieldValueGetters.LAST_NAME, "Blažić", ComparisonOperators.EQUALS)
				},
				new ConditionalExpression[] {
						new ConditionalExpression(FieldValueGetters.FIRST_NAME, "A", ComparisonOperators.GREATER),
						new ConditionalExpression(FieldValueGetters.LAST_NAME, "B*ć", ComparisonOperators.LIKE)
				},
				new ConditionalExpression[] {
						new ConditionalExpression(FieldValueGetters.FIRST_NAME, "A", ComparisonOperators.GREATER),
						new ConditionalExpression(FieldValueGetters.FIRST_NAME, "C", ComparisonOperators.LESS),
						new ConditionalExpression(FieldValueGetters.LAST_NAME, "B*ć", ComparisonOperators.LIKE),
						new ConditionalExpression(FieldValueGetters.JMBAG, "0000000002", ComparisonOperators.GREATER),
				}
		};
		
		for (int i = 0; i < testFile.size(); ++i) {
			String query = testFile.get(i);
			QueryParser parser = new QueryParser(query);
			assertArrayEquals(expectedExpr[i], parser.getQuery().toArray());
		}
		
	}
	
	@Test
	public void testExample1() {
		List<String> testFile = loader("/src/test/resources/testExample1.txt");
				
		ConditionalExpression[][] expectedExpr = new ConditionalExpression[][] {
				new ConditionalExpression[] {
						new ConditionalExpression(FieldValueGetters.JMBAG, "0000000003", ComparisonOperators.EQUALS),
						new ConditionalExpression(FieldValueGetters.LAST_NAME, "B*", ComparisonOperators.LIKE)
				},
		};
		
		for (int i = 0; i < testFile.size(); ++i) {
			String query = testFile.get(i);
			QueryParser parser = new QueryParser(query);
			assertArrayEquals(expectedExpr[i], parser.getQuery().toArray());
		}
		
	}

}














