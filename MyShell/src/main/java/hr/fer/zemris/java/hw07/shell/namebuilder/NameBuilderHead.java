package hr.fer.zemris.java.hw07.shell.namebuilder;

/**
 * Holds all {@link NameBuilder} used to generate a transformed file name.
 * When executed it calls execute method on all {@link NameBuilder} it holds
 * in the order they were given to this object.
 * @author ltomic
 *
 */
public class NameBuilderHead implements NameBuilder {

	/**
	 * Saved {@link NameBuilder}s
	 */
	private NameBuilder[] builders;
	
	/**
	 * Constructs {@link NameBuilderHead} with provided {@link NameBuilder}s.
	 * @param builders {@link NameBuilder}s to save
	 */
	public NameBuilderHead(NameBuilder ...builders) {
		this.builders = builders;
	}
	
	@Override
	public void execute(NameBuilderInfo info) {
		for (NameBuilder i : builders) {
			i.execute(info);
		}
	}
	
	/**
	 * Returns array {@link NameBuilder}s this object has saved.
	 * @return
	 */
	public NameBuilder[] getBuilders() {
		return builders;
	}

}
