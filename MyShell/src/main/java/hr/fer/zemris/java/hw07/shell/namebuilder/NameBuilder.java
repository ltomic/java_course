package hr.fer.zemris.java.hw07.shell.namebuilder;

/**
 * Used to generate parts of transformed names in massrename command.
 * @author ltomic
 *
 */
public interface NameBuilder {

	/**
	 * Modifies the part of given {@link NameBuilderInfo} {@link StringBuilder}
	 * based on the type of {@link NameBuilder} and information contained in given info
	 * @param info contains information used to modify its string
	 */
	void execute(NameBuilderInfo info);
}
