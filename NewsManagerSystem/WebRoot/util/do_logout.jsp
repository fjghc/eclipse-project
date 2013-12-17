<%@ page language="java" import="java.util.*,java.sql.*" pageEncoding="gbk"%>
<%
	session.removeAttribute("admin");
	response.sendRedirect("../index.jsp");
%>
