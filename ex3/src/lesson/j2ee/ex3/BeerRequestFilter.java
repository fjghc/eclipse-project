package lesson.j2ee.ex3;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

public class BeerRequestFilter implements Filter{
	
	private FilterConfig fc;
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		HttpServletRequest httpReq = (HttpServletRequest) req;
		String colorName = httpReq.getParameter("color");
		
		if (!(colorName.equals("light") || colorName.equals("amber") 
				|| colorName.equals("brown") || colorName.equals("dark") ))
		{
//			fc.getServletContext().log("The " + colorName + " beer is not exist!");
			PrintWriter out = resp.getWriter();
			out.println("<html>" + "<body>" + "<h1>" + "The " + colorName + " beer is not exist!"
							+ "<h1 align=center>" + "</body>" + "</html>");
			
			
		}
		else
		{
			chain.doFilter(req, resp);
		}
		
		
	}
	@Override
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub
		this.fc = config;
	}

}
