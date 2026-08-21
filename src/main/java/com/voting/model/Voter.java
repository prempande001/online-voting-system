package com.voting.model;

/**
 * Registered voter record. No database - voters live in memory in
 * VoterStore for the lifetime of the JVM, same as candidates/votes.
 */
public class Voter {

    private final int id;
    private final String fullName;
    private final String username;
    private final String passwordHash;
    private boolean hasVoted;

    public Voter(int id, String fullName, String username, String passwordHash) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.passwordHash = passwordHash;
        this.hasVoted = false;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }
}
