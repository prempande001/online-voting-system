<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.voting.model.Candidate" %>
<%@ page import="com.voting.model.VoteStore" %>
<%@ page import="com.voting.model.Voter" %>
<%@ page import="java.util.Map" %>
<%
    Voter resultsVoter = (Voter) session.getAttribute("voter");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Voting Results</title>
    <link rel="stylesheet" href="css/style.css">
    <meta http-equiv="refresh" content="10">
</head>
<body>
<div class="container">
    <% if (resultsVoter != null) { %>
    <div class="welcome-bar">
        <span>Welcome, <strong><%= resultsVoter.getFullName() %></strong></span>
        <a href="logout" class="logout-link">Log out</a>
    </div>
    <% } %>

    <h1>Live Voting Results</h1>

    <%
        if ("true".equals(request.getParameter("voted"))) {
    %>
        <p class="success">Thanks for voting!</p>
    <%
        } else if ("true".equals(request.getParameter("alreadyVoted"))) {
    %>
        <p class="alert">You have already voted.</p>
    <%
        }

        VoteStore store = VoteStore.getInstance();
        Map<Integer, Candidate> candidates = store.getCandidates();
        int totalVotes = store.getTotalVotes();
    %>

    <table class="results-table">
        <thead>
        <tr>
            <th>Candidate</th>
            <th>Party</th>
            <th>Votes</th>
            <th>Share</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Candidate c : candidates.values()) {
                int count = store.getVoteCount(c.getId());
                double pct = totalVotes == 0 ? 0 : (count * 100.0 / totalVotes);
        %>
        <tr>
            <td><%= c.getName() %></td>
            <td><%= c.getParty() %></td>
            <td><%= count %></td>
            <td><%= String.format("%.1f", pct) %>%</td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>

    <p>Total votes cast: <strong><%= totalVotes %></strong></p>
    <% if (resultsVoter != null) { %>
    <p class="results-link"><a href="index.jsp">&larr; Back to voting</a></p>
    <% } else { %>
    <p class="results-link"><a href="login.jsp">Log in to vote</a></p>
    <% } %>
</div>
</body>
</html>
