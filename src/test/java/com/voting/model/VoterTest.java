package com.voting.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VoterTest {

    @Test
    void constructorSetsFieldsAndDefaultsHasVotedToFalse() {
        Voter voter = new Voter(1, "Alice Example", "alice", "hashedpw");

        assertEquals(1, voter.getId());
        assertEquals("Alice Example", voter.getFullName());
        assertEquals("alice", voter.getUsername());
        assertEquals("hashedpw", voter.getPasswordHash());
        assertFalse(voter.isHasVoted());
    }

    @Test
    void setHasVotedUpdatesFlag() {
        Voter voter = new Voter(2, "Bob Example", "bob", "hashedpw");

        voter.setHasVoted(true);

        assertTrue(voter.isHasVoted());
    }
}
