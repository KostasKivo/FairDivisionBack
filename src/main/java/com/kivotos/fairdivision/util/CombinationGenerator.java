package com.kivotos.fairdivision.util;

import com.kivotos.fairdivision.model.Allocation;

import java.util.ArrayList;
import java.util.List;

public class CombinationGenerator {

    public static List<List<Allocation>> generateAllAllocations(int n, int[][] valuationMatrix) {
        List<List<Allocation>> allAllocations = new ArrayList<>();
        generateAllocationsHelper(n, 0, new Allocation(0), new Allocation(1), valuationMatrix, allAllocations);
        return allAllocations;
    }

    private static void generateAllocationsHelper(int n, int currentGood, Allocation player1Allocation, Allocation player2Allocation, int[][] valuationMatrix, List<List<Allocation>> allAllocations) {
        if (currentGood == n) {
            // Both allocations are complete, add them to the result list
            List<Allocation> finalAllocation = new ArrayList<>();
            finalAllocation.add(new Allocation(player1Allocation));  // Copy allocation for player 1
            finalAllocation.add(new Allocation(player2Allocation));  // Copy allocation for player 2
            allAllocations.add(finalAllocation);
            return;
        }

        // Allocate the current good to Player 1
        Allocation newPlayer1Allocation = new Allocation(player1Allocation);  // Make a deep copy
        newPlayer1Allocation.add(currentGood, valuationMatrix);
        generateAllocationsHelper(n, currentGood + 1, newPlayer1Allocation, player2Allocation, valuationMatrix, allAllocations);

        // Allocate the current good to Player 2
        Allocation newPlayer2Allocation = new Allocation(player2Allocation);  // Make a deep copy
        newPlayer2Allocation.add(currentGood, valuationMatrix);
        generateAllocationsHelper(n, currentGood + 1, player1Allocation, newPlayer2Allocation, valuationMatrix, allAllocations);
    }
}
