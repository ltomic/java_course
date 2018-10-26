package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * A decorator for some {@link ILocalizationProvider}. It moves away the application from the
 * localization provider enabling garbage collector to do its job once the application window is
 * closed. This "bridge" is used to resolve a request for translations and notify listeners about
 * the localization changes. When application window needs the translations it creates a
 * {@link LocalizationProviderBridge} and connects it to the decorated {@link ILocalizationProvider}.
 * Once the application window starts closing bridge disconnects from the {@link ILocalizationProvider}.
 * @author ltomic
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	/** Decorated {@link ILocalizationProvider} */
	private ILocalizationProvider parent;
	/**
	 * {@link ILocalizationListener} that informs all its listeners about the localization change
	 */
	private final ILocalizationListener listener = () -> {
		fire();
	};
	/** Connection status */
	private boolean connected;

	/**
	 * Construct a {@link LocalizationProviderBridge} with provided {@link ILocalizationProvider}.
	 * @param lp - decorated {@link ILocalizationProvider}
	 */
	public LocalizationProviderBridge(ILocalizationProvider lp) {
		parent = lp;
	}

	@Override
	public String getString(String key) {
		return parent.getString(key);
	}

	/**
	 * Connects to the {@link ILocalizationProvider}
	 */
	public void connect() {
		if (connected) return;
		parent.addLocalizationListener(listener);
		connected = true;
	}

	/**
	 * Disconnects from the {@link ILocalizationProvider}
	 */
	public void disconnect() {
		parent.removeLocalizationListener(listener);
		connected = false;
	}

}
