package hr.fer.zemris.java.hw16.trazilica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Implements a <a href="https://en.wikipedia.org/wiki/Bag-of-words_model">Bag of words</a> model.
 * Model is constructed over text documents found in the provided directory and allows queries
 * with documents represented as strings.
 * @author ltomic
 *
 */
public class BagOfWords {

	/** Vocabulary */
	private Map<String, Integer> vocabulary = new HashMap<>();
	/** Stop words */
	private Set<String> stopWords = new HashSet<>();
	/** Loaded documents TFIDFVectors */
	private Map<Vector, Path> TFIDFVectors;
	/** Number of loaded documents */
	private int numberOfDocuments = 0;
	/** IDF vector */
	private Vector IDF;

	/**
	 * Constructs {@link BagOfWords} with provided arguments
	 * @param dir - directory over which model should be constructed
	 * @param stopWordsFile - stop words file to use
	 */
	public BagOfWords(File dir, File stopWordsFile) {
		forEachLineInFile(stopWordsFile, (line) -> {
			stopWords.add(line.toLowerCase());
		});
		buildVocabulary(dir);
		Map<Vector, Path> TFVectors = buildTFVectors(dir);
		buildIDFVector(TFVectors);
		buildTFIDFVectors(TFVectors);
	}

	/**
	 * Queries this bag of words to compute similarity between
	 * provided doc and loaded doc and return the values as {@link Map}
	 * of documents and similarity values
	 * @param doc - doc to compute similarity with
	 * @return {@link Map} of documents and similarity values
	 */
	public Map<Path, Double> querySimilar(String doc) {
		Vector TFIDF = new Vector(vocabulary.size());
		countWords.accept(doc, TFIDF);
		TFIDF.mulByElements(IDF);

		Map<Path, Double> simVal = new HashMap<>();
		TFIDFVectors.forEach((key, value) -> {
			simVal.put(value, key.cosAngle(TFIDF));
		});
		
		return simVal;
	}
	
	/**
	 * Returns vocabulary size
	 * @return vocabulary size
	 */
	public int vocabularySize() {
		return vocabulary.size();
	}

	/**
	 * Builds the vocabulary over documents found in the provided directory.
	 * @param dir - directory in which are the documents over which vocabulary should be constructed. 
	 */
	private void buildVocabulary(File dir) {
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) continue;
			numberOfDocuments++;
			forEachLineInFile(f, scanWords);
		}
	}

	/**
	 * Builds IDF vector using provided tf vectors
	 * @param TFVectors - document tf vectors
	 */
	private void buildIDFVector(Map<Vector, Path> TFVectors) {
		int[] countDocsWord = new int[vocabulary.size()];

		for (Vector i : TFVectors.keySet()) {
			for (int j = 0; j < vocabulary.size(); ++j) {
				if (i.getElement(j) != 0) countDocsWord[j]++;
			}
		}

		IDF = new Vector(vocabulary.size());
		for (int i = 0; i < countDocsWord.length; ++i) {
			IDF.setElement(i, Math.log((double)numberOfDocuments / countDocsWord[i]));
		}
	}

	/**
	 * Builds TFIDF vector using provided document tf vectors.
	 * @param TFVectors - document tf vectors
	 */
	private void buildTFIDFVectors(Map<Vector, Path> TFVectors) {
		TFVectors.forEach((key, value) -> {
			key.mulByElements(IDF);
		});
		TFIDFVectors = TFVectors;
		TFVectors = null;
	}

	/** Adds words from the line into the vocabulary */
	private final Consumer<String> scanWords = (line) -> {
		char[] data = line.toCharArray();
		StringBuilder wordBuild = new StringBuilder();

		for (int i = 0; i < data.length; ++i) {
			while (i < data.length && !Character.isAlphabetic(data[i]))
				++i;
			while (i < data.length && Character.isAlphabetic(data[i])) {
				wordBuild.append(data[i]);
				i++;
			}
			String word = wordBuild.toString().toLowerCase();
			if (!stopWords.contains(word) && !vocabulary.containsKey(word)) {
				vocabulary.put(word, vocabulary.size());
			}
			wordBuild = new StringBuilder();
		}
	};

	/**
	 * Builds TF vectors from the documents in the provided directory
	 * @param dir - directory in which are the document over which tf vectors should be built
	 * @return {@link Map} of tf vectors and documents
	 */
	private Map<Vector, Path> buildTFVectors(File dir) {
		Map<Vector, Path> TFVectors = new HashMap<>();
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) continue;
			TFVectors.put(buildTFVector(f), f.toPath());
		}

		return TFVectors;
	}

	/** Count words in line into the tf vector */
	private final BiConsumer<String, Vector> countWords = (line, tf) -> {
		if (line == null) line = "";
		char[] data = line.toCharArray();
		StringBuilder wordBuild = new StringBuilder();

		for (int i = 0; i < data.length; ++i) {
			while (i < data.length && !Character.isAlphabetic(data[i]))
				++i;
			while (i < data.length && Character.isAlphabetic(data[i])) {
				wordBuild.append(data[i]);
				++i;
			}

			String word = wordBuild.toString().toLowerCase();
			if (!stopWords.contains(word)) {
				Integer ind = vocabulary.get(word);
				if (ind != null) { 
					tf.setElement(ind, tf.getElement(ind) + 1);
				}
			}
			wordBuild = new StringBuilder();
		}
	};

	/**
	 * Builds tf vector from the provided document
	 * @param doc - document from which tf vector should be built
	 * @return tf vector built from the provided document
	 */
	private Vector buildTFVector(File doc) {
		Vector TF = new Vector(vocabulary.size());

		forEachLineInFile(doc, countWords, TF);
		return TF;
	}

	/**
	 * Helper method invoking provided action on each line of the provided document file
	 * @param f - file to read
	 * @param action - action to invoke on each line of the provided file
	 */
	private void forEachLineInFile(File f, Consumer<String> action) {
		BiConsumer<String, Integer> tmp = (line, num) -> {
			action.accept(line);
		};
		forEachLineInFile(f, tmp, (Integer) null);
	}

	/**
	 * Helper method invoking provided action on provided object and each line of the provided document file
	 * @param f - file to read
	 * @param action - action to invoke
	 * @param object - object
	 */
	private <U> void forEachLineInFile(File f, BiConsumer<String, U> action, U object) {
		try (BufferedReader r = new BufferedReader(new FileReader(f))) {
			for (String line = r.readLine(); line != null; line = r.readLine()) {
				action.accept(line, object);
			}
		} catch (IOException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

}
