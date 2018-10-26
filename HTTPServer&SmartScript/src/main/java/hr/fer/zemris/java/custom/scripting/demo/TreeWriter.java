package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * Program that reads a SmartScript file, parses it and reproduces back its approximate
 * original form to standard output. 
 * @author ltomic
 *
 */
public class TreeWriter {

	/**
	 * Method called at the beginning of the program. Expects a single argument :
	 * path to file to be processed.
	 * @param args - path to file to be processed
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String docBody = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8);
		SmartScriptParser p = new SmartScriptParser(docBody);
		WriterVisitor visitor = new WriterVisitor();
		p.getDocumentNode().accept(visitor);
	}
	
	/**
	 * INodeVisitor repreducing part of the document for each provided node
	 * @author ltomic
	 *
	 */
	private static class WriterVisitor implements INodeVisitor {

		/**
		 * Reproduces original part of the document representing text node
		 * @param node node whose text representation should be outputed
		 */
		@Override
		public void visitTextNode(TextNode node) {
			System.out.print(node.getText());
		}

		/**
		 * Reproduces original part of the document representing for loop node
		 * @param node node whose text representation should be outputed
		 */
		@Override
		public void visitForLoopNode(ForLoopNode node) {
			StringBuilder text = new StringBuilder();
			text.append("{$ FOR " + node.getVariable()+ " " + node.getStartExpression()+ " " + node.getEndExpression());
			if (node.getStepExpression()!= null) text.append(" " + node.getStepExpression());
			text.append(" $}");
			System.out.print(text.toString());
			
			for (int i = 0, sz = node.numberOfChildren(); i < sz; ++i) {
				node.getChild(i).accept(this);
			}
			
			System.out.print("{$ END $}");
		}

		/**
		 * Reproduces original part of the document representing echo node
		 * @param node node whose text representation should be outputed
		 */
		@Override
		public void visitEchoNode(EchoNode node) {
			StringBuilder text = new StringBuilder("{$");
			
			for (Element i : node.getElements()) {
				text.append(" " + i.toString());
			}
			
			text.append(" $}");
			
			System.out.print(text.toString());
		}

		/**
		 * Reproduces original part of the document representing document node
		 * @param node node whose text representation should be outputed
		 */
		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0, sz = node.numberOfChildren(); i < sz; ++i) {
				node.getChild(i).accept(this);
			}
		}
		
	}
	
	
}
