<%@ page language="java" import="java.util.*,java.sql.*" pageEncoding="gbk"%>
<jsp:useBean id="nd" beanName="org.jbit.news.dao.impl.NewsDaoImpl" type="org.jbit.news.dao.NewsDao" />
<jsp:useBean id="nbl" beanName="org.jbit.news.biz.impl.NewsBizImpl" type="org.jbit.news.biz.NewsBiz" />
<jsp:setProperty property="nd" name="nbl" value="<%=nd %>"/>
<%
	request.setCharacterEncoding("gbk");
	String nid = request.getParameter("nid");
	int i = nbl.deleteNews(nid);
	if(i > 0){	
%>
<script type="text/javascript">
	alert("已经成功删除新闻，点击确认返回原来页面");
	open("../newspages/admin.jsp","_self");	
</script>
<%
	}else{
%>
<script type="text/javascript">
	alert("删除新闻失败！请联系管理员查找原因！点击确认返回原来页面");
	open("../newspages/admin.jsp","_self");	
</script>
<%	
	}
%>
