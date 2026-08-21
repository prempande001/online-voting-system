package com.voting.servlet;

import com.voting.model.Voter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoteServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    private final VoteServlet servlet = new VoteServlet();

    @Test
    void doGetRedirectsBackToBallot() throws Exception {
        when(request.getContextPath()).thenReturn("/app");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/app/index.jsp");
    }

    @Test
    void doPostRedirectsToLoginWhenNoVoterInSession() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("voter")).thenReturn(null);
        when(request.getContextPath()).thenReturn("/app");

        servlet.doPost(request, response);

        verify(response).sendRedirect("/app/login.jsp");
    }

    @Test
    void doPostRedirectsWithAlreadyVotedWhenVoterHasVoted() throws Exception {
        Voter voter = new Voter(1, "Full Name", "user", "hash");
        voter.setHasVoted(true);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("voter")).thenReturn(voter);
        when(request.getContextPath()).thenReturn("/app");

        servlet.doPost(request, response);

        verify(response).sendRedirect("/app/results.jsp?alreadyVoted=true");
    }

    @Test
    void doPostRedirectsWithMissingErrorWhenCandidateIdBlank() throws Exception {
        Voter voter = new Voter(2, "Full Name", "user2", "hash");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("voter")).thenReturn(voter);
        when(request.getParameter("candidateId")).thenReturn("  ");
        when(request.getContextPath()).thenReturn("/app");

        servlet.doPost(request, response);

        verify(response).sendRedirect("/app/index.jsp?error=missing");
    }

    @Test
    void doPostRedirectsWithInvalidErrorWhenCandidateIdNotANumber() throws Exception {
        Voter voter = new Voter(3, "Full Name", "user3", "hash");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("voter")).thenReturn(voter);
        when(request.getParameter("candidateId")).thenReturn("not-a-number");
        when(request.getContextPath()).thenReturn("/app");

        servlet.doPost(request, response);

        verify(response).sendRedirect("/app/index.jsp?error=invalid");
    }

    @Test
    void doPostRedirectsWithInvalidErrorWhenCandidateDoesNotExist() throws Exception {
        Voter voter = new Voter(4, "Full Name", "user4", "hash");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("voter")).thenReturn(voter);
        when(request.getParameter("candidateId")).thenReturn("999");
        when(request.getContextPath()).thenReturn("/app");

        servlet.doPost(request, response);

        verify(response).sendRedirect("/app/index.jsp?error=invalid");
    }

    @Test
    void doPostRecordsVoteAndRedirectsToResultsOnSuccess() throws Exception {
        Voter voter = new Voter(5, "Full Name", "user5", "hash");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("voter")).thenReturn(voter);
        when(request.getParameter("candidateId")).thenReturn("1");
        when(request.getContextPath()).thenReturn("/app");

        servlet.doPost(request, response);

        verify(response).sendRedirect("/app/results.jsp?voted=true");
    }
}
