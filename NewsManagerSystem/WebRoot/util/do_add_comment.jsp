<%@ page language="java" import="java.util.*,java.sql.*" pageEncoding="gbk"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@page import="java.text.SimpleDateFormat"%>
<jsp:useBean id="cd" beanName="org.jbit.news.dao.impl.CommentsDaoImpl" type="org.jbit.news.dao.CommentsDao" />
<jsp:useBean id="cbl" beanName="org.jbit.news.biz.impl.CommentsBizImpl" type="org.jbit.news.biz.CommentsBiz" />
<jsp:setProperty property="cd" name="cbl" value="<%=cd %>"/>
<%
	request.setCharacterEncoding("gbk");
	String cauthor = request.getParameter("cauthor");
	String cnid = request.getParameter("nid");
	String cip = request.getParameter("cip");
	String ccontent = request.getParameter("ccontent");
	java.util.Date date = new java.util.Date();
	Timestamp cdate = new Timestamp(date.getTime());
	Map comment  = new HashMap();
	comment.put("cnid",cnid);
	comment.put("ccontent",ccontent);
	comment.put("cdate",cdate);
	comment.put("cip",cip);
	comment.put("cauthor",cauthor);	
	int i = cbl.addComment(comment);
	request.setAttribute("isAddComment", i);
%>
<c:if test="${isAddComment > 0}">
	<script type="text/javascript">
		alert("已经成功添加评论，点击确认返回原来页面");
		open("../news_read.jsp?nid=<%=cnid%>","_self");	
	</script>
</c:if>
<c:if test="${isAddComment <= 0}">
	<script type="text/javascript">
		alert("添加新闻失败！请联系管理员查找原因！点击确认返回原来页面");
		open("../news_read.jsp?nid=<%=cnid%>","_self");	
	</script>
</c:if>
<%request.removeAttribute("isAddComment"); %>
