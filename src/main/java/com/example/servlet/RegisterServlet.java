package com.example.servlet;

import com.example.dao.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            resp.sendRedirect(req.getContextPath() + "/records");
            return;
        }
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        if (isBlank(username) || isBlank(password) || isBlank(confirmPassword)) {
            req.setAttribute("error", "用户名和密码不能为空");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        username = username.trim();
        if (!password.equals(confirmPassword)) {
            req.setAttribute("error", "两次输入的密码不一致");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        try {
            boolean created = userDao.register(username, password.trim());
            if (!created) {
                req.setAttribute("error", "用户名已存在");
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/login?registered=1");
        } catch (SQLException e) {
            throw new ServletException("注册失败", e);
        }
    }

    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}
