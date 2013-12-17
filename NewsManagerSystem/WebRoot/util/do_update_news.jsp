<%@ page language="java" import="java.util.*,java.sql.*,com.jspsmart.upload.*" pageEncoding="gbk"%>
<%@page import="java.text.SimpleDateFormat"%>
<jsp:useBean id="nd" beanName="org.jbit.news.dao.impl.NewsDaoImpl" type="org.jbit.news.dao.NewsDao" />
<jsp:useBean id="nbl" beanName="org.jbit.news.biz.impl.NewsBizImpl" type="org.jbit.news.biz.NewsBiz" />
<jsp:setProperty property="nd" name="nbl" value="<%=nd %>"/>
<%
	SmartUpload su = new SmartUpload();
	//初始化SmartUpload对象
	su.initialize(pageContext);
	com.jspsmart.upload.File file = null;
	//允许上传类型
	String allowed = "gif,jpg,doc,rar";
	//不许上传类型
	String denied = "jsp,asp,php,aspx,html,htm,exe,bat";
	com.jspsmart.upload.Request req  = null;
	//设置上传文件大小
	int file_size = 2*1024*1024;
	String exceptionMsg = null;
	int i = 0;
	try {
			//定义允许上传文件类型   
			su.setAllowedFilesList(allowed);
			//不允许上传文件类型   
			su.setDeniedFilesList(denied);		
			//单个文件最大限制   
			su.setMaxFileSize(file_size);						
			su.setCharset("GBK");
			//执行上传
			su.upload();
			//得到单个上传文件的信息
			file = su.getFiles().getFile(0);
			String filepath = null;
			if(!file.isMissing()){
				//设置文件在服务器的保存位置
				filepath = "upload\\";
				filepath += file.getFileName();
				//文件另存为   
				file.setCharset("gbk");
				file.saveAs(filepath, SmartUpload.SAVE_VIRTUAL);
			}
			//获取smartupload封装的request
			req = su.getRequest();
			//获取添加新闻页面传递过来的参数	
			String nid = req.getParameter("nid");
			String ntid = req.getParameter("ntid");
			String ntitle = req.getParameter("ntitle");
			String nauthor = req.getParameter("nauthor");
			String nsummary = req.getParameter("nsummary");
			String ncontent = req.getParameter("ncontent");
			String filePath = req.getParameter("file");
			Map news = new HashMap();
			news.put("nid",nid);
			news.put("ntid",ntid);
			news.put("ntitle",ntitle);
			news.put("nauthor",nauthor);
			news.put("filePath",filepath);
			news.put("ncontent",ncontent);
			news.put("nsummary",nsummary);
			i = nbl.updateNews(news);
		} catch (Exception e) {
			exceptionMsg = e.getMessage();
			e.printStackTrace();
		}	
	if(i > 0){	
%>
<script type="text/javascript">
	alert("已经成功更新新闻，点击确认返回新闻列表");
	open("../newspages/admin.jsp","_self");	
</script>
<%}else{
%>
<script type="text/javascript">
	alert("更新新闻失败，点击确认返回新闻列表");
	open("../newspages/admin.jsp","_self");	
</script>
<%
}
%>
