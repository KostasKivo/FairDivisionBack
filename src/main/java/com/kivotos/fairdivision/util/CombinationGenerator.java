package com.kivotos.fairdivision.util;

import com.kivotos.fairdivision.model.Allocation;

import java.util.ArrayList;
import java.util.List;

public class CombinationGenerator {

    public static List<List<Allocation>> generateAllAllocations(int goods, int agents, int[][] valuationMatrix) {
        List<List<Allocation>> allAllocations = new ArrayList<>();
        List<Allocation> currentAllocations = new ArrayList<>();

        // Initialize allocations for each player
        for (int i = 0; i < agents; i++) {
            currentAllocations.add(new Allocation(i));
        }

        generateAllocationsHelper(goods, 0, currentAllocations, valuationMatrix, allAllocations);
        return allAllocations;
    }

    private static void generateAllocationsHelper(int n, int currentGood, List<Allocation> currentAllocations, int[][] valuationMatrix, List<List<Allocation>> allAllocations) {
        if (currentGood == n) {
            // All goods are allocated, add the current allocation to the list
            List<Allocation> finalAllocation = new ArrayList<>();
            for (Allocation allocation : currentAllocations) {
                finalAllocation.add(new Allocation(allocation));  // Copy each player's allocation
            }
            allAllocations.add(finalAllocation);
            return;
        }

        // Try allocating the current good to each player
        for (int i = 0; i < currentAllocations.size(); i++) {
            Allocation newAllocation = new Allocation(currentAllocations.get(i));  // Make a deep copy of the current player's allocation
            newAllocation.add(currentGood, valuationMatrix);  // Add the current good to this player's allocation

            List<Allocation> updatedAllocations = new ArrayList<>(currentAllocations);
            updatedAllocations.set(i, newAllocation);  // Update the current player's allocation

            generateAllocationsHelper(n, currentGood + 1, updatedAllocations, valuationMatrix, allAllocations);
        }
    }
}
