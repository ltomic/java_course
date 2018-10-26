package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Observer of the {@link ILocalizationProvider} that observer localization change.
 * @author ltomic
 *
 */
public interface ILocalizationListener {

	/**
	 * Called upon localization change.
	 */
	public void localizationChanged();
}
