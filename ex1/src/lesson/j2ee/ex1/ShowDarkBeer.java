package lesson.j2ee.ex1;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class ShowDarkBeer extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws IOException, ServletException { 
		String colorParam = request.getParameter("color"); 
		PrintWriter out = response.getWriter();
		out.println("<html> " + "<body>"
				+ "<h1 align=center>Dark Page:Beer Color</h1>" + "<br>"
				+ "My selected beer color is: " + colorParam + "</body>" + "</html>");
	}
}
