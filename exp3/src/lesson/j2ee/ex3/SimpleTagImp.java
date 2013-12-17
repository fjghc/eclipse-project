package lesson.j2ee.ex3;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class SimpleTagImp extends SimpleTagSupport {

	private String color;
	@Override
	public void doTag() throws JspException, IOException {
		getJspContext().getOut().print("Beer expert recommends " + this.color + " beers: " );
	}
	
	public void setColor(String color)
	{
		this.color = color;
	}

}
