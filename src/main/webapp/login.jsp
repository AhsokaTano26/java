<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.example.util.HtmlEscapeUtil" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>用户登录</title>
</head>
<body>
<h2>JAVA WEB 登录验证</h2>
<%
    Object error = request.getAttribute("error");
    if (error != null) {
%>
<p style="color:red;"><%= HtmlEscapeUtil.escape(error.toString()) %></p>
<%
    }
%>
<form method="post" action="<%= request.getContextPath() %>/login">
    <label>用户名：</label>
    <input type="text" name="username" required/><br/><br/>

    <label>密码：</label>
    <input type="password" name="password" required/><br/><br/>

    <button type="submit">登录</button>
</form>
</body>
</html>
