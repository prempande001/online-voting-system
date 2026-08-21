package com.voting.servlet;

import com.voting.model.Voter;
import com.voting.model.VoteStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Handles vote submissions from index.jsp.
 * Requires a logged-in voter (see LoginServlet) and uses that voter's
 * hasVoted flag (not a database) to stop a single registered voter
 * from voting more than once.
 */
@WebServlet("/vote")
public class VoteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        Voter voter = (Voter) session.getAttribute("voter");

        if (voter == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        if (voter.isHasVoted()) {
            resp.sendRedirect(req.getContextPath() + "/results.jsp?alreadyVoted=true");
            return;
        }

        String candidateIdParam = req.getParameter("candidateId");
        if (candidateIdParam == null || candidateIdParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=missing");
            return;
        }

        try {
            int candidateId = Integer.parseInt(candidateIdParam.trim());
            boolean success = VoteStore.getInstance().castVote(candidateId);

            if (success) {
                voter.setHasVoted(true);
                resp.sendRedirect(req.getContextPath() + "/results.jsp?voted=true");
            } else {
                resp.sendRedirect(req.getContextPath() + "/index.jsp?error=invalid");
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=invalid");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Voting must be a POST; redirect stray GET requests back to the form.
        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }
}
