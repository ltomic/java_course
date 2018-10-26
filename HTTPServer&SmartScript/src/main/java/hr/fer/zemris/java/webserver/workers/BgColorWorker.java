package hr.fer.zemris.java.webserver.workers;

import java.util.function.IntPredicate;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class BgColorWorker implements IWebWorker{
	
	private static final IntPredicate isHexDigit = (digit) ->
		('0' <= digit && digit <= '9') || ('A' <= digit && digit <= 'F');

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String color = context.getParameter("bgcolor");
		
		context.write("<html><body><a href=\"/index2.html\">index2.html</a><br>Color is ");
		if (color.chars().allMatch(isHexDigit)) {
			context.setPersistentParameter("bgcolor", color);
		} else {
			context.write("not ");
		}
		context.write("updated.");
	}

	
}
