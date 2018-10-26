package hr.fer.zemris.java.webserver;

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.HttpCookie;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Implements a http server.
 * Server supports serving text files, images and running of SmarcScript files.
 * Server is configured by providing the path to the configuration file to the
 * class constructor
 */
public class SmartHttpServer {

	/** Server address **/
	private String address;
	/** Server domain **/
	private String domainName;
	/** Server port **/
	private int port;
	/** Number of worker threads **/
	private int workerThreads;
	/** Session timeout time **/
	private int sessionTimeout;
	/** Mime types supported **/
	private Map<String, String> mimeTypes = new HashMap<String, String>();
	/** Main server thread **/
	private ServerThread serverThread;
	/** Thread pool for client threads **/
	private ExecutorService threadPool;
	/** Path to root server media folder **/
	private Path documentRoot;
	/** Server scripts **/
	private Map<String, IWebWorker> workersMap;
	/** Stored sessions **/
	private Map<String, SessionMapEntry> sessions = new HashMap<>();
	/** Radnom object provider, used to generate SID **/
	private Random sessionRandom = new Random();

	/** Used to clean expired sessions every 5 min **/
	private Thread expiredSessionCleaner;
	/** Is cleaner working **/
	private volatile boolean cleanerWorking = false;

	/** Expired session cleaner working activation time **/
	private static final long CLEANER_DELAY = 60 * 5;

	/* Keys for configuration values */
	private static final String ADDRESS_KEY = "server.address";
	private static final String DOMAIN_NAME_KEY = "server.domainName";
	private static final String PORT_KEY = "server.port";
	private static final String WORKER_THREADS_KEY = "server.workerThreads";
	private static final String SESSION_TIMEOUT_KEY = "session.timeout";
	private static final String MIME_CONFIG_KEY = "server.mimeConfig";
	private static final String DOCUMENT_ROOT_KEY = "server.documentRoot";
	private static final String WORKER_DOCUMENT_NAME_KEY = "server.workers";
	
	/** Private media folder path **/
	private static final String PRIVATE_FOLDER_PATH = "/private/";
	/** Server configuration file path **/
	private static final String SERVER_CONGIF_PATH = "/config/server.properties";

	/**
	 * Constructs a {@link SmartHttpServer} with provided configuration file
	 * @param configFileName
	 * @throws IOException
	 */
	public SmartHttpServer(String configFileName) throws IOException {
		Properties prop = new Properties();
		Properties mimeProp = new Properties();
		prop.load(new FileReader(configFileName));
		mimeProp.load(new FileReader((String) prop.get(MIME_CONFIG_KEY)));

		address = prop.getProperty(ADDRESS_KEY);
		domainName = prop.getProperty(DOMAIN_NAME_KEY);
		port = Integer.parseInt(prop.getProperty(PORT_KEY));
		workerThreads = Integer.parseInt(prop.getProperty(WORKER_THREADS_KEY));
		sessionTimeout = Integer.parseInt(prop.getProperty(SESSION_TIMEOUT_KEY));
		documentRoot = Paths.get(prop.getProperty(DOCUMENT_ROOT_KEY));
		for (final String name: mimeProp.stringPropertyNames()) {
			mimeTypes.put(name, mimeProp.getProperty(name));
		}

		serverThread = new ServerThread();

		expiredSessionCleaner = new Thread(new Runnable() {
			@Override
			public void run() {
				while (cleanerWorking == true) {
					try {
						Thread.sleep(CLEANER_DELAY * 1000);
					} catch (InterruptedException ignorable) {}
					sessions.entrySet().removeIf(entry -> 
						entry.getValue().validUntil < System.currentTimeMillis() / 1000.0);
				}
			}
		});

		loadWorkers(prop.getProperty(WORKER_DOCUMENT_NAME_KEY));
	}

	/**
	 * Loadsa all configured worker scripts from the provided worker config file
	 * @param workerConfigFile - path to worker config file
	 * @throws IOException
	 */
	private void loadWorkers(String workerConfigFile) throws IOException {
		workersMap = new HashMap<>();
		List<String> lines = Files.readAllLines(Paths.get(workerConfigFile));
		lines.forEach((line) -> {
			if (line.trim().startsWith("#")) return;
			if (line.trim().isEmpty()) return;

			String[] splitted = line.split("=");
			String path = splitted[0].trim();
			String fqcn = splitted[1].trim();
			try {
				Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
				Object newObject = referenceToClass.getDeclaredConstructor().newInstance();
				IWebWorker iww = (IWebWorker) newObject;
				workersMap.put(path, iww);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

	/**
	 * Method called at the beginning of the program. Starts up the server,
	 * Expects one argument : path to server config file
	 * @param args - path to server config file
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		SmartHttpServer server = new SmartHttpServer(args[0]);
		server.start();
	}

	/**
	 * Starts the server
	 */
	protected synchronized void start() {
		if (serverThread.isAlive()) return;
		serverThread.start();
		threadPool = Executors.newFixedThreadPool(workerThreads);
		cleanerWorking = true;
		expiredSessionCleaner.start();

		System.out.println("Server running...");
	}

	/**
	 * Stops the server
	 */
	protected synchronized void stop() {
		serverThread.terminate();
		threadPool.shutdown();
		cleanerWorking = false;
		System.out.println("Server stopped!");
	}

	/**
	 * Thread accepting new connections to the server and initializing new threads
	 * for new connections.
	 * @author ltomic
	 *
	 */
	protected class ServerThread extends Thread {

		/** Is thread running **/
		private volatile boolean running = true;

		/**
		 * Terminates the thread
		 */
		public void terminate() {
			running = false;
		}

		@Override
		public void run() {
			try (ServerSocket serverSocket = new ServerSocket(port)) {
				while (running) {
					Socket client = serverSocket.accept();
					ClientWorker cw = new ClientWorker(client);
					threadPool.submit(cw);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Thread maintaining connection and processing requests from the connected client.
	 * @author ltomic
	 *
	 */
	private class ClientWorker implements Runnable, IDispatcher {
		/** Client socket **/
		private Socket csocket;
		/** Input stream from the client **/
		private PushbackInputStream istream;
		/** Output stream to the client **/
		private OutputStream ostream;
		/** HTTP version **/
		private String version;
		/** Request method **/
		private String method;
		/** Client host **/
		private String host;
		/** Saved parameters **/
		private Map<String, String> params = new HashMap<String, String>();
		/** Saved temporary parameters **/
		private Map<String, String> tempParams = new HashMap<String, String>();
		/** Saved permanent parameters **/
		private Map<String, String> permParams = new HashMap<String, String>();
		/** Output cookies **/
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		/** SID **/
		private String SID;
		/** Context **/
		private RequestContext context;

		/**
		 * Constructs a {@link ClientWorker} with provided connection socket
		 * @param csocket - connection socket
		 */
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * Processes the request
		 */
		@Override
		public void run() {
			try {
				istream = new PushbackInputStream(csocket.getInputStream());
				ostream = csocket.getOutputStream();

				byte[] request = readRequest(istream);
				if (request == null) {
					sendError(ostream, 400, "Bad request");
					return;
				}
				String requestStr = new String(request, StandardCharsets.US_ASCII);
				List<String> headers = extractHeaders(requestStr);
				String[] firstLine = headers.isEmpty() ? null : headers.get(0).split(" ");
				
				if (!checkFirstHeader(firstLine, ostream)) return;
				
				host = extractHost(headers);
				String requestedPath = firstLine[1];
				URL dummyURL = new URL("http", "dummyhost", requestedPath);
				String path = dummyURL.getPath();

				checkSession(headers, host);
				parseParameters(dummyURL.getQuery());
				internalDispatchRequest(path, true);
			} catch (Exception ex) {
				System.out.println("Pogreška: " + ex.getMessage());
			} finally {
				try {
					csocket.close();
				} catch (IOException ignorable) {}
			}
		}

		/** Host header start format **/
		private static final String HOST_HEADER_START = "host:";
		/** Host header format **/
		private final Pattern HOST_HEADER_PATTERN = Pattern.compile("^(.+)(:\\d+)$");
		
		/**
		 * Extracts host name from the headers
		 * @param headers - request headers 
		 * @return host name provided in request headers, if there are no provided uses
		 * 			the domain name provided in server config files
		 */
		private String extractHost(List<String> headers) {
			for (String header: headers) {
				if (!header.toLowerCase().startsWith(HOST_HEADER_START)) continue;
				Matcher m = HOST_HEADER_PATTERN.matcher(header.substring(HOST_HEADER_START.length()));
				if (m.matches()) {
					return m.group(1).trim();
				}
			}
			return domainName;
		}

		/**
		 * Checks if a valid session already exists for this client. If a session
		 * already exists it renews it, else it creates a new session. 
		 * @param headers
		 * @param host
		 */
		private void checkSession(List<String> headers, String host) {
			String sidCandidate = null;

			for (String header : headers) {
				if (!header.startsWith("Cookie:")) continue;
				header = "Set-Cookie:" + header.substring("Cookie:".length());
				List<HttpCookie> cookies = HttpCookie.parse(header);

				for (HttpCookie cookie : cookies) {
					if (cookie.getName().equals("sid")) {
						sidCandidate = cookie.getValue();
					}
				}
			}
			synchronized (SmartHttpServer.this) {
				if (sidCandidate == null) {
					newClient(host);
					return;
				}
				SessionMapEntry session = sessions.get(sidCandidate);
				if (session == null || !host.equals(session.host)) {
					newClient(host);
					return;
				}
				if (session.validUntil < System.currentTimeMillis() / 1000.0) {
					sessions.remove(sidCandidate);
					newClient(host);
					return;
				}
				session.validUntil = System.currentTimeMillis() / 1000 + sessionTimeout;
				this.permParams = session.map;
				checkContextInitialized();
			}
		}
		
		/**
		 * Checks if context is initialized. If not, initializes it.
		 */
		private void checkContextInitialized() {
			if (this.context != null) return;
			this.context = new RequestContext(ostream, params, permParams, outputCookies, tempParams, this);
		}

		/**
		 * Creates a new session associated with provied host 
		 * @param host - host with whom new session should be associated
		 */
		private void newClient(String host) {
			SessionMapEntry newSession = new SessionMapEntry();
			StringBuilder sidBuilder = new StringBuilder();
			
			for (int i = 0; i < 20; ++i) {
				sidBuilder.append((char) (sessionRandom.nextInt(26)) + 'A');
			}
			newSession.host = host;
			newSession.sid = sidBuilder.toString();
			newSession.validUntil = System.currentTimeMillis() / 1000 + sessionTimeout;
			newSession.map = new ConcurrentHashMap<>();

			sessions.put(newSession.sid, newSession);
			checkContextInitialized();
			context.addRCCookie(new RCCookie("sid", newSession.sid, null, host, "/"));
			this.permParams = newSession.map;
			context.setPersistentParameters(permParams);
		}

		/** Workers scripts package name **/
		private static final String WORKER_PACKAGE_NAME = "workers";
		/** Dynamic loaded worker script prefix **/
		private static final String DYNAMIC_SCRIPT_BEGIN = "/ext/";

		/**
		 * Dispatches the request determining is it valid and what content should be returned to
		 * the client
		 * @param urlPath - url path of the request
		 * @param directCall - is request a direct call
		 * @throws Exception
		 */
		public void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
			if (directCall == true && urlPath.startsWith(PRIVATE_FOLDER_PATH)) {
				sendError(ostream, 403, "Forbidden path");
				return;
			}

			if (urlPath.startsWith(DYNAMIC_SCRIPT_BEGIN)) {
				int index = urlPath.lastIndexOf("?");
				if (index == -1) index = urlPath.length();
				String workerName = urlPath.substring(DYNAMIC_SCRIPT_BEGIN.length(), index);
				Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(
						this.getClass().getPackageName() + "." + WORKER_PACKAGE_NAME + "." + workerName);
				Object newObject = referenceToClass.getDeclaredConstructor().newInstance();
				IWebWorker iww = (IWebWorker) newObject;

				checkContextInitialized();
				iww.processRequest(context);
				return;
			}
			Optional<Entry<String, IWebWorker>> worker = workersMap.entrySet().stream()
					.filter(entry -> entry.getKey().equals(urlPath)).findAny();
			if (worker.isPresent()) {
				checkContextInitialized();
				worker.get().getValue().processRequest(context);
				return;
			}

			Path requestedFile = documentRoot.resolve(urlPath.substring(1));

			if (!requestedFile.startsWith(documentRoot)) {
				sendError(ostream, 403, "Forbidden path");
				return;
			}
			if (!Files.isReadable(requestedFile) || !Files.isRegularFile(requestedFile)) {
				sendError(ostream, 404, "File not found");
				return;
			}

			int ind = requestedFile.toString().lastIndexOf(".");
			String fileExtension = ind == -1 ? "" : requestedFile.toString().substring(ind + 1);

			if (fileExtension.equals("smscr")) {
				String script = new String(Files.readAllBytes(requestedFile), StandardCharsets.UTF_8);
				checkContextInitialized();
				context.setTemporaryParameters(tempParams);
				SmartScriptEngine engine = new SmartScriptEngine(
						(new SmartScriptParser(script)).getDocumentNode(), context);
				engine.execute();
				return;
			}
			checkContextInitialized();
			String mimeType = mimeTypes.get(fileExtension);
			if (mimeType == null) mimeType = "application/octet-stream";

			context.setMimeType(mimeType);
			context.setStatusCode(200);
			long size = requestedFile.toFile().length();
			context.setContentLength(size);

			try (InputStream is = Files.newInputStream(requestedFile)) {
				byte[] buf = new byte[1024];
				while (true) {
					int r = is.read(buf);
					if (r < 1) break;
					context.write(buf, 0, r);
				}
			}
		}

		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}

		/**
		 * Parses the provided cookies from the request
		 * @param query - request recived
		 */
		private void parseParameters(String query) {
			if (query == null) return;

			String[] splittedParameters = query.split("&");
			for (String i : splittedParameters) {
				String[] splittedParameter = i.split("=");
				params.put(splittedParameter[0], splittedParameter[1]);
			}
		}
	}

	/**
	 * Represents a session
	 * @author ltomic
	 *
	 */
	private static class SessionMapEntry {
		/** ID key **/
		String sid;
		/** Session host **/
		String host;
		/** Session is valid until this time **/
		long validUntil;
		/** Cookies stored in this session **/
		Map<String, String> map;
	}

	/**
	 * Checks if first header is valid. If it is not valid returns false and sends a error to the
	 * client.
	 * @param firstLine - first header 
	 * @param cos - output stream to client
	 * @return true if valid, else false
	 * @throws IOException
	 */
	private static boolean checkFirstHeader(String[] firstLine, OutputStream cos)
			throws IOException {
		if (firstLine == null || firstLine.length != 3) {
			sendError(cos, 400, "Bad request");
			return false;
		}

		String method = firstLine[0].toUpperCase();
		if (!method.equals("GET")) {
			sendError(cos, 400, "Method Not Allowed");
			return false;
		}

		String version = firstLine[2].toUpperCase();
		if (!version.equals("HTTP/1.1") && !version.equals("HTTP/1.0")) {
			sendError(cos, 400, "HTTP Version Not Supported");
			return false;
		}
		return true;
	}

	/**
	 * Reads the request from the input stream 
	 * @param is - input stream from the client
	 * @return request read as bytes
	 * @throws IOException
	 */
	private static byte[] readRequest(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int state = 0;
		l: while (true) {
			int b = is.read();
			if (b == -1) return null;
			if (b != 13) {
				bos.write(b);
			}
			switch (state) {
			case 0:	if (b == 13) { state = 1; } else if (b == 10) state = 4; break;
			case 1: if (b == 10) { state = 2; } else state = 0; break;
			case 2:	if (b == 13) { state = 3; } else state = 0; break;
			case 3: if (b == 10) { break l; } else state = 0; break;
			case 4: if (b == 10) { break l; } else state = 0; break;
			}
		}
		return bos.toByteArray();
	}

	/**
	 * Extracts headers from the string
	 * @param requestHeader - headers to be parsed
	 * @return list of headers
	 */
	private static List<String> extractHeaders(String requestHeader) {
		List<String> headers = new ArrayList<String>();
		String currentLine = null;
		for (String s : requestHeader.split("\n")) {
			if (s.isEmpty()) break;
			char c = s.charAt(0);
			if (c == 9 || c == 32) {
				currentLine += s;
			} else {
				if (currentLine != null) {
					headers.add(currentLine);
				}
				currentLine = s;
			}
		}
		if (!currentLine.isEmpty()) {
			headers.add(currentLine);
		}
		return headers;
	}

	/**
	 * Sends a error message to the client
	 * @param cos output stream to the client
	 * @param statusCode - error status code 
	 * @param statusText - error status text 
	 * @throws IOException
	 */
	private static void sendError(OutputStream cos, int statusCode, String statusText)
			throws IOException {
		String errorHeader = "HTTP/1.1 " + statusCode + " " + statusText + "\r\n"
				+ "Server: simple java server\r\n" + "Content-Type: text/plain;charset=UTF-8\r\n"
				+ "Content-Length: 0\r\n" + "Connection: close\r\n" + "\r\n";
		cos.write(errorHeader.getBytes(StandardCharsets.US_ASCII));
		cos.flush();
	}

}