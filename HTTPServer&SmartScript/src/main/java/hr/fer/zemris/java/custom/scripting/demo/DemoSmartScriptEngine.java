package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Program demonstrating usage of SmartScriptEngine. Executes the given
 * SmartScript file.
 * @author ltomic
 *
 */
public class DemoSmartScriptEngine {

	/**
	 * Reads the the file from the provided path and returns it as a single string.
	 * @param path - file to read
	 * @return - file returned as a single string
	 */
	public static String readFromDisk(String path) {
		try {
			return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			System.err.println("Could not read file " + path);
			return null;
		}
	}

	/**
	 * Method called at the beginning of the program.
	 * Expects single argument : Path to SmartScript file to execute
	 * @param args - path to SmartScript file to execute
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String documentBody = readFromDisk(args[0]);
		Map<String,String> parameters = new HashMap<String, String>();
		Map<String,String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		// create engine and execute it
		new SmartScriptEngine(
		new SmartScriptParser(documentBody).getDocumentNode(),
		new RequestContext(System.out, parameters, persistentParameters, cookies)
		).execute();
	}
}
