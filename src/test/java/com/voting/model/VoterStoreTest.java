package com.voting.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * VoterStore is a process-wide singleton, so each test registers its own
 * uniquely-named voter to stay isolated from other tests in the same run.
 */
class VoterStoreTest {

    private final VoterStore store = VoterStore.getInstance();

    private String uniqueUsername() {
        return "user-" + UUID.randomUUID();
    }

    @Test
    void registerCreatesVoterAndMarksUsernameTaken() {
        String username = uniqueUsername();

        Voter voter = store.register("Full Name", username, "secret");

        assertNotNull(voter);
        assertEquals(username, voter.getUsername());
        assertTrue(store.isUsernameTaken(username));
    }

    @Test
    void isUsernameTakenIsCaseInsensitive() {
        String username = uniqueUsername();
        store.register("Full Name", username, "secret");

        assertTrue(store.isUsernameTaken(username.toUpperCase()));
    }

    @Test
    void registerReturnsNullWhenUsernameAlreadyTaken() {
        String username = uniqueUsername();
        store.register("First", username, "secret");

        Voter second = store.register("Second", username, "other-secret");

        assertNull(second);
    }

    @Test
    void authenticateReturnsVoterForCorrectCredentials() {
        String username = uniqueUsername();
        store.register("Full Name", username, "secret");

        Voter voter = store.authenticate(username, "secret");

        assertNotNull(voter);
        assertEquals(username, voter.getUsername());
    }

    @Test
    void authenticateReturnsNullForWrongPassword() {
        String username = uniqueUsername();
        store.register("Full Name", username, "secret");

        assertNull(store.authenticate(username, "wrong-password"));
    }

    @Test
    void authenticateReturnsNullForUnknownUsername() {
        assertNull(store.authenticate(uniqueUsername(), "whatever"));
    }

    @Test
    void resetVotedFlagsClearsHasVotedForAllVoters() {
        String username = uniqueUsername();
        Voter voter = store.register("Full Name", username, "secret");
        voter.setHasVoted(true);

        store.resetVotedFlags();

        assertFalse(voter.isHasVoted());
    }
}
