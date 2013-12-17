package lesson.j2ee.ex3;
import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
public class SimpleTag  extends SimpleTagSupport{

	private String color;
	@Override
	public void doTag() throws JspException, IOException {
		// TODO Auto-generated method stub
		getJspContext().getOut().print("Beer expert recommends " + color + " beers: " );
	}
	
	public void setColor(String color)
	{
		this.color = color;
	}
	
}
