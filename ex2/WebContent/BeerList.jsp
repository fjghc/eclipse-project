<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="beer" class="lesson.j2ee.ex2.Beer" scope="request"/>
<jsp:setProperty property="color" name="beer" value=<% request.getParameter("color"); %>/>
<jsp:useBean id="beer1" class="lesson.j2ee.ex2.Beer" scope="request"/>
<jsp:getProperty property="color" name="beer1" />


<%
	String color=beer1.getColor();
	if(color.equals("amber")){
%>
	<jsp:forward page="ShowAmberBeer.jsp" />
<%	}
	if(color.equals("brown")){
		response.sendRedirect("ShowBrownBeer.jsp");
	}
	if(color.equals("dark")){
	    RequestDispatcher view = request.getRequestDispatcher("ShowDarkBeer.jsp");
	    view.forward(request, response);
	}
%>
<jsp:include page="Header_act.jsp">
	<jsp:param name="subTitle" value="Welcome to the Light Beer World!"/>
</jsp:include>
<%@ include file="DisplayItem.jsp" %>