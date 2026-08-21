package com.voting.servlet;

import com.voting.model.Voter;
import com.voting.model.VoterStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Authenticates a registered voter and starts their session.
 * No database - credentials are checked against VoterStore in memory.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || password == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=invalid");
            return;
        }

        Voter voter = VoterStore.getInstance().authenticate(username.trim(), password);
        if (voter == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=invalid");
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("voter", voter);
        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }
}
