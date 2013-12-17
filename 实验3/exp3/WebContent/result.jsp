<%@ page import="java.util.*"%>
<html>
<body>
<h1 align="center">Beer Recommendations JSP</h1>
<p>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:forEach var="style" items="${styles}"  varStatus="styleLoopCount">
<br>try:${style} 
</c:forEach>

</body>
</html>