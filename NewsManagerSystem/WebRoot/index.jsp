<%@ page language="java" import="java.util.*,java.sql.*,org.jbit.news.bean.*" pageEncoding="gbk" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<jsp:include page="index-elements/index_top.jsp"></jsp:include>
<jsp:useBean id="nd" beanName="org.jbit.news.dao.impl.NewsDaoImpl" type="org.jbit.news.dao.NewsDao" />
<jsp:useBean id="nbl" beanName="org.jbit.news.biz.impl.NewsBizImpl" type="org.jbit.news.biz.NewsBiz" />
<jsp:setProperty property="nd" name="nbl" value="<%=nd %>"/>
<jsp:useBean id="td" beanName="org.jbit.news.dao.impl.TopicsDaoImpl" type="org.jbit.news.dao.TopicsDao" />
<jsp:useBean id="tbl" beanName="org.jbit.news.biz.impl.TopicBizImpl" type="org.jbit.news.biz.TopicBiz" />
<jsp:setProperty property="td" name="tbl" value="<%=td %>"/>
<%
	//设置分页各个参数
	int total_news = nbl.countNews();
	int page_news = 15;
	int total_page = total_news/page_news + 1;
	String page_no_str = request.getParameter("page_no");
	int page_no = 1;
	if(page_no_str != null)
		page_no = Integer.valueOf(page_no_str);
	request.setAttribute("page_no", page_no);
	request.setAttribute("total_page", total_page);
%>
<%
	String tid = request.getParameter("tid");
	List news_list = nbl.findNewsByPage(page_news,page_no,tid);
	List topic_list = tbl.findTopicsByPage(0,0);
	//控制主题排放数量
	int column = 1;
	request.setAttribute("news_list", news_list);
	request.setAttribute("topic_list", topic_list);		
	request.setAttribute("column", column);
%>
<div id="container">
<jsp:include page="index-elements/index_sidebar.jsp" />
  <div class="main">
    <div class="class_type"> <img src="Images/class_type.gif" alt="新闻中心" /> </div>
    <div class="content">
      <ul class="class_date">
      	<li id='class_month'>
      	<c:set value="1" var="column" />
      	<c:forEach var="topic" items="${topic_list}">
      		<a href="index.jsp?tid=${topic.tid}"><b>${topic.tname}</b></a>
      		<c:set value="${column+1}" var="column" />
      	</c:forEach>
      </ul>
      <ul class="classlist">
      	<c:forEach var="news" items="${news_list}">
      		<li><a href="news_read.jsp?nid=${news.nid}"> ${news.ntitle} </a>
      		<span> ${news.ncreatedate}</span></li>
      	</c:forEach>
      	<c:if test="${page_no == 1}">
      		<p align="right"> 当前页数:[1/${total_page }]&nbsp; <a href="index.jsp?page_no=2">下一页&nbsp;&nbsp;</a> <a href="index.jsp?page_no=${total_page }">末页</a> </p>
      	</c:if>
      	<c:if test="${page_no == total_page}">
      		<p align="right"> 当前页数:[${total_page }/${total_page }]&nbsp; <a href="index.jsp?page_no=1">首页</a><a href="index.jsp?page_no=${total_page-1 }">&nbsp;&nbsp;上一页</a></p>
      	</c:if>
      	<c:if test="${page_no < total_page && page_no > 1}">
      		<p align="right"> 当前页数:[${page_no}/${total_page }]&nbsp;&nbsp; <a href="index.jsp?page_no=1">首页</a><a href="index.jsp?page_no=${page_no-1}">&nbsp;&nbsp;上一页</a><a href="index.jsp?page_no=${page_no+1}">&nbsp;&nbsp;下一页</a> <a href="index.jsp?page_no=${total_page }">&nbsp;&nbsp;末页</a></p>
      	</c:if>			
       </ul>
    </div>
    <jsp:include page="index-elements/index_rightbar.html" />
  </div>
</div>
<%
	request.removeAttribute("page_no");
	request.removeAttribute("total_page");
	request.removeAttribute("news_list");
	request.removeAttribute("topic_list");
%>
<jsp:include page="index-elements/index_bottom.html" />
