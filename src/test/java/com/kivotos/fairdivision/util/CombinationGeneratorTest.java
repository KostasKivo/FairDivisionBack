package com.kivotos.fairdivision.util;

import com.kivotos.fairdivision.model.Allocation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CombinationGeneratorTest {

    @Test
    void testGenerateAllAllocations_SingleGood_TwoPlayers() {
        int[][] valuationMatrix = {
                {1},
                {1}
        };

        List<List<Allocation>> allAllocations = CombinationGenerator.generateAllAllocations(1, 2, valuationMatrix);

        assertEquals(2, allAllocations.size());

        Allocation allocation1 = allAllocations.get(0).get(0); // First allocation for Player 1
        Allocation allocation2 = allAllocations.get(0).get(1); // First allocation for Player 2

        Allocation allocation3 = allAllocations.get(1).get(0); // Second allocation for Player 1
        Allocation allocation4 = allAllocations.get(1).get(1); // Second allocation for Player 2

        assertEquals(1, allocation1.getGoodsList().size());
        assertEquals(0, allocation2.getGoodsList().size());

        assertEquals(0, allocation3.getGoodsList().size());
        assertEquals(1, allocation4.getGoodsList().size());
    }

    @Test
    void testGenerateAllAllocations_TwoGoods_TwoPlayers() {
        int[][] valuationMatrix = {
                {1, 2},
                {2, 1}
        };

        List<List<Allocation>> allAllocations = CombinationGenerator.generateAllAllocations(2, 2, valuationMatrix);

        assertEquals(4, allAllocations.size());

        // Check that all possible combinations of goods are generated
        for (List<Allocation> allocationPair : allAllocations) {
            assertEquals(2, allocationPair.size());

            Allocation player1Allocation = allocationPair.get(0);
            Allocation player2Allocation = allocationPair.get(1);

            int totalGoods = player1Allocation.getGoodsList().size() + player2Allocation.getGoodsList().size();
            assertEquals(2, totalGoods); // The total number of goods between the two allocations should be 2
        }
    }

    @Test
    void testGenerateAllAllocations_ThreeGoods_TwoPlayers() {
        int[][] valuationMatrix = {
                {1, 2, 3},
                {3, 2, 1}
        };

        List<List<Allocation>> allAllocations = CombinationGenerator.generateAllAllocations(3, 2, valuationMatrix);

        assertEquals(8, allAllocations.size());

        // Verify that all goods are allocated in every possible combination
        for (List<Allocation> allocationPair : allAllocations) {
            assertEquals(2, allocationPair.size());

            Allocation player1Allocation = allocationPair.get(0);
            Allocation player2Allocation = allocationPair.get(1);

            int totalGoods = player1Allocation.getGoodsList().size() + player2Allocation.getGoodsList().size();
            assertEquals(3, totalGoods); // The total number of goods between the two allocations should be 3
        }
    }

    @Test
    void testGenerateAllAllocations_EmptyValuationMatrix_TwoPlayers() {
        int[][] valuationMatrix = {};

        List<List<Allocation>> allAllocations = CombinationGenerator.generateAllAllocations(0, 2, valuationMatrix);

        assertEquals(1, allAllocations.size());

        Allocation player1Allocation = allAllocations.get(0).get(0);
        Allocation player2Allocation = allAllocations.get(0).get(1);

        assertTrue(player1Allocation.getGoodsList().isEmpty());
        assertTrue(player2Allocation.getGoodsList().isEmpty());
    }

    @Test
    void testGenerateAllAllocations_MultipleGoodsWithSameValue_TwoPlayers() {
        int[][] valuationMatrix = {
                {1, 1, 1},
                {1, 1, 1}
        };

        List<List<Allocation>> allAllocations = CombinationGenerator.generateAllAllocations(3, 2, valuationMatrix);

        assertEquals(8, allAllocations.size());

        // Verify that each combination is unique and all goods are allocated
        for (List<Allocation> allocationPair : allAllocations) {
            assertEquals(2, allocationPair.size());

            Allocation player1Allocation = allocationPair.get(0);
            Allocation player2Allocation = allocationPair.get(1);

            int totalGoods = player1Allocation.getGoodsList().size() + player2Allocation.getGoodsList().size();
            assertEquals(3, totalGoods);
        }
    }

    @Test
    void testGenerateAllAllocations_TwoGoods_ThreePlayers() {
        int[][] valuationMatrix = {
                {1, 2},
                {2, 1},
                {1, 1}
        };

        List<List<Allocation>> allAllocations = CombinationGenerator.generateAllAllocations(2, 3, valuationMatrix);

        assertEquals(9, allAllocations.size());

        // Check that all possible combinations of goods are generated
        for (List<Allocation> allocationGroup : allAllocations) {
            assertEquals(3, allocationGroup.size());

            int totalGoods = 0;
            for (Allocation allocation : allocationGroup) {
                totalGoods += allocation.getGoodsList().size();
            }
            assertEquals(2, totalGoods); // The total number of goods between all players should be 2
        }
    }

    @Test
    void testGenerateAllAllocations_ThreeGoods_ThreePlayers() {
        int[][] valuationMatrix = {
                {1, 2, 3},
                {3, 2, 1},
                {2, 3, 1}
        };

        List<List<Allocation>> allAllocations = CombinationGenerator.generateAllAllocations(3, 3, valuationMatrix);

        assertEquals(27, allAllocations.size());

        // Verify that all goods are allocated in every possible combination
        for (List<Allocation> allocationGroup : allAllocations) {
            assertEquals(3, allocationGroup.size());

            int totalGoods = 0;
            for (Allocation allocation : allocationGroup) {
                totalGoods += allocation.getGoodsList().size();
            }
            assertEquals(3, totalGoods); // The total number of goods between all players should be 3
        }
    }
}
