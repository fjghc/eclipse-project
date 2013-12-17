<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="nd" beanName="org.jbit.news.dao.impl.NewsDaoImpl" type="org.jbit.news.dao.NewsDao" />
<jsp:useBean id="nbl" beanName="org.jbit.news.biz.impl.NewsBizImpl" type="org.jbit.news.biz.NewsBiz" />
<jsp:setProperty property="nd" name="nbl" value="<%=nd %>"/>
<div class="sidebar">
    <h1> <img src="Images/title_1.gif" alt="国内新闻" /> </h1>
    <div class="side_list">
      <ul>
      	<%
      		List news_in_topic = nbl.findNewsByPage(0,0,"1");
      		request.setAttribute("news_in_topic", news_in_topic);
      	%>
      	<c:forEach var="news" items="${news_in_topic}" begin="0" end="2">
     		<li> <a href='news_read.jsp?nid=${news.nid }'><b> ${news.ntitle }</b></a> </li>
      	</c:forEach>
      </ul>
    </div>
    <h1> <img src="Images/title_2.gif" alt="国际新闻" /> </h1>
    <div class="side_list">
      <ul>
      	<%
      		news_in_topic = nbl.findNewsByPage(0,0,"2");
      		request.setAttribute("news_in_topic", news_in_topic);
      	%>
      	<c:forEach var="news" items="${news_in_topic}" begin="0" end="2">
     		<li> <a href='news_read.jsp?nid=${news.nid }'><b> ${news.ntitle }</b></a> </li>
      	</c:forEach>
      </ul>
    </div>
    <h1> <img src="Images/title_3.gif" alt="娱乐新闻" /> </h1>
    <div class="side_list">
      <ul>
      	<%
      		news_in_topic = nbl.findNewsByPage(0,0,"5");
      		request.setAttribute("news_in_topic", news_in_topic);
      	%>
      	<c:forEach var="news" items="${news_in_topic}" begin="0" end="2">
     		<li> <a href='news_read.jsp?nid=${news.nid }'><b> ${news.ntitle }</b></a> </li>
      	</c:forEach>
      </ul>
    </div>
  </div>
<%
	request.removeAttribute("news_in_topic");
%>
