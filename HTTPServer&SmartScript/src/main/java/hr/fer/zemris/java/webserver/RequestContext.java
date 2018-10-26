package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Holds information about the response it provides to the request issuer. Generates
 * the appropriate header describing the message it will transmit to the receiver, but
 * does not generate the message itself.
 * @author ltomic
 *
 */
public class RequestContext {

	/** Message issued when it is tried to change the properties defining the header
	 * of the message when the header is already generated */
	private static final String HEADER_ALREADY_GENERATED_MESSAGE = "Cannot change header properties. Header already generated";

	/** Output stream to the request issuer **/
	private OutputStream outputStream;
	/** Charset used **/
	private Charset charset;

	/** Message encoding **/
	private String encoding = "UTF-8";
	/** Message status code **/
	private int statusCode = 200;
	/** Message status text **/
	private String statusText = "OK";
	/** Message mime type **/
	private String mimeType = "text/html";
	/** Message length **/
	private Long contentLength;
	/** Dispatcher issuing the message **/
	private IDispatcher dispatcher;

	/** Header generated flag **/
	private boolean headerGenerated = false;

	/** Properties saved **/
	private Map<String, String> parameters = new HashMap<>();
	/** Temporary properties **/
	private Map<String, String> temporaryParameters = new HashMap<>();
	/** Persistent properties **/
	private Map<String, String> persistentParameters = new HashMap<>();
	/** Cookies to output **/
	private List<RCCookie> outputCookies = new ArrayList<>();

	/**
	 * Generates {@link RequestContext} with provided arguments and temporary parameters
	 * and dispacther set to null
	 * @param outputStream - provided outputStream
	 * @param parameters - properties to save
	 * @param persistentParameters - persistent properties
	 * @param outputCookies - cookies to output
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
		this(outputStream, parameters, persistentParameters, outputCookies, null, null);
	}

	/**
	 * Generates {@link RequestContext} with provided arguments 
	 * @param outputStream - provided outputStream
	 * @param parameters - properties to save
	 * @param persistentParameters - persistent properties
	 * @param outputCookies - cookies to output
	 * @param temporaryParameters - temporary properties
	 * @param dispacther - dispatcher issuing the messsage
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies,
			Map<String, String> temporaryParameters, IDispatcher dispacther) {
		Objects.requireNonNull(outputStream);
		this.outputStream = outputStream;
		if (parameters != null) this.parameters = parameters;
		if (persistentParameters != null) this.persistentParameters = persistentParameters;
		if (outputCookies != null) this.outputCookies = outputCookies;

		parameters = Collections.unmodifiableMap(parameters);
		
		if (temporaryParameters != null) this.temporaryParameters = temporaryParameters;
		this.dispatcher = dispacther;
	}

	/**
	 * Writes the provided bytes to the output stream
	 * @param data - data to write
	 * @return this request context
	 * @throws IOException
	 */
	public RequestContext write(byte[] data) throws IOException {
		if (!headerGenerated) generateHeader();
		outputStream.write(data);
		outputStream.flush();

		return this;
	}

	/**
	 * Writes the provided text to the output stream
	 * @param text - text to write
	 * @return this request context
	 * @throws IOException
	 */
	public RequestContext write(String text) throws IOException {
		if (!headerGenerated) generateHeader();
		outputStream.write(text.getBytes(charset));
		outputStream.flush();

		return this;
	}

	/**
	 * Writes the provided bytes to the output stream with offset in the bytes array
	 * and provided length to write
	 * @param data - array of bytes used for output
	 * @param offset - the start offset in data
	 * @param len - number of bytes to write
	 * @return this context
	 * @throws IOException
	 */
	public RequestContext write(byte[] data, int offset, int len) throws IOException {
		if (!headerGenerated) generateHeader();
		outputStream.write(data, offset, len);
		outputStream.flush();

		return this;
	}

	/** Header start format **/
	private static final String HEADER_START_FORMAT = "HTTP/1.1 %s %s\r\n"
			+ "Content-Type: %s%s\r\n";
	/** Cookie format in header **/
	private static final String HEADER_COOKIE_FORMAT = "Set-Cookie: %s%s%s%s";
	/** Format of the property in the cookie **/
	private static final String HEADER_COOKIE_PART_FORMAT = "%s=%s; ";

	/** Charset used in the header **/
	private static final Charset HEADER_CHARSET = StandardCharsets.ISO_8859_1;

	/**
	 * Generates and outputs the header
	 * @throws IOException
	 */
	private void generateHeader() throws IOException {
		headerGenerated = true;
		charset = Charset.forName(encoding);
		
		StringBuilder headerBuilder = new StringBuilder(
				String.format(HEADER_START_FORMAT, statusCode, statusText, mimeType,
						mimeType.startsWith("text/") ? ("; charset=" + encoding) : ""));

		if (contentLength != null) { headerBuilder.append("Content-Length: " + contentLength); }

		outputCookies.forEach((cookie) -> {
			headerBuilder.append(generateHeaderCookie(cookie));
			if (cookie.name.equals("sid")) headerBuilder.append("HttpOnly  ");
			headerBuilder.deleteCharAt(headerBuilder.length() - 1);
			headerBuilder.deleteCharAt(headerBuilder.length() - 1);
			headerBuilder.append("\r\n");
		});
		headerBuilder.append("\r\n");

		outputStream.write(headerBuilder.toString().getBytes(HEADER_CHARSET));
	}

	/**
	 * Generates string representing the cookie in the header
	 * @param cookie - cookie whose string representation should be generated
	 * @return string representation of the provided cookie
	 */
	private String generateHeaderCookie(RCCookie cookie) {
		return String.format(HEADER_COOKIE_FORMAT,
				generateHeaderCookiePart(cookie.getName(), "\"" + cookie.getValue() + "\""),
				generateHeaderCookiePart("Domain", cookie.getDomain()),
				generateHeaderCookiePart("Path", cookie.getPath()),
				generateHeaderCookiePart("Max-Age", cookie.getMaxAge()));
	}

	/**
	 * Generates string representation of a single property in the cookie
	 * @param name - name of the property
	 * @param value - value of the property
	 * @return string representation of a single property in the cookie
	 */
	private String generateHeaderCookiePart(String name, Object value) {
		if (value == null) return "";
		return String.format(HEADER_COOKIE_PART_FORMAT, name, value);
	}

	/**
	 * Gets value of the property by name
	 * @param name - property whose value should be fetched
	 * @return value of the property with provided name
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}

	/**
	 * Gets all properties names
	 * @return all properties
	 */
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}

	/**
	 * Gets value of the persistent property by name
	 * @param name - persistent property whose value should be fetched
	 * @return value of the persistent property with provided name
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}

	/**
	 * Gets all persistent properties names
	 * @return all persistent properties
	 */
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}

	/**
	 * Sets a persistent property with provided name and value
	 * @param name - name of a new persistent property
	 * @param value - value of a new persistent property
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}

	/**
	 * Removes a persistent property with provided name
	 * @param name - name of the persistent property to remove
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}

	/**
	 * Gets value of the temporary property by name
	 * @param name - temporary property whose value should be fetched
	 * @return value of the temporary property with provided name
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}

	/**
	 * Gets all temporary properties names
	 * @return all temporary properties
	 */
	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}

	/**
	 * Sets a temporary property with provided name and value
	 * @param name - name of a new temporary property
	 * @param value - value of a new temporary property
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}

	/**
	 * Removes a temporary property with provided name
	 * @param name - name of the temporary property to remove
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}

	/**
	 * Gets output stream
	 * @return output stream
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * Sets output stream
	 * @param outputStream - new output stream
	 */
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	/**
	 * Gets charset
	 * @return charset
	 */
	public Charset getCharset() {
		return charset;
	}

	/**
	 * Sets charset
	 * @param charset - new charset
	 */
	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	/**
	 * Sets encoding.
	 * @param encoding - new encoding
	 * @throws RuntimeException if header is already generated
	 */
	public void setEncoding(String encoding) {
		if (headerGenerated) throw new RuntimeException(HEADER_ALREADY_GENERATED_MESSAGE);
		this.encoding = encoding;
	}

	/**
	 * Sets status code
	 * @param statusCode - new status code
	 * @throws RuntimeException if header is already generated
	 */
	public void setStatusCode(int statusCode) {
		if (headerGenerated) throw new RuntimeException(HEADER_ALREADY_GENERATED_MESSAGE);
		this.statusCode = statusCode;
	}

	/**
	 * Sets status text
	 * @param statusText - new status text
	 * @throws RuntimeException if header is already generated
	 */
	public void setStatusText(String statusText) {
		if (headerGenerated) throw new RuntimeException(HEADER_ALREADY_GENERATED_MESSAGE);
		this.statusText = statusText;
	}

	/**
	 * Sets mime type
	 * @param mimeType - new mime type
	 * @throws RuntimeException if header is already generated
	 */
	public void setMimeType(String mimeType) {
		if (headerGenerated) throw new RuntimeException(HEADER_ALREADY_GENERATED_MESSAGE);
		this.mimeType = mimeType;
	}
	
	/**
	 * Gets dispatcher
	 * @return dispatcher
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}

	/**
	 * Sets content length
	 * @param contentLength - new content length
	 * @throws RuntimeException if header is already generated
	 */
	public void setContentLength(Long contentLength) {
		if (headerGenerated) throw new RuntimeException(HEADER_ALREADY_GENERATED_MESSAGE);
		this.contentLength = contentLength;
	}

	/**
	 * Gets parameters map
	 * @return parameters map
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

	/**
	 * Sets parameters map
	 * @param parameters - new parameters map
	 */
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	/**
	 * Gets temporary parameters map
	 * @return temporary parameters map
	 */
	public Map<String, String> getTemporaryParameters() {
		return temporaryParameters;
	}

	/**
	 * Sets temporary parameters map
	 * @param temporaryParameters - new temporary parameters map
	 */
	public void setTemporaryParameters(Map<String, String> temporaryParameters) {
		this.temporaryParameters = temporaryParameters;
	}

	/**
	 * Gets persistent parameters map
	 * @return persistent parameters map
	 */
	public Map<String, String> getPersistentParameters() {
		return persistentParameters;
	}

	/**
	 * Sets persistent parameters map
	 * @param persistentParameters - new persistent parameters map
	 */
	public void setPersistentParameters(Map<String, String> persistentParameters) {
		this.persistentParameters = persistentParameters;
	}

	/**
	 * Gets list of output cookies
	 * @return list of output cookies
	 */
	public List<RCCookie> getOutputCookies() {
		return outputCookies;
	}

	/**
	 * Sets a new list of output cookies
	 * @param outputCookies - new list of output cookies
	 * @throws RuntimeException if header is already generated
	 */
	public void setOutputCookies(List<RCCookie> outputCookies) {
		if (headerGenerated) throw new RuntimeException(HEADER_ALREADY_GENERATED_MESSAGE);
		this.outputCookies = outputCookies;
	}

	/**
	 * Class represening a cookies. 
	 * @author ltomic
	 *
	 */
	public static class RCCookie {

		/** Cookie name **/
		private String name;
		/** Cookies value **/
		private String value;
		/** Cookie domain **/
		private String domain;
		/** Cookie path **/
		private String path;
		/** Cookie age **/
		private Integer maxAge;

		/**
		 * Constructs a {@link RCCookie} with provided parameters
		 * @param name
		 * @param value
		 * @param maxAge
		 * @param domain
		 * @param path
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			super();
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		/**
		 * Gets cookie name
		 * @return cookie name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Gets cookie value
		 * @return cookie value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Gets cookie domain
		 * @return cookie domain
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * Gets cookie path
		 * @return cookie path
		 */
		public String getPath() {
			return path;
		}

		/**
		 * Gets cookie age
		 * @return cookie age
		 */
		public Integer getMaxAge() {
			return maxAge;
		}

	}

	/**
	 * Adds a provided cookie to the output cookie list
	 * @param rcCookie - cookie to be added
	 */
	public void addRCCookie(RCCookie rcCookie) {
		outputCookies.add(rcCookie);
	}

}
