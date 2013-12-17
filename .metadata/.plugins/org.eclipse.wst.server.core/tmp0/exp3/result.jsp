<%@ page import="java.util.*"%>
<%@ taglib prefix="myTags" uri="simpleTags" %>
<html>
<body>
<h1 align="center">Beer Recommendations JSP</h1>
<myTags:simple1 color="${colorName}"/>
<p>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:forEach var="style" items="${styles}"  varStatus="styleLoopCount">
<br>try:${style} 
</c:forEach>

</body>
</html>