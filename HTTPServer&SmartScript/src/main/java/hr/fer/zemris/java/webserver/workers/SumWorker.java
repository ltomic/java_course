package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class SumWorker implements IWebWorker {
	
	private static final String CALC_SCRIPT_PATH = "/private/calc.smscr";

	@Override
	public void processRequest(RequestContext context) throws Exception {
		Integer a;
		Integer b;
		
		a = parseInt(context.getParameter("a"));
		b = parseInt(context.getParameter("b"));
		
		if (a == null) a = 1;
		if (b == null) b = 2;
		
		context.setTemporaryParameter("zbroj", String.valueOf(a + b));
		context.setTemporaryParameter("a", a.toString());
		context.setTemporaryParameter("b", b.toString());
		context.getDispatcher().dispatchRequest(CALC_SCRIPT_PATH);
	}

	private static Integer parseInt(String num) {
		try {
			return Integer.parseInt(num);
		} catch (NumberFormatException ex) {
			return null;
		}
	}
	
}
