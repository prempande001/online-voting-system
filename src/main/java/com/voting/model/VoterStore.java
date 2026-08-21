package com.voting.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory voter registry (singleton, thread-safe), mirroring the
 * pattern used by VoteStore. No database - registered voters live in
 * JVM memory and are lost on restart, which is fine for this stage.
 */
public class VoterStore {

    private static final VoterStore INSTANCE = new VoterStore();

    private final Map<String, Voter> votersByUsername = new LinkedHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    private VoterStore() {
    }

    public static VoterStore getInstance() {
        return INSTANCE;
    }

    public synchronized boolean isUsernameTaken(String username) {
        return votersByUsername.containsKey(username.toLowerCase());
    }

    /**
     * Registers a new voter.
     * @return the created Voter, or null if the username is already taken
     */
    public synchronized Voter register(String fullName, String username, String password) {
        String key = username.toLowerCase();
        if (votersByUsername.containsKey(key)) {
            return null;
        }
        Voter voter = new Voter(nextId.getAndIncrement(), fullName, username, hash(password));
        votersByUsername.put(key, voter);
        return voter;
    }

    /**
     * @return the matching Voter if the username/password are valid, otherwise null
     */
    public synchronized Voter authenticate(String username, String password) {
        Voter voter = votersByUsername.get(username.toLowerCase());
        if (voter != null && voter.getPasswordHash().equals(hash(password))) {
            return voter;
        }
        return null;
    }

    /**
     * Resets every registered voter's hasVoted flag. Used only by the
     * /reset endpoint to make local testing/demoing easier.
     */
    public synchronized void resetVotedFlags() {
        votersByUsername.values().forEach(v -> v.setHasVoted(false));
    }

    private static String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                sb.append(Character.forDigit((b >> 4) & 0xF, 16));
                sb.append(Character.forDigit(b & 0xF, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
