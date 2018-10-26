package hr.fer.zemris.java.hw07.shell.namebuilder;

import java.util.Objects;

/**
 * Adds the same text to the transforming file name.
 * @author ltomic
 *
 */
public class NameBuilderText implements NameBuilder {

	/**
	 * Text to be added
	 */
	private final String text;
	
	/**
	 * Constructs {@link NameBuilderText} with provided text as text that this
	 * object is adding to transforming file name.
	 * @param text text used for adding to transforming file name
	 */
	public NameBuilderText(String text) {
		this.text = Objects.requireNonNull(text, "text cannot be null");
	}
	
	@Override
	public void execute(NameBuilderInfo info) {
		info.getStringBuilder().append(text);
	}
	
	@Override
	public String toString() {
		return "Text : " + text;
	}
}
