package com.voting.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory vote storage (singleton, thread-safe).
 *
 * NOTE: This is intentionally NOT backed by a database. All data
 * (candidates + vote counts) lives in JVM memory and resets whenever
 * the application restarts. That's fine for this basic stage - a
 * database can be added later once the DevOps pipeline is in place.
 */
public class VoteStore {

    private static final VoteStore INSTANCE = new VoteStore();

    private final Map<Integer, Candidate> candidates = new LinkedHashMap<>();
    private final Map<Integer, AtomicInteger> votes = new LinkedHashMap<>();

    private VoteStore() {
        seedCandidates();
    }

    public static VoteStore getInstance() {
        return INSTANCE;
    }

    private void seedCandidates() {
        addCandidate(new Candidate(1, "Alice Johnson", "Green Party"));
        addCandidate(new Candidate(2, "Bob Martinez", "Blue Party"));
        addCandidate(new Candidate(3, "Carla Singh", "Yellow Party"));
    }

    private void addCandidate(Candidate c) {
        candidates.put(c.getId(), c);
        votes.put(c.getId(), new AtomicInteger(0));
    }

    public Map<Integer, Candidate> getCandidates() {
        return candidates;
    }

    /**
     * Registers a vote for the given candidate.
     * @return true if the candidate exists and the vote was recorded
     */
    public boolean castVote(int candidateId) {
        AtomicInteger count = votes.get(candidateId);
        if (count == null) {
            return false;
        }
        count.incrementAndGet();
        return true;
    }

    public int getVoteCount(int candidateId) {
        AtomicInteger count = votes.get(candidateId);
        return count == null ? 0 : count.get();
    }

    public int getTotalVotes() {
        return votes.values().stream().mapToInt(AtomicInteger::get).sum();
    }

    /**
     * Resets all vote counts back to zero. Used only by the /reset
     * endpoint to make local testing/demoing easier - remove this
     * once you move past the basic practice stage.
     */
    public void resetVotes() {
        votes.values().forEach(counter -> counter.set(0));
    }
}
