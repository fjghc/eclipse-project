package lesson.j2ee.ex2;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

public class BeerAddedListener implements HttpSessionAttributeListener {

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		// TODO Auto-generated method stub
		String name = event.getName();
		Object value = event.getValue();
		System.out.println("The attribute "+name+" is added,the value is "+value);
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		// TODO Auto-generated method stub
		String name = event.getName();
		Object value = event.getValue();
		System.out.println("The attribute "+name+" is removed,the value is "+value);

	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		// TODO Auto-generated method stub
		String name = event.getName();
		Object value = event.getValue();
		System.out.println("The attribute "+name+" is replaced,the value is "+value);

	}

}
