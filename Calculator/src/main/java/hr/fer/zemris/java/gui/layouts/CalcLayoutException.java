package hr.fer.zemris.java.gui.layouts;

/**
 * Exception thrown by {@link CalcLayout}.
 * @author ltomic
 *
 */
@SuppressWarnings("javadoc")
public class CalcLayoutException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CalcLayoutException() {
	}
	
	public CalcLayoutException(String message) {
		super(message);
	}
	
	public CalcLayoutException(Throwable cause) {
		super(cause);
	}
	
	public CalcLayoutException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
