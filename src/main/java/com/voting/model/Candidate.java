package com.voting.model;

/**
 * Simple immutable candidate record.
 * No database - candidates are seeded in memory by VoteStore.
 */
public class Candidate {

    private final int id;
    private final String name;
    private final String party;

    public Candidate(int id, String name, String party) {
        this.id = id;
        this.name = name;
        this.party = party;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }
}
