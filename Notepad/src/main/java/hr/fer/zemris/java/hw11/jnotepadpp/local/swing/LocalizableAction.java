package hr.fer.zemris.java.hw11.jnotepadpp.local.swing;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import hr.fer.zemris.java.hw11.jnotepadpp.Utils;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Implements a {@link AbstractAction} that changes its properties that are affected by the localization.
 * @author ltomic
 *
 */
public abstract class LocalizableAction extends AbstractAction {
	
	/** Serial version **/
	private static final long serialVersionUID = 1L;
	/** Localization name key **/
	private String key;

	/**
	 * Constructs a {@link LocalizableAction} with provided arguments
	 * @param key - localization name key
	 * @param lp - {@link ILocalizationProvider}
	 */
	public LocalizableAction(String key, ILocalizationProvider lp) {
		this(key, lp, null);
	}
	
	/**
	 * Constructs a {@link LocalizableAction} with provided arguments
	 * @param key - localization name key
	 * @param lp - {@link ILocalizationProvider}
	 * @param icon - small icon action
	 */
	public LocalizableAction(String key, ILocalizationProvider lp, ImageIcon icon) {
		super(key, icon);
		this.key = key;
		updateProperties(lp);

		lp.addLocalizationListener(() -> {
			updateProperties(lp);
		});
	}
	
	/**
	 * Method called upon localization change. Updates the properties affected by the localization
	 * change - NAME, MNEMONIC_KEEY
	 * @param lp
	 */
	private void updateProperties(ILocalizationProvider lp) {
		this.putValue(NAME, lp.getString(this.key));
		Character mnemonicKey = lp.getString(this.key + Utils.MNEMONIC_EXTENSION).charAt(0);
		this.putValue(MNEMONIC_KEY, KeyEvent.getExtendedKeyCodeForChar(mnemonicKey));
	}
	
	@Override
	abstract public void actionPerformed(ActionEvent event);

}
