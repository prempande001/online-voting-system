package com.voting.servlet;

import com.voting.model.VoterStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    private final LoginServlet servlet = new LoginServlet();

    @Test
    void doGetRedirectsToLoginPage() throws Exception {
        when(request.getContextPath()).thenReturn("/app");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/app/login.jsp");
    }

    @Test
    void doPostRedirectsWithErrorWhenCredentialsMissing() throws Exception {
        when(request.getParameter("username")).thenReturn(null);
        when(request.getContextPath()).thenReturn("/app");

        servlet.doPost(request, response);

        verify(response).sendRedirect("/app/login.jsp?error=invalid");
    }

    @Test
    void doPostRedirectsWithErrorForInvalidCredentials() throws Exception {
        when(request.getParameter("username")).thenReturn("nobody-" + UUID.randomUUID());
        when(request.getParameter("password")).thenReturn("wrong");
        when(request.getContextPath()).thenReturn("/app");

        servlet.doPost(request, response);

        verify(response).sendRedirect("/app/login.jsp?error=invalid");
    }

    @Test
    void doPostStartsSessionAndRedirectsHomeForValidCredentials() throws Exception {
        String username = "user-" + UUID.randomUUID();
        VoterStore.getInstance().register("Full Name", username, "secret");

        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn("secret");
        when(request.getContextPath()).thenReturn("/app");
        when(request.getSession()).thenReturn(session);

        servlet.doPost(request, response);

        verify(session).setAttribute(any(), any());
        verify(response).sendRedirect("/app/index.jsp");
    }
}
