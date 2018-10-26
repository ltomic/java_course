package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.swing.LocalizableAction;

/**
 * Changes the language of the {@link LocalizationProvider} instance to the language
 * of this action.
 * @author ltomic
 *
 */
public class ChangeLanguageAction extends LocalizableAction {
	
	/** Serial version **/
	private static final long serialVersionUID = 1L;
	/** This action language **/
	private String language;

	/**
	 * Constructs a {@link ChangeLanguageAction} with given localization key, {@link ILocalizationProvider}
	 * and language this action should set the {@link LocalizationProvider} instance to.
	 * @param key - localization key
	 * @param lp - localization provider
	 * @param language - language of this action
	 */
	public ChangeLanguageAction(String key, ILocalizationProvider lp, String language) {
		super(key, lp);
		this.language = language;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		LocalizationProvider.getInstance().setLanguage(language);
	}

}
