package com.example.servlet;

import com.example.dao.StudentRecordDao;
import com.example.model.StudentRecord;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@WebServlet("/records")
public class StudentRecordServlet extends HttpServlet {
    private final StudentRecordDao recordDao = new StudentRecordDao();
    private static final Set<String> ALLOWED_MOODS = new HashSet<>(Arrays.asList(
            "calm", "happy", "sad", "angry", "anxious", "tired"
    ));

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            String mode = normalizeMode(req.getParameter("mode"));
            List<StudentRecord> records = "random".equals(mode) ? recordDao.findAllRandom() : recordDao.findAllByTimeline();
            req.setAttribute("records", records);
            req.setAttribute("mode", mode);
            req.setAttribute("username", req.getSession().getAttribute("username"));

            HttpSession session = req.getSession();
            Object flashMessage = session.getAttribute("flashMessage");
            if (flashMessage != null) {
                req.setAttribute("flashMessage", flashMessage);
                session.removeAttribute("flashMessage");
            }

            String csrfToken = (String) session.getAttribute("csrfToken");
            if (csrfToken == null || csrfToken.trim().isEmpty()) {
                csrfToken = UUID.randomUUID().toString();
                session.setAttribute("csrfToken", csrfToken);
            }
            req.setAttribute("csrfToken", csrfToken);

            req.getRequestDispatcher("/records.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("查询记录失败", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (!verifyCsrf(req)) {
            setFlash(req.getSession(), "CSRF 校验失败，请刷新页面后重试");
            resp.sendRedirect(req.getContextPath() + "/records");
            return;
        }

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        try {
            if ("add".equals(action)) {
                StudentRecord record = buildRecord(req);
                recordDao.insert(record);
                setFlash(req.getSession(), "纸条投递成功");
            } else if ("delete".equals(action)) {
                int id = parseRequiredInt(req, "id");
                String username = currentUsername(req);
                int affected = recordDao.deleteByIdAndOwner(id, username);
                if (affected > 0) {
                    setFlash(req.getSession(), "已撤回你的纸条");
                } else {
                    setFlash(req.getSession(), "仅能撤回你自己的纸条，或纸条不存在");
                }
            } else {
                setFlash(req.getSession(), "未知操作");
            }
        } catch (IllegalArgumentException e) {
            setFlash(req.getSession(), e.getMessage());
        } catch (SQLException e) {
            throw new ServletException("处理记录失败", e);
        }

        resp.sendRedirect(req.getContextPath() + "/records");
    }

    private StudentRecord buildRecord(HttpServletRequest req) {
        StudentRecord record = new StudentRecord();
        String content = safeTrim(req.getParameter("content"));
        if (content.isEmpty()) {
            throw new IllegalArgumentException("纸条内容不能为空");
        }
        if (content.length() > 280) {
            throw new IllegalArgumentException("纸条内容不能超过 280 字");
        }
        String mood = safeTrim(req.getParameter("mood"));
        if (!ALLOWED_MOODS.contains(mood)) {
            throw new IllegalArgumentException("请选择有效的情绪标签");
        }

        record.setContent(content);
        record.setMood(mood);
        record.setOwnerUsername(currentUsername(req));
        return record;
    }

    private int parseRequiredInt(HttpServletRequest req, String field) {
        String value = req.getParameter(field);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("参数 " + field + " 不能为空");
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("参数 " + field + " 必须是整数");
        }
    }

    private String normalizeMode(String mode) {
        return "random".equals(mode) ? "random" : "timeline";
    }

    private boolean verifyCsrf(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return false;
        }
        String expected = (String) session.getAttribute("csrfToken");
        String actual = req.getParameter("csrfToken");
        return expected != null && expected.equals(actual);
    }

    private boolean isLoggedIn(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null && session.getAttribute("username") != null;
    }

    private void setFlash(HttpSession session, String message) {
        session.setAttribute("flashMessage", message);
    }

    private String currentUsername(HttpServletRequest req) {
        Object username = req.getSession().getAttribute("username");
        return username == null ? "" : username.toString();
    }

    private String safeTrim(String text) {
        return text == null ? "" : text.trim();
    }
}
