package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class provides the translations for the required localization. It implements
 * a Singleton pattern that is only one instance of this class exists. Provider can
 * change the localization it uses for translations.
 * @author ltomic
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider {
	
	/** Location of the resource bundle **/
	private final String bundlePath;
	/** Prefix of the localization files **/
	private static final String TRANSLATION_PREFIX = "translation";

	/** Current language **/
	private String language;
	/** Current resource bundle **/
	private ResourceBundle bundle;
	
	/** The only instace of this class **/
	private static final LocalizationProvider instance = new LocalizationProvider();
	
	/**
	 * Private constructor of this class, initializes the language to english and aquires the
	 * resource bundle.
	 */
	private LocalizationProvider() {
		bundlePath = getClass().getPackageName() + "." + TRANSLATION_PREFIX;
		language = new String("en");
		bundle = ResourceBundle.getBundle(bundlePath, Locale.forLanguageTag(language));
	}
	
	/**
	 * Returns the instance of this class.
	 * @return the instance of this class
	 */
	public static LocalizationProvider getInstance() {
		return instance;
	}
	
	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}
	
	/**
	 * Sets the language of the provider
	 * @param newLanguage - new language to be set
	 */
	public void setLanguage(String newLanguage) {
		language = newLanguage;
		bundle = ResourceBundle.getBundle(bundlePath, Locale.forLanguageTag(language));
		fire();
	}
}
