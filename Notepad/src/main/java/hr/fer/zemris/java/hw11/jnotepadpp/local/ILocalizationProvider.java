package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * This interface provides a translation for a given key. It also implements
 * the observable pattern.
 * @author ltomic
 *
 */
public interface ILocalizationProvider {

	/**
	 * Adds the provided {@link ILocalizationListener}
	 * @param listener - {@link ILocalizationListener} to add
	 */
	public void addLocalizationListener(ILocalizationListener listener);
	/**
	 * Removed the provided {@link ILocalizationListener}
	 * @param listener - {@link ILocalizationListener} to remove
	 */
	public void removeLocalizationListener(ILocalizationListener listener);
	/**
	 * Returns the translation for the given key
	 * @param key - translation key
	 * @return translation for the given key
	 */
	public String getString(String key);
}
