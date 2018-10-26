package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class that implements {@link ILocalizationProvider}. It contains the
 * implementation of the observable pattern. Translation providing method getString
 * is the abstract one. 
 * @author ltomic
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

	/** Observer storage **/
	private List<ILocalizationListener> listeners = new ArrayList<>();
	
	@Override
	public void addLocalizationListener(ILocalizationListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener listener) {
		listeners.remove(listener);
	}

	@Override
	abstract public String getString(String key);
	
	/**
	 * Notifies all observer about the localization change(language change)
	 */
	public void fire() {
		listeners.forEach((l) -> l.localizationChanged());
	}
}
