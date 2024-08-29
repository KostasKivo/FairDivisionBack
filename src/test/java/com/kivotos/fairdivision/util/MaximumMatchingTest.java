package com.kivotos.fairdivision.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MaximumMatchingTest {

    @Test
    void testMaxBPM_SimpleCase() {
        boolean[][] adjacencyMatrix = {
                {true, false, false},
                {false, true, false},
                {false, false, true}
        };

        List<int[]> matching = MaximumMatching.maxBPM(adjacencyMatrix, 3, 3);

        // Expect each agent to be matched with their corresponding good
        assertEquals(3, matching.size());
        assertTrue(containsPair(matching, 0, 0));
        assertTrue(containsPair(matching, 1, 1));
        assertTrue(containsPair(matching, 2, 2));
    }

    @Test
    void testMaxBPM_MultipleOptions() {
        boolean[][] adjacencyMatrix = {
                {true, true, false},
                {false, true, true},
                {true, false, true}
        };

        List<int[]> matching = MaximumMatching.maxBPM(adjacencyMatrix, 3, 3);

        // We expect a perfect matching with 3 pairs
        assertEquals(3, matching.size());
        // Each good should be assigned to one agent, the specific pairs can vary
        assertTrue(isValidMatching(matching, 3, 3));
    }

    @Test
    void testMaxBPM_NoMatchingPossible() {
        boolean[][] adjacencyMatrix = {
                {false, false, false},
                {false, false, false},
                {false, false, false}
        };

        List<int[]> matching = MaximumMatching.maxBPM(adjacencyMatrix, 3, 3);

        // Expect no matching pairs because there are no connections
        assertEquals(0, matching.size());
    }

    @Test
    void testMaxBPM_OneSidedCase() {
        boolean[][] adjacencyMatrix = {
                {true, true, true},
                {false, false, false},
                {false, false, false}
        };

        List<int[]> matching = MaximumMatching.maxBPM(adjacencyMatrix, 3, 3);

        // Expect only one pair because only the first agent can be matched
        assertEquals(1, matching.size());
        assertTrue(containsPair(matching, 0, 0));
    }

    @Test
    void testMaxBPM_UnequalAgentsAndGoods() {
        boolean[][] adjacencyMatrix = {
                {true, true, false, false},
                {false, true, true, true}
        };

        List<int[]> matching = MaximumMatching.maxBPM(adjacencyMatrix, 2, 4);

        // We expect a maximum of 2 matches, as we only have 2 agents
        assertEquals(2, matching.size());
        // Check that the matches are valid
        assertTrue(isValidMatching(matching, 2, 4));
    }

    /**
     * Helper method to check if the matching contains a specific pair.
     */
    private boolean containsPair(List<int[]> matching, int agent, int good) {
        for (int[] pair : matching) {
            if (pair[0] == agent && pair[1] == good) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method to validate that the matching is valid:
     * 1. No good is matched to more than one agent.
     * 2. No agent is matched to more than one good.
     */
    private boolean isValidMatching(List<int[]> matching, int agents, int goods) {
        boolean[] agentUsed = new boolean[agents];
        boolean[] goodUsed = new boolean[goods];

        for (int[] pair : matching) {
            if (agentUsed[pair[0]] || goodUsed[pair[1]]) {
                return false;
            }
            agentUsed[pair[0]] = true;
            goodUsed[pair[1]] = true;
        }
        return true;
    }
}
