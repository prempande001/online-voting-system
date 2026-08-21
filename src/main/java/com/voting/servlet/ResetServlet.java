package com.voting.servlet;

import com.voting.model.VoteStore;
import com.voting.model.VoterStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Convenience endpoint for local testing/demos only: clears all vote
 * counts and every registered voter's "hasVoted" flag so you can vote
 * again without restarting the server. Consider removing or protecting
 * this before any real deployment.
 */
@WebServlet("/reset")
public class ResetServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        VoteStore.getInstance().resetVotes();
        VoterStore.getInstance().resetVotedFlags();
        resp.sendRedirect(req.getContextPath() + "/index.jsp?reset=true");
    }
}
