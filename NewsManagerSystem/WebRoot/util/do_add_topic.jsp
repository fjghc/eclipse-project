<%@ page language="java" import="java.util.*,java.sql.*" pageEncoding="gbk"%>
<jsp:useBean id="td" beanName="org.jbit.news.dao.impl.TopicsDaoImpl" type="org.jbit.news.dao.TopicsDao" />
<jsp:useBean id="tbl" beanName="org.jbit.news.biz.impl.TopicBizImpl" type="org.jbit.news.biz.TopicBiz" />
<jsp:setProperty property="td" name="tbl" value="<%=td %>"/>
<%
	request.setCharacterEncoding("gbk");
	String tname = request.getParameter("tname");
	Map topic = tbl.findTopicByTname(tname);
	if(topic != null){
%>
<script type="text/javascript">
	alert("当前主题已存在，请输入不同的主题！");
	open("../newspages/topic_add.jsp","_self");	
</script>
<%
	}else{
		topic = new HashMap();
		topic.put("tname", tname);
		tbl.addTopic(topic);
%>
<script type="text/javascript">
	alert("当前主题创建成功，点击确认返回主题列表！");
	open("../newspages/topic_list.jsp","_self");	
</script>
<%
	}
%>
