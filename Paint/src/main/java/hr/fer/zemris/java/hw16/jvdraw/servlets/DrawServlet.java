package hr.fer.zemris.java.hw16.jvdraw.servlets;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw16.jvdraw.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.DrawingModelImpl;
import hr.fer.zemris.java.hw16.jvdraw.Util;

@WebServlet("/crtaj")
public class DrawServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String text = req.getParameter("doc");
		String[] splitted = text.split("\\n");
		
		DrawingModel drawingModel = new DrawingModelImpl();
		for (int i = 0, sz = splitted.length; i < sz; ++i) {
			try {
				drawingModel.add(Util.parseStringLine(null, splitted[i]));
			} catch (Exception ex) {
				req.getRequestDispatcher("/WEB-INF/pages/error.html").forward(req, resp);
			}
		}
		
		BufferedImage image = Util.generateImage(drawingModel);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		byte[] imgBytes = baos.toByteArray();
		
		resp.getOutputStream().write(imgBytes);	
		resp.getOutputStream().flush();
	
	}

}
