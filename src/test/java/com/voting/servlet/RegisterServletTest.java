package com.voting.servlet;

import com.voting.model.VoterStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private final RegisterServlet servlet = new RegisterServlet();

    private String uniqueUsername() {
        return "user-" + UUID.randomUUID();
    }

    @Test
    void doGetRedirectsToRegisterPage() throws Exception {
        when(request.getContextPath()).thenReturn("/app");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/app/register.jsp");
    }

    @Test
    void doPostRedirectsWithErrorWhenRequiredFieldsMissing() throws Exception {
        when(request.getParameter("fullName")).thenReturn("");
        when(request.getContextPath()).thenReturn("/app");

        servlet.doPost(request, response);

        verify(response).sendRedirect("/app/register.jsp?error=missing");
    }

    @Test
    void doPostRedirectsWithErrorWhenPasswordsDoNotMatch() throws Exception {
        when(request.getParameter("fullName")).thenReturn("Full Name");
        when(request.getParameter("username")).thenReturn(uniqueUsername());
        when(request.getParameter("password")).thenReturn("secret");
        when(request.getParameter("confirmPassword")).thenReturn("different");
        when(request.getContextPath()).thenReturn("/app");

        servlet.doPost(request, response);

        verify(response).sendRedirect("/app/register.jsp?error=mismatch");
    }

    @Test
    void doPostRedirectsWithErrorWhenUsernameAlreadyTaken() throws Exception {
        String username = uniqueUsername();
        VoterStore.getInstance().register("Existing", username, "secret");

        when(request.getParameter("fullName")).thenReturn("Full Name");
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn("secret");
        when(request.getParameter("confirmPassword")).thenReturn("secret");
        when(request.getContextPath()).thenReturn("/app");

        servlet.doPost(request, response);

        verify(response).sendRedirect("/app/register.jsp?error=taken");
    }

    @Test
    void doPostRegistersVoterAndRedirectsToLoginOnSuccess() throws Exception {
        String username = uniqueUsername();

        when(request.getParameter("fullName")).thenReturn("Full Name");
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn("secret");
        when(request.getParameter("confirmPassword")).thenReturn("secret");
        when(request.getContextPath()).thenReturn("/app");

        servlet.doPost(request, response);

        verify(response).sendRedirect("/app/login.jsp?registered=true");
    }
}
