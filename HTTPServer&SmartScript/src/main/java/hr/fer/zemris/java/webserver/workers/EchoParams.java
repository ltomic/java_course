package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class EchoParams implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		context.setMimeType("text/html");

		context.write("<html><body><table><thead><tr><th>Name</th><th>Value</th></tr></thead><tbody>");
		context.getParameters().forEach((name, value) -> {
			try {
				context.write("<tr><td>" + name + "</td><td>" + value + "</tr>");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
		context.write("</tbody></table></body></html>");
	}

}
