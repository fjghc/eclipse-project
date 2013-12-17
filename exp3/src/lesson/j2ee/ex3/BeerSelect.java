package lesson.j2ee.ex3;


import lesson.j2ee.ex3.model.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class BeerSelect extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {
		String c = request.getParameter("color");
		BeerExpert be = new BeerExpert();
		String rootpath = getServletContext().getRealPath("/");
		List result = be.getBrands(rootpath, c);

		request.setAttribute("styles", result);
		request.setAttribute("colorName", c);
		RequestDispatcher view =
			request.getRequestDispatcher("result.jsp");
		view.forward(request, response);
	}
}
