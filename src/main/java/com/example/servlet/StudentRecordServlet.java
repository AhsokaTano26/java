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

            Object flashMessage = req.getSession().getAttribute("flashMessage");
            if (flashMessage != null) {
                req.setAttribute("flashMessage", flashMessage);
                req.getSession().removeAttribute("flashMessage");
            }

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

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        try {
            if ("add".equals(action)) {
                StudentRecord record = buildRecord(req);
                recordDao.insert(record);
                setFlash(req.getSession(), "新增记录成功");
            } else if ("update".equals(action)) {
                StudentRecord record = buildRecord(req);
                record.setId(Integer.parseInt(req.getParameter("id")));
                recordDao.update(record);
                setFlash(req.getSession(), "修改记录成功");
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                recordDao.delete(id);
                setFlash(req.getSession(), "删除记录成功");
            } else {
                setFlash(req.getSession(), "未知操作");
            }
        } catch (NumberFormatException e) {
            setFlash(req.getSession(), "参数格式错误，请检查输入");
        } catch (SQLException e) {
            throw new ServletException("处理记录失败", e);
        }

        resp.sendRedirect(req.getContextPath() + "/records");
    }

    private StudentRecord buildRecord(HttpServletRequest req) {
        StudentRecord record = new StudentRecord();
        record.setName(req.getParameter("name"));
        record.setAge(Integer.parseInt(req.getParameter("age")));
        record.setMajor(req.getParameter("major"));
        return record;
    }

    private boolean isLoggedIn(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null && session.getAttribute("username") != null;
    }

    private void setFlash(HttpSession session, String message) {
        session.setAttribute("flashMessage", message);
    }
}
