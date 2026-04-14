<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.model.StudentRecord" %>
<%!
    private String h(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>学生信息管理</title>
</head>
<body>
<h2>学生信息管理（增删改查）</h2>
<p>当前用户：<strong><%= h((String) request.getAttribute("username")) %></strong> | <a href="<%= request.getContextPath() %>/logout">退出登录</a></p>

<%
    Object flash = request.getAttribute("flashMessage");
    if (flash != null) {
%>
<p style="color:green;"><%= h(flash.toString()) %></p>
<%
    }

    StudentRecord editRecord = (StudentRecord) request.getAttribute("editRecord");
    boolean editing = editRecord != null;
%>

<h3><%= editing ? "修改记录" : "新增记录" %></h3>
<form method="post" action="<%= request.getContextPath() %>/records">
    <input type="hidden" name="action" value="<%= editing ? "update" : "add" %>" />
    <%
        if (editing) {
    %>
    <input type="hidden" name="id" value="<%= editRecord.getId() %>" />
    <%
        }
    %>
    姓名：<input type="text" name="name" required value="<%= editing ? h(editRecord.getName()) : "" %>" />
    年龄：<input type="number" name="age" min="1" max="120" required value="<%= editing ? editRecord.getAge() : "" %>" />
    专业：<input type="text" name="major" required value="<%= editing ? h(editRecord.getMajor()) : "" %>" />
    <button type="submit"><%= editing ? "保存" : "添加" %></button>
    <%
        if (editing) {
    %>
    <a href="<%= request.getContextPath() %>/records">取消编辑</a>
    <%
        }
    %>
</form>

<h3>记录列表</h3>
<table border="1" cellpadding="8" cellspacing="0">
    <thead>
    <tr>
        <th>ID</th>
        <th>姓名</th>
        <th>年龄</th>
        <th>专业</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <%
        List<StudentRecord> records = (List<StudentRecord>) request.getAttribute("records");
        if (records != null && !records.isEmpty()) {
            for (StudentRecord record : records) {
    %>
    <tr>
        <td><%= record.getId() %></td>
        <td><%= h(record.getName()) %></td>
        <td><%= record.getAge() %></td>
        <td><%= h(record.getMajor()) %></td>
        <td>
            <a href="<%= request.getContextPath() %>/records?editId=<%= record.getId() %>">编辑</a>
            <form method="post" action="<%= request.getContextPath() %>/records" style="display:inline;">
                <input type="hidden" name="action" value="delete" />
                <input type="hidden" name="id" value="<%= record.getId() %>" />
                <button type="submit" onclick="return confirm('确认删除该记录？');">删除</button>
            </form>
        </td>
    </tr>
    <%
            }
        } else {
    %>
    <tr>
        <td colspan="5">暂无数据</td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>
</body>
</html>
