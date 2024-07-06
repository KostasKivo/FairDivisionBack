package com.kivotos.fairdivision.algorithms;

import com.kivotos.fairdivision.model.Allocation;
import com.kivotos.fairdivision.model.FairDivisionOutput;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoundRobinAlgorithm implements FairDivisionAlgorithm {

    @Override
    public FairDivisionOutput allocate(int agents, int goods, int[][] valuationMatrix) {
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

        // Perform Round-Robin allocation
        for (int l = 0; l < goods; l++) {
            int agentIndex = l % agents;
            int selectedGood = -1;
            int maxValuation = Integer.MIN_VALUE;

            // Find the good with the highest valuation for the current agent
            for (int good : availableGoods) {
                if (valuationMatrix[agentIndex][good] > maxValuation) {
                    maxValuation = valuationMatrix[agentIndex][good];
                    selectedGood = good;
                }
            }

            // Allocate the selected good to the current agent with identifier g(selectedGood+1)
            allocationsList.get(agentIndex).add(selectedGood,valuationMatrix);
            availableGoods.remove(selectedGood);
        }

        return new FairDivisionOutput(allocationsList);
    }
}
