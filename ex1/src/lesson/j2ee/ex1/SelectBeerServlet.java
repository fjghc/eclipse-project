package lesson.j2ee.ex1;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class SelectBeerServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws IOException, ServletException { 
		String colorParam = request.getParameter("color"); 
		if(colorParam.equals("brown"))
		{
			RequestDispatcher dp = request.getRequestDispatcher("/Brown");
			dp.forward(request, response);
		}
		else if(colorParam.equals("dark"))	
		{
			RequestDispatcher dp = request.getRequestDispatcher("/Dark");
			dp.forward(request, response);
		}
		else
		{
			PrintWriter out = response.getWriter();
			out.println("<html> " + "<body>"
					+ "<h1 align=center>Beer Color</h1>" + "<br>"
					+ "My selected beer color is: " + colorParam + "</body>" + "</html>");
		}
	
	}
}
