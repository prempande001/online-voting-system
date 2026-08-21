package com.voting.servlet;

import com.voting.model.Voter;
import com.voting.model.VoterStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles new voter registration from register.jsp.
 * Voters are stored in memory via VoterStore - no database.
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/register.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String fullName = trim(req.getParameter("fullName"));
        String username = trim(req.getParameter("username"));
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        if (fullName.isEmpty() || username.isEmpty() || password == null || password.isEmpty()) {
            redirectWithError(req, resp, "missing");
            return;
        }
        if (!password.equals(confirmPassword)) {
            redirectWithError(req, resp, "mismatch");
            return;
        }
        if (VoterStore.getInstance().isUsernameTaken(username)) {
            redirectWithError(req, resp, "taken");
            return;
        }

        Voter voter = VoterStore.getInstance().register(fullName, username, password);
        if (voter == null) {
            redirectWithError(req, resp, "taken");
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/login.jsp?registered=true");
    }

    private static String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private static void redirectWithError(HttpServletRequest req, HttpServletResponse resp, String code)
            throws IOException {
        resp.sendRedirect(req.getContextPath() + "/register.jsp?error=" + code);
    }
}
