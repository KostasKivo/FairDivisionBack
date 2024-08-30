package com.kivotos.fairdivision.algorithms;

import com.kivotos.fairdivision.model.Allocation;
import com.kivotos.fairdivision.model.FairDivisionInput;
import com.kivotos.fairdivision.model.FairDivisionOutput;
import com.kivotos.fairdivision.util.AgentUtility;
import com.kivotos.fairdivision.util.CombinationGenerator;
import com.kivotos.fairdivision.util.ValuationChecker;

import java.util.*;


public class LeximinPlusPlusAlgorithm implements FairDivisionAlgorithm {

    @Override
    public FairDivisionOutput allocate(FairDivisionInput fairDivisionInput) {

        if(fairDivisionInput.getAgentNumber() > 3 || fairDivisionInput.getGoodsNumber() > 12)
            return new FairDivisionOutput("There was a problem with the input, try smaller values of agents or goods.");

        List<List<Allocation>> allocations = CombinationGenerator.generateAllAllocations(fairDivisionInput.getGoodsNumber(), fairDivisionInput.getAgentNumber() ,fairDivisionInput.getValuationMatrix());

        // Holds the increasing order of players indexes of the lowest to highest allocation
        List<List<Integer>> increasingOrderingForEachPlayerAndAllocation = new ArrayList<>();
        for(List<Allocation> l : allocations)
            increasingOrderingForEachPlayerAndAllocation.add(calculateIncreasingOrdering(l,fairDivisionInput.getValuationMatrix()));

        List<Allocation> retAllocation = findLeximinPlusPlusAllocation(allocations,increasingOrderingForEachPlayerAndAllocation,fairDivisionInput.getValuationMatrix());

        return new FairDivisionOutput(retAllocation,fairDivisionInput.getValuationMatrix());

    }


    private List<Integer> calculateIncreasingOrdering(List<Allocation> allocations, int[][] valuationMatrix) {
        List<Integer> ordering = new ArrayList<>();
        List<AgentUtility> agentUtilities = new ArrayList<>();

        // Calculate utility for each agent
        for (int i = 0; i < allocations.size(); i++) {
            int utility = ValuationChecker.getValuation(i, allocations.get(i).getGoodsList(),valuationMatrix);
            int bundleSize = allocations.get(i).getGoodsList().size(); // Assuming allocation is a list of items
            agentUtilities.add(new AgentUtility(i, utility, bundleSize));
        }

        // Sort agents by utility, then by bundle size if there are ties
        Collections.sort(agentUtilities, new Comparator<AgentUtility>() {
            @Override
            public int compare(AgentUtility a1, AgentUtility a2) {
                if (a1.getUtility() != a2.getUtility()) {
                    return Integer.compare(a1.getUtility(), a2.getUtility());
                } else {
                    return Integer.compare(a1.getBundleSize(), a2.getBundleSize());
                }
            }
        });

        // Create the ordering list based on sorted utilities
        for (AgentUtility agentUtility : agentUtilities) {
            ordering.add(agentUtility.getAgentIndex());
        }

        return ordering;
    }


    public List<Allocation> findLeximinPlusPlusAllocation(List<List<Allocation>> allocations, List<List<Integer>> increasingOrdering, int[][] valuationMatrix) {

        int currentColumn = 0;

        // Stop when we have only one allocation remaining, or if we've processed all columns
        while (allocations.size() > 1 && currentColumn < increasingOrdering.get(0).size()) {

            int maxValue = Integer.MIN_VALUE;
            int maxBundleSize = 0;
            Set<Integer> maxAllocationIndexes = new HashSet<>();

            // Find max allocations
            for (int i = 0; i < increasingOrdering.size(); i++) {

                int currentPlayerIndex = increasingOrdering.get(i).get(currentColumn);

                Allocation currentAllocation = allocations.get(i).get(currentPlayerIndex);

                int allocationValue = ValuationChecker.getValuation(currentAllocation.getAgentId(), currentAllocation.getGoodsList(), valuationMatrix);
                int allocationSize = currentAllocation.getGoodsList().size();

                // Update max allocations based on value, then size
                if (allocationValue > maxValue) {
                    maxValue = allocationValue;
                    maxBundleSize = allocationSize;
                    maxAllocationIndexes.clear();
                    maxAllocationIndexes.add(i);
                } else if (allocationValue == maxValue) {
                    if (allocationSize > maxBundleSize) {
                        maxBundleSize = allocationSize;
                        maxAllocationIndexes.clear();
                        maxAllocationIndexes.add(i);
                    } else if (allocationSize == maxBundleSize) {
                        maxAllocationIndexes.add(i);
                    }
                }
            }

            // Filter allocations and increasingOrdering based on maxAllocationIndexes
            List<List<Allocation>> filteredAllocations = new ArrayList<>();
            List<List<Integer>> filteredOrdering = new ArrayList<>();
            for (int index : maxAllocationIndexes) {
                filteredAllocations.add(allocations.get(index));
                filteredOrdering.add(increasingOrdering.get(index));
            }

            allocations = filteredAllocations;
            increasingOrdering = filteredOrdering;

            currentColumn++;
        }


        // Select a random index if there are multiple remaining allocations
        Random random = new Random();
        int randomIndex = random.nextInt(allocations.size());
        return allocations.get(randomIndex);
    }



}
