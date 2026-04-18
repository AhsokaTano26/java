<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.model.StudentRecord" %>
<%@ page import="com.example.util.HtmlEscapeUtil" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>情绪垃圾桶匿名墙</title>
    <style>
        :root {
            --glass-bg: rgba(255, 255, 255, 0.18);
            --glass-border: rgba(255, 255, 255, 0.35);
            --text-primary: #1f2937;
            --text-subtle: #4b5563;
            --bg-gradient: linear-gradient(135deg, #dbeafe 0%, #ede9fe 35%, #fce7f3 100%);
        }

        * { box-sizing: border-box; }

        body {
            margin: 0;
            min-height: 100vh;
            font-family: -apple-system, BlinkMacSystemFont, "SF Pro Text", "Segoe UI", sans-serif;
            color: var(--text-primary);
            background: var(--bg-gradient);
        }

        .page {
            max-width: 920px;
            margin: 0 auto;
            padding: 28px 16px 36px;
        }

        .glass {
            background: var(--glass-bg);
            border: 1px solid var(--glass-border);
            border-radius: 22px;
            backdrop-filter: blur(20px) saturate(1.2);
            -webkit-backdrop-filter: blur(20px) saturate(1.2);
            box-shadow: 0 12px 36px rgba(31, 41, 55, 0.12);
        }

        .hero {
            padding: 20px;
            margin-bottom: 16px;
        }

        .hero h1 {
            margin: 0 0 6px;
            font-size: 26px;
        }

        .subtle {
            color: var(--text-subtle);
            margin: 0;
        }

        .top-bar {
            margin-top: 14px;
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
            align-items: center;
            justify-content: space-between;
        }

        .actions a, .actions button {
            text-decoration: none;
            border: 1px solid rgba(255, 255, 255, 0.5);
            color: #111827;
            background: rgba(255, 255, 255, 0.35);
            border-radius: 999px;
            padding: 8px 14px;
            cursor: pointer;
            font-size: 14px;
        }

        .panel {
            padding: 16px;
            margin-bottom: 16px;
        }

        textarea, select {
            width: 100%;
            margin-top: 6px;
            border-radius: 14px;
            border: 1px solid rgba(255, 255, 255, 0.7);
            background: rgba(255, 255, 255, 0.78);
            padding: 10px 12px;
            color: #111827;
            font-size: 14px;
        }

        .row {
            display: grid;
            grid-template-columns: 1fr 180px;
            gap: 12px;
            margin-top: 10px;
        }

        .note-list {
            display: grid;
            gap: 12px;
        }

        .note {
            border-radius: 18px;
            padding: 14px;
            border: 1px solid rgba(255, 255, 255, 0.55);
            background: rgba(255, 255, 255, 0.7);
        }

        .note-calm { background: linear-gradient(135deg, rgba(209, 250, 229, 0.85), rgba(240, 253, 250, 0.9)); }
        .note-happy { background: linear-gradient(135deg, rgba(254, 240, 138, 0.82), rgba(254, 249, 195, 0.92)); }
        .note-sad { background: linear-gradient(135deg, rgba(191, 219, 254, 0.82), rgba(224, 242, 254, 0.92)); }
        .note-angry { background: linear-gradient(135deg, rgba(254, 202, 202, 0.82), rgba(254, 226, 226, 0.92)); }
        .note-anxious { background: linear-gradient(135deg, rgba(221, 214, 254, 0.82), rgba(237, 233, 254, 0.92)); }
        .note-tired { background: linear-gradient(135deg, rgba(229, 231, 235, 0.85), rgba(243, 244, 246, 0.92)); }

        .note-head {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 8px;
            color: var(--text-subtle);
            font-size: 13px;
        }

        .note-content {
            margin: 0 0 10px;
            white-space: pre-wrap;
            word-break: break-word;
            line-height: 1.55;
        }

        .danger-btn {
            border: none;
            background: rgba(239, 68, 68, 0.14);
            color: #b91c1c;
            border-radius: 999px;
            padding: 7px 12px;
            cursor: pointer;
        }

        .flash {
            margin-bottom: 14px;
            border-radius: 12px;
            padding: 10px 12px;
            background: rgba(16, 185, 129, 0.16);
            color: #065f46;
            border: 1px solid rgba(16, 185, 129, 0.25);
        }

        @media (max-width: 720px) {
            .row { grid-template-columns: 1fr; }
        }
    </style>
</head>
<body>
<div class="page">
<%
    Object flash = request.getAttribute("flashMessage");
    String csrfToken = (String) request.getAttribute("csrfToken");
    String username = (String) request.getAttribute("username");
    String mode = (String) request.getAttribute("mode");
    List<StudentRecord> records = (List<StudentRecord>) request.getAttribute("records");
%>
    <div class="hero glass">
        <h1>情绪垃圾桶 · 匿名墙</h1>
        <p class="subtle">把情绪写下来，轻轻放下。墙上只显示内容与心情色块，不显示发布者身份。</p>
        <div class="top-bar">
            <p class="subtle">已登录：<strong><%= HtmlEscapeUtil.escape(username) %></strong></p>
            <div class="actions">
                <a href="<%= request.getContextPath() %>/records?mode=timeline">时间轴</a>
                <a href="<%= request.getContextPath() %>/records?mode=random">随机流</a>
                <a href="<%= request.getContextPath() %>/logout">退出</a>
            </div>
        </div>
    </div>

    <div class="panel glass">
        <form method="post" action="<%= request.getContextPath() %>/records">
            <input type="hidden" name="csrfToken" value="<%= HtmlEscapeUtil.escape(csrfToken) %>" />
            <input type="hidden" name="action" value="add" />
            <label for="content">写下这一刻（最多 280 字）</label>
            <textarea id="content" name="content" maxlength="280" rows="4" required placeholder="我想说..."></textarea>
            <div class="row">
                <div>
                    <label for="mood">心情颜色</label>
                    <select id="mood" name="mood" required>
                        <option value="calm">平静</option>
                        <option value="happy">开心</option>
                        <option value="sad">低落</option>
                        <option value="angry">生气</option>
                        <option value="anxious">焦虑</option>
                        <option value="tired">疲惫</option>
                    </select>
                </div>
                <div style="display:flex;align-items:flex-end;">
                    <button type="submit">投递纸条</button>
                </div>
            </div>
        </form>
    </div>

    <%
        if (flash != null) {
    %>
    <div class="flash"><%= HtmlEscapeUtil.escape(flash.toString()) %></div>
    <%
        }
    %>

    <div class="panel glass">
        <p class="subtle" style="margin-top:0;">
            当前查看：<strong><%= "random".equals(mode) ? "随机流" : "时间轴" %></strong>
        </p>
        <div class="note-list">
            <%
                if (records != null && !records.isEmpty()) {
                    for (StudentRecord note : records) {
                        String moodClass = "note-" + HtmlEscapeUtil.escape(note.getMood());
                        boolean own = username != null && username.equals(note.getOwnerUsername());
            %>
            <article class="note <%= moodClass %>">
                <div class="note-head">
                    <span>#匿名纸条 · <%= HtmlEscapeUtil.escape(note.getCreatedAt()) %></span>
                    <span><%= HtmlEscapeUtil.escape(note.getMood()) %></span>
                </div>
                <p class="note-content"><%= HtmlEscapeUtil.escape(note.getContent()) %></p>
                <%
                    if (own) {
                %>
                <form method="post" action="<%= request.getContextPath() %>/records">
                    <input type="hidden" name="csrfToken" value="<%= HtmlEscapeUtil.escape(csrfToken) %>" />
                    <input type="hidden" name="action" value="delete" />
                    <input type="hidden" name="id" value="<%= note.getId() %>" />
                    <button class="danger-btn" type="submit" onclick="return confirm('确认撤回这条纸条？');">撤回我的纸条</button>
                </form>
                <%
                    }
                %>
            </article>
            <%
                    }
                } else {
            %>
            <p class="subtle">还没有纸条，试着投递第一条吧。</p>
            <%
                }
            %>
        </div>
    </div>
</div>
</body>
</html>
