package hr.fer.zemris.java.webserver;

/**
 * Dispatches web requests. Determines what subtype of request was issued using
 * provided url path and calls appropriate actions.
 * @author ltomic
 *
 */
public interface IDispatcher {
	/**
	 * Dispacthes the request given as url path. 
	 * @param urlPath - request url path
	 * @throws Exception
	 */
	void dispatchRequest(String urlPath) throws Exception;
}
