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
import java.util.List;
import java.util.UUID;

@WebServlet("/records")
public class StudentRecordServlet extends HttpServlet {
    private final StudentRecordDao recordDao = new StudentRecordDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            List<StudentRecord> records = recordDao.findAll();
            req.setAttribute("records", records);
            req.setAttribute("username", req.getSession().getAttribute("username"));

            String idText = req.getParameter("editId");
            if (idText != null && !idText.trim().isEmpty()) {
                int id = Integer.parseInt(idText);
                StudentRecord editRecord = recordDao.findById(id);
                req.setAttribute("editRecord", editRecord);
            }

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
                setFlash(req.getSession(), "新增记录成功");
            } else if ("update".equals(action)) {
                StudentRecord record = buildRecord(req);
                record.setId(parseRequiredInt(req, "id"));
                recordDao.update(record);
                setFlash(req.getSession(), "修改记录成功");
            } else if ("delete".equals(action)) {
                int id = parseRequiredInt(req, "id");
                recordDao.delete(id);
                setFlash(req.getSession(), "删除记录成功");
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
        record.setName(req.getParameter("name"));
        record.setAge(parseRequiredInt(req, "age"));
        record.setMajor(req.getParameter("major"));
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
}
