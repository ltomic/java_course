package hr.fer.zemris.java.webserver;

/**
 * Processes a certain type of request creating content for the client.
 * @author ltomic
 *
 */
public interface IWebWorker {

	/**
	 * Processes the request it represents in the provided {@link RequestContext}.
	 * @param context - context to use
	 * @throws Exception
	 */
	public void processRequest(RequestContext context) throws Exception;
}
