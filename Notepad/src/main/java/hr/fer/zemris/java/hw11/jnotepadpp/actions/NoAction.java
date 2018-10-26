package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.swing.LocalizableAction;

/**
 * No commands executed upon calling this action. This action is used by {@link JComponent}s that
 * have constructors supporting {@link Action} and is used for localization purposes.
 * @author ltomic
 *
 */
public class NoAction extends LocalizableAction {

	/** Serial version **/
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a {@link NoAction} with provided localization name key and
	 * {@link ILocalizationProvider}.
	 * @param key - localization name key
	 * @param lp - {@link ILocalizationProvider}
	 */
	public NoAction(String key, ILocalizationProvider lp) {
		this(key, lp, null);
	}

	/**
	 * Constructs a {@link NoAction} with provided localization name key,
	 * {@link ILocalizationProvider} and {@link ImageIcon}.
	 * @param key - localization name key
	 * @param lp - {@link ILocalizationProvider}
	 * @param icon - action icon
	 */
	public NoAction(String key, ILocalizationProvider lp, ImageIcon icon) {
		super(key, lp, icon);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
	}

}
