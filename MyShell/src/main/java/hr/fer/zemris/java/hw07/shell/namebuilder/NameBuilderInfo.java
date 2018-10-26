package hr.fer.zemris.java.hw07.shell.namebuilder;

/**
 * Hold information about the to be transformed file name that is
 * in the process of transforming. This object is provided to {@link NameBuilder}
 * to generate the transformed file name. It also holds the information
 * about the captured groups that were matched by pattern which filtered
 * the original file name. 
 * @author ltomic
 *
 */
public interface NameBuilderInfo {

	/**
	 * Returns the current state of transformed file name.
	 * @return the current state of transformed file name.
	 */
	StringBuilder getStringBuilder();
	
	/**
	 * Returns the group matched by pattern whose index is the provided one.
	 * @param index index of group to be returned
	 * @return  the group matched by pattern whose index is the provided one.
	 */
	String getGroup(int index);
}
