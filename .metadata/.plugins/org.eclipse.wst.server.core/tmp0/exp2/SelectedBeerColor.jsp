<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!-- Created by Song Zehui -->
<title> </title>
</head>
<body>

<jsp:useBean id="beer" class="lesson.j2ee.ex2.Beer" scope="session"/>


<%

Integer colorCount=(Integer)session.getAttribute("colorCount");
//如果session是新的
if (colorCount==null)
	colorCount=new Integer(0);
//接收传来的参数
		String color;
		color=beer.getColor();

		colorCount=new Integer(colorCount.intValue()+1);
		session.setAttribute("colorCount",colorCount);
		session.setAttribute("color"+colorCount, color);
		System.out.println(colorCount);


%>
<h4>Session List:</h4><hr><br><br>
The color you have selected:<br>

<% for(int i=1;i<=colorCount;i++){
	out.println((String)session.getAttribute("color"+i)+"<hr>");
} %>

</body>
</html>