package hr.fer.zemris.java.gui.calc;

/**
 * Interface that implements a observer strategy on the {@link CalcModel}.
 * @author ltomic
 *
 */
public interface CalcValueListener {
	/**
	 * {@link CalcModel} in which this {@link CalcValueListener} is registered in
	 * calls this function to notify about the change value. This method then
	 * performs the appropriate actions.
	 * @param model - model who called this method
	 */
	void valueChanged(CalcModel model);
}