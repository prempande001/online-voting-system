package com.voting.servlet;

import com.voting.model.VoteStore;
import com.voting.model.Voter;
import com.voting.model.VoterStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResetServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private final ResetServlet servlet = new ResetServlet();

    @Test
    void doGetResetsVotesAndVotedFlagsThenRedirects() throws Exception {
        String username = "user-" + UUID.randomUUID();
        Voter voter = VoterStore.getInstance().register("Full Name", username, "secret");
        voter.setHasVoted(true);
        VoteStore.getInstance().castVote(1);

        when(request.getContextPath()).thenReturn("/app");

        servlet.doGet(request, response);

        assertFalse(voter.isHasVoted());
        assertEquals(0, VoteStore.getInstance().getVoteCount(1));
        verify(response).sendRedirect("/app/index.jsp?reset=true");
    }
}
