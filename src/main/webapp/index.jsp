<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.voting.model.Candidate" %>
<%@ page import="com.voting.model.VoteStore" %>
<%@ page import="com.voting.model.Voter" %>
<%@ page import="java.util.Map" %>
<%
    Voter voter = (Voter) session.getAttribute("voter");
    if (voter == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Online Voting Application</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container">
    <div class="welcome-bar">
        <span>Welcome, <strong><%= voter.getFullName() %></strong></span>
        <a href="logout" class="logout-link">Log out</a>
    </div>

    <h1>Online Voting Application</h1>
    <p class="subtitle">Cast your vote below. One vote per registered voter.</p>

    <%
        String error = request.getParameter("error");
        if ("missing".equals(error)) {
    %>
        <p class="alert">Please select a candidate before voting.</p>
    <%
        } else if ("invalid".equals(error)) {
    %>
        <p class="alert">Invalid candidate selected. Please try again.</p>
    <%
        }
        if ("true".equals(request.getParameter("reset"))) {
    %>
        <p class="success">Votes have been reset for testing.</p>
    <%
        }

        if (voter.isHasVoted()) {
    %>
        <p class="alert">You have already cast your vote. <a href="results.jsp">View results</a>.</p>
    <%
        } else {
    %>

    <form action="vote" method="post">
        <div class="candidate-list">
            <%
                Map<Integer, Candidate> candidates = VoteStore.getInstance().getCandidates();
                for (Candidate c : candidates.values()) {
            %>
            <label class="candidate-card">
                <input type="radio" name="candidateId" value="<%= c.getId() %>" required>
                <span class="candidate-name"><%= c.getName() %></span>
                <span class="candidate-party"><%= c.getParty() %></span>
            </label>
            <%
                }
            %>
        </div>
        <button type="submit" class="btn-vote">Cast Vote</button>
    </form>

    <%
        }
    %>

    <p class="results-link"><a href="results.jsp">View Live Results</a></p>
</div>
</body>
</html>
