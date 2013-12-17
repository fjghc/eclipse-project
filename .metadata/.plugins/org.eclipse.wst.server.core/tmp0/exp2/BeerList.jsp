<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">



<jsp:useBean id="beer" class="lesson.j2ee.ex2.Beer" scope="session">
</jsp:useBean>
<jsp:setProperty  name="beer" property="color" value="<%= request.getParameter("color") %>"/>

<%
	String color= beer.getColor();
	if(color.equals("amber")){
%>
		<jsp:forward page="ShowAmberBeer.jsp" />
<%	}
	if(color.equals("brown")){
%>
		<jsp:forward page="ShowBrownBeer.jsp" />
<% 
	}
	if(color.equals("dark")){
	%>
		<jsp:forward page="ShowDarkBeer.jsp" />
	<% 
	}
%>


<jsp:include page="Header_act.jsp">
	<jsp:param name="subTitle" value="Welcome to the Light Beer World!"/>
</jsp:include>
<jsp:include page="SelectedBeerColor.jsp"/>
<%@ include file="DisplayItem.jsp" %>