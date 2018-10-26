package hr.fer.zemris.java.hw07.shell.namebuilder;

/**
 * Modifies the transforming file name by adding a group from the pattern matcher
 * of the original file name.
 * @author ltomic
 *
 */
public class NameBuilderGroup implements NameBuilder {
	
	/**
	 * Index of the group in pattern to be added
	 */
	private final int index;
	/**
	 * Minimal character width to be added
	 */
	private final int width;
	/**
	 * Character to be used to pad the groups that have less than minimal character width
	 */
	private final char repeat;
	
	/**
	 * Constructs a {@link NameBuilderGroup} with given arguments.
	 * @param index index of the group from the pattern to be added to the file name
	 * @param width minimal character width
	 * @param zero character to be used to pad groups that are smaller than minimal character width
	 */
	public NameBuilderGroup(int index, int width, boolean zero) {
		this.index = index;
		this.width = width;
		repeat = zero ? '0' : ' ';
	}

	@Override
	public void execute(NameBuilderInfo info) {
		String text = info.getGroup(index);
		for (int i = 0, len = text.length(); i < width-len; ++i) { 
			info.getStringBuilder().append(repeat);
		}
		
		info.getStringBuilder().append(text);
	}
	
	@Override
	public String toString() {
		return String.format("Group:: index:%d , width:%d, char:%c", index, width, repeat);
	}

}
