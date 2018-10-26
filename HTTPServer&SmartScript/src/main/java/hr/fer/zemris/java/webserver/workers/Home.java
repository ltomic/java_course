package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class Home implements IWebWorker {

	private static final String DEFAULT_BGCOLOR = "7F7F7F";
	private static final String HOME_SCRIPT_PATH = "/private/home.smscr";
	
	@Override
	public void processRequest(RequestContext context) throws Exception {
		String color = context.getPersistentParameter("bgcolor");
		if (color == null) color = DEFAULT_BGCOLOR;
		context.setTemporaryParameter("background", color);
		
		context.getDispatcher().dispatchRequest(HOME_SCRIPT_PATH);
	}

}
