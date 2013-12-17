<%@ page import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="myTags" uri="simpleTags" %>

<html>
<body>
<h1 align="center">Beer Recommendations JSP</h1>
<p>


<myTags:simple1 color="${colorName}"/>

<c:forEach var="it" items="${styles}"> 
	<br>try: ${it} 
</c:forEach>




</body>
</html>