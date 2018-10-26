package hr.fer.zemris.java.hw11.jnotepadpp.local.swing;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProviderBridge;

/**
 * Implements a {@link LocalizationProviderBridge} that has a reference to the application
 * window that is using this {@link LocalizationProvider}. When the window opens, bridge
 * connects to the decorated {@link ILocalizationProvider}, and when window closes it
 * disconnects.
 * @author ltomic
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

	/**
	 * Constructs a {@link FormLocalizationProvider} with provided arguments.
	 * @param provider - decorated {@link ILocalizationProvider}
	 * @param frame - frame that uses this bridge for localization
	 */
	public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
		super(provider);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent event) {
				disconnect();
			}
			
			@Override
			public void windowOpened(WindowEvent arg0) {
				connect();
			}
		});
	}
}
