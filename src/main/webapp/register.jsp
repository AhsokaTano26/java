<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.example.util.HtmlEscapeUtil" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>注册 - 情绪垃圾桶</title>
    <style>
        body {
            margin: 0;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: -apple-system, BlinkMacSystemFont, "SF Pro Text", "Segoe UI", sans-serif;
            background: linear-gradient(135deg, #dbeafe 0%, #e9d5ff 55%, #fbcfe8 100%);
            color: #111827;
        }
        .card {
            width: min(420px, 92vw);
            padding: 24px;
            border-radius: 22px;
            background: rgba(255, 255, 255, 0.2);
            border: 1px solid rgba(255, 255, 255, 0.45);
            box-shadow: 0 12px 36px rgba(31, 41, 55, 0.14);
            backdrop-filter: blur(20px) saturate(1.2);
            -webkit-backdrop-filter: blur(20px) saturate(1.2);
        }
        h2 { margin: 0 0 8px; }
        p { margin: 0 0 16px; color: #4b5563; }
        label { display: block; margin: 10px 0 6px; }
        input {
            width: 100%;
            box-sizing: border-box;
            border-radius: 14px;
            border: 1px solid rgba(255, 255, 255, 0.7);
            background: rgba(255, 255, 255, 0.8);
            padding: 10px 12px;
        }
        button {
            margin-top: 14px;
            width: 100%;
            border: none;
            border-radius: 14px;
            padding: 11px 12px;
            background: rgba(30, 64, 175, 0.84);
            color: #fff;
            cursor: pointer;
        }
        .error {
            margin-top: 10px;
            border-radius: 12px;
            padding: 10px 12px;
            color: #991b1b;
            background: rgba(254, 202, 202, 0.5);
            border: 1px solid rgba(239, 68, 68, 0.35);
        }
        .login-link {
            margin-top: 12px;
            text-align: center;
        }
        .login-link a {
            color: #1d4ed8;
            text-decoration: none;
        }
        .login-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="card">
    <h2>创建账号</h2>
    <p>注册后即可登录并投递心情纸条。</p>
    <form method="post" action="<%= request.getContextPath() %>/register">
        <label>用户名</label>
        <input type="text" name="username" required />
        <label>密码</label>
        <input type="password" name="password" required />
        <label>确认密码</label>
        <input type="password" name="confirmPassword" required />
        <button type="submit">注册</button>
    </form>
    <div class="login-link">
        已有账号？<a href="<%= request.getContextPath() %>/login">去登录</a>
    </div>
    <%
        Object error = request.getAttribute("error");
        if (error != null) {
    %>
    <div class="error"><%= HtmlEscapeUtil.escape(error.toString()) %></div>
    <%
        }
    %>
</div>
</body>
</html>
