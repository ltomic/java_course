package hr.fer.zemris.java.hw11.jnotepadpp.document;

/**
 * Abstract class that implements {@link MultipleDocumentListener} with empty methods.
 * Created for code shortness.
 * @author ltomic
 *
 */
public abstract class MultipleDocumentAdapter implements MultipleDocumentListener {

	@Override
	public void currentDocumentChanged(SingleDocumentModel previousModel,
			SingleDocumentModel currentModel) {}

	@Override
	public void documentAdded(SingleDocumentModel model) {}

	@Override
	public void documentRemoved(SingleDocumentModel model) {}
}
