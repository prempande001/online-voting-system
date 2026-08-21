package com.voting.servlet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogoutServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    private final LogoutServlet servlet = new LogoutServlet();

    @Test
    void doGetInvalidatesExistingSessionAndRedirects() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(request.getContextPath()).thenReturn("/app");

        servlet.doGet(request, response);

        verify(session).invalidate();
        verify(response).sendRedirect("/app/login.jsp");
    }

    @Test
    void doGetSkipsInvalidateWhenNoSessionExists() throws Exception {
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/app");

        servlet.doGet(request, response);

        verify(session, never()).invalidate();
        verify(response).sendRedirect("/app/login.jsp");
    }
}
