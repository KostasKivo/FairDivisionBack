package com.kivotos.fairdivision.algorithms;

import com.kivotos.fairdivision.model.Allocation;
import com.kivotos.fairdivision.model.FairDivisionInput;
import com.kivotos.fairdivision.model.FairDivisionOutput;
import com.kivotos.fairdivision.util.ValuationChecker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IdenticalValuationAlgorithm implements FairDivisionAlgorithm {

    @Override
    public FairDivisionOutput allocate(FairDivisionInput fairDivisionInput) {
        int agents = fairDivisionInput.getAgentNumber();
        int goods = fairDivisionInput.getGoodsNumber();
        int[][] valuationMatrix = fairDivisionInput.getValuationMatrix();

        // Initialize allocations for each agent
        List<Allocation> allocationsList = new ArrayList<>();
        for (int i = 0; i < agents; i++) {
            allocationsList.add(new Allocation(i));
        }

        // Initialize available goods with identifiers
        Set<Integer> availableGoods = new HashSet<>();
        for (int i = 0; i < goods; i++) {
            availableGoods.add(i);
        }

        // Perform allocation, giving the most valuable item to the agent with the smallest allocation
        while (!availableGoods.isEmpty()) {
            Allocation smallestAllocation = findSmallestValuedAllocation(allocationsList, valuationMatrix);

            // Get the current agent's ID
            int agentId = smallestAllocation.getAgentId();

            int selectedGood = -1;
            int maxValuation = Integer.MIN_VALUE;

            // Find the most valuable good for the agent with the smallest allocation
            for (int good : availableGoods) {
                int valuation = valuationMatrix[agentId][good];
                if (valuation > maxValuation) {
                    maxValuation = valuation;
                    selectedGood = good;
                }
            }

            // Allocate the selected good to the current agent
            smallestAllocation.add(selectedGood, valuationMatrix);
            availableGoods.remove(selectedGood);
        }

        return new FairDivisionOutput(allocationsList, valuationMatrix);
    }

    // Find the agent with the smallest total allocation value
    public static Allocation findSmallestValuedAllocation(List<Allocation> allocations, int[][] valuationMatrix) {
        if (allocations == null || allocations.isEmpty()) {
            throw new IllegalArgumentException("Allocations list cannot be null or empty.");
        }

        // Initialize the smallest allocation and its value
        Allocation smallestAllocation = allocations.get(0);
        int smallestValue = ValuationChecker.getValuation(smallestAllocation.getAgentId(), smallestAllocation.getGoodsList(), valuationMatrix);

        // Loop through each allocation to find the one with the smallest value
        for (Allocation allocation : allocations) {
            int allocationValue = ValuationChecker.getValuation(allocation.getAgentId(), allocation.getGoodsList(), valuationMatrix);

            // If current allocation has a smaller value, update the smallest allocation
            if (allocationValue < smallestValue) {
                smallestAllocation = allocation;
                smallestValue = allocationValue;
            }
        }

        return smallestAllocation;
    }
}
