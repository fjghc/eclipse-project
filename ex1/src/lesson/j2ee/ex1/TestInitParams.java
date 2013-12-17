package lesson.j2ee.ex1;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestInitParams extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.print("Here are servlet context params:<br>");
		ServletContext sc = this.getServletContext();
		String name = sc.getInitParameter("name");
		String id = sc.getInitParameter("id");
		out.print("my name is: "+name+"<br>");
		out.print("my student id is: "+id+"<br>");
		out.print("Here is servlet config param:<br>");
		ServletConfig config =  this.getServletConfig();
		String myemail = config.getInitParameter("myEmail");
		out.print("my email is: "+myemail+"<br>");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}
	
	

}
