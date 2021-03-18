package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class RegServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("name");
        String userEmail = req.getParameter("email");
        String userPassword = req.getParameter("password");
        try {
            PsqlStore.instOf().save(
                    new User(0, userName, userEmail, userPassword));
        } catch (SQLException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/reg.jsp").forward(req, resp);
        }
        resp.sendRedirect(req.getContextPath() + "/auth.do");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("reg.jsp").forward(req, resp);
    }
}
