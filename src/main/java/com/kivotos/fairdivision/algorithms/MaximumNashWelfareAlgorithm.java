package com.kivotos.fairdivision.algorithms;

import com.kivotos.fairdivision.model.Allocation;
import com.kivotos.fairdivision.model.FairDivisionInput;
import com.kivotos.fairdivision.model.FairDivisionOutput;
import com.kivotos.fairdivision.util.AgentUtility;
import com.kivotos.fairdivision.util.CombinationGenerator;
import com.kivotos.fairdivision.util.NashSocialWelfareHelper;

import java.util.*;

/**
 * This class implements the Maximum Nash Welfare (MNW) algorithm.
 * The MNW algorithm aims to achieve EF1 and PO by maximizing the product of utilities across all agents.
 */
public class MaximumNashWelfareAlgorithm implements FairDivisionAlgorithm {

    @Override
    public FairDivisionOutput allocate(FairDivisionInput fairDivisionInput) {
        Set<Integer> players = calculateLargestSetOfPlayersWithPositiveUtility(fairDivisionInput.getValuationMatrix());

        // Create a map to track original agent indices
        Map<Integer, Integer> originalIndexMap = new HashMap<>();
        int[][] newValuationMatrix = createFilteredValuationMatrix(players, fairDivisionInput.getValuationMatrix(), originalIndexMap);

        // Generate all possible allocations for the remaining agents
        List<List<Allocation>> allocations = CombinationGenerator.generateAllAllocations(newValuationMatrix[0].length, newValuationMatrix.length, newValuationMatrix);

        // Find the allocation that maximizes the Nash Social Welfare
        List<Allocation> maximumNashWelfareAllocation = findMaxNashSocialWelfareAllocation(allocations, newValuationMatrix, originalIndexMap);

        return new FairDivisionOutput(maximumNashWelfareAllocation, fairDivisionInput.getValuationMatrix());
    }

    public Set<Integer> calculateLargestSetOfPlayersWithPositiveUtility(int[][] valuationMatrix) {
        int agents = valuationMatrix.length;
        int goods = valuationMatrix[0].length;

        if (agents <= goods) {
            Set<Integer> allAgents = new HashSet<>();
            for (int i = 0; i < agents; i++) {
                allAgents.add(i);
            }
            return allAgents;
        } else {
            List<AgentUtility> agentUtilities = new ArrayList<>();

            // Calculate the product of valuations for each agent
            for (int i = 0; i < agents; i++) {
                int product = 1;
                for (int j = 0; j < goods; j++) {
                    product *= valuationMatrix[i][j];
                }
                agentUtilities.add(new AgentUtility(i, product));
            }

            // Sort agents by the product of valuations in descending order
            agentUtilities.sort((a1, a2) -> Integer.compare(a2.getProduct(), a1.getProduct()));

            // Select the top `goods` agents
            Set<Integer> selectedAgents = new HashSet<>();
            for (int i = 0; i < goods; i++) {
                selectedAgents.add(agentUtilities.get(i).getAgentIndex());
            }

            return selectedAgents;
        }
    }

    public static int[][] createFilteredValuationMatrix(Set<Integer> players, int[][] valuationMatrix, Map<Integer, Integer> originalIndexMap) {
        // Convert the set to a list for easy access
        List<Integer> playerList = new ArrayList<>(players);

        // Sort the list to ensure the order of the resulting rows
        Collections.sort(playerList);

        // Initialize the new matrix with the appropriate size
        int[][] newMatrix = new int[playerList.size()][valuationMatrix[0].length];

        // Copy the rows from the original matrix to the new matrix
        for (int i = 0; i < playerList.size(); i++) {
            int originalIndex = playerList.get(i);
            newMatrix[i] = valuationMatrix[originalIndex];
            originalIndexMap.put(i, originalIndex);  // Map the new index to the original index
        }

        return newMatrix;
    }

    public static List<Allocation> findMaxNashSocialWelfareAllocation(List<List<Allocation>> allocations, int[][] valuationMatrix, Map<Integer, Integer> originalIndexMap) {
        double max = -1;
        List<Allocation> maxAllocation = null;

        for (List<Allocation> allocationList : allocations) {
            double currentNashWelfare = NashSocialWelfareHelper.calculateNashSocialWelfare(allocationList, valuationMatrix);
            if (currentNashWelfare > max) {
                max = currentNashWelfare;
                maxAllocation = allocationList;
            }
        }

        // Adjust the allocation list to use the original agent indices
        for (Allocation allocation : maxAllocation) {
            int originalIndex = originalIndexMap.get(allocation.getAgentId());
            allocation.setAgentId(originalIndex);
        }

        return maxAllocation;
    }
}
