package com.kivotos.fairdivision.algorithms;

import com.kivotos.fairdivision.model.Allocation;
import com.kivotos.fairdivision.model.FairDivisionInput;
import com.kivotos.fairdivision.model.FairDivisionOutput;
import com.kivotos.fairdivision.util.MaximumMatching;
import com.kivotos.fairdivision.util.ValuationChecker;

import java.util.*;


public class MatchAndFreezeAlgorithm implements FairDivisionAlgorithm {
    @Override
    public FairDivisionOutput allocate(FairDivisionInput fairDivisionInput) {

        int agents = fairDivisionInput.getAgentNumber();
        int goods = fairDivisionInput.getGoodsNumber();
        int[][] valuationMatrix = fairDivisionInput.getValuationMatrix();

        int [] valuesArray = findTwoValues(valuationMatrix);

        if(valuesArray==null)
            return new FairDivisionOutput("Not a binary instance.");

        int aValue = valuesArray[0];
        int bValue = valuesArray[1];

        List<Allocation> allocationsList = new ArrayList<>();
        for (int i = 0; i < agents; i++) {
            allocationsList.add(new Allocation(i));
        }

        Set<Integer> activeAgents = new HashSet<>();
        for (int i = 0; i < agents; i++) {
            activeAgents.add(i);
        }

        Set<Integer> availableGoods = new HashSet<>();
        for (int i = 0; i < goods; i++) {
            availableGoods.add(i);
        }

        LinkedList<Integer> lambaList = new LinkedList<>();
        for (int i = 0; i < agents; i++) {
            lambaList.add(i);
        }

        Map<Integer,Integer> frozenAgents = new HashMap<>();

        int round = 0;

        while(!availableGoods.isEmpty()) {

            // Check if any agent unfroze
            checkFrozenAgents(frozenAgents,activeAgents,round);

            // Create bipartite adjacency matrix
            boolean[][] adjacencyMatrix = createBipartiteAdjacencyMatrix(agents,goods, activeAgents, availableGoods, aValue , valuationMatrix);

            // Each array contains 2 elements, arr[0] the agent and arr[1] the item he gets
            List<int[]> maximumMatching = MaximumMatching.maxBPM(adjacencyMatrix,agents,goods);

            // Allocate through matching - Map holds the agent,receivedGood
            Map<Integer,Integer> macthedAgents = allocateThroughMaximumMatching(maximumMatching,allocationsList,valuationMatrix,availableGoods);

            // Allocate to unmatched agents
            allocateToUnmatchedAgents(activeAgents,macthedAgents,allocationsList,valuationMatrix,availableGoods,lambaList);

            // Identify freezing agents - The ones that got their good from maximum matching so the matchedAgents

            //Remove agents of F from L for the next ⌊a/b − 1⌋ rounds.
            removeFreezingAgents(macthedAgents,frozenAgents,activeAgents,valuationMatrix,aValue,bValue);

            // Put agents of F to the end of ℓ.
            putFrozenAgentsAtTheEnd(frozenAgents,lambaList);

            round++;
        }


        return new FairDivisionOutput(allocationsList,valuationMatrix);
    }


    public static int[] findTwoValues(int[][] matrix) {
        Set<Integer> distinctValues = new TreeSet<>(); // Using TreeSet to maintain order

        for (int[] row : matrix) {
            for (int value : row) {
                distinctValues.add(value);
                if (distinctValues.size() > 2) {
                    return null; // More than 2 distinct values found
                }
            }
        }

        if (distinctValues.size() == 2) {
            Iterator<Integer> iterator = distinctValues.iterator();
            int b = iterator.next();
            int a = iterator.next();
            if (a > b && b >= 0) {
                return new int[]{a, b};
            }
        }

        return null;
    }

    private boolean [][] createBipartiteAdjacencyMatrix(int agents, int goods, Set<Integer> activeAgents,Set<Integer> availableGoods, int aValue, int [][] valuationMatrix ) {
        boolean[][] adjacencyMatrix = new boolean[agents][goods];

        for (int agent: activeAgents) {
            for (int good : availableGoods) {
                if(ValuationChecker.getValuationMatrixAtIndex(agent,good,valuationMatrix)== aValue && isAgentActive(agent,activeAgents)) {
                    adjacencyMatrix[agent][good] = true;
                }
            }
        }

        return adjacencyMatrix;
    }

    private Map<Integer,Integer> allocateThroughMaximumMatching(List<int[]> maximumMatching, List<Allocation> allocationsList, int[][] valuationMatrix, Set<Integer> availableGoods) {
        Map<Integer,Integer> matchedAgents = new HashMap<>();

        for(int[] arr: maximumMatching) {
            allocationsList.get(arr[0]).add(arr[1],valuationMatrix);
            availableGoods.remove(arr[1]);
            matchedAgents.put(arr[0],arr[1]);
        }

        return matchedAgents;
    }

    private void allocateToUnmatchedAgents(Set<Integer> activeAgents, Map<Integer,Integer> matchedAgents, List<Allocation> allocationsList, int[][] valuationMatrix, Set<Integer> availableGoods, LinkedList<Integer> lambdaList) {
        // Iterate over the lambdaList to find unmatched agents who are also active
        Iterator<Integer> iterator = lambdaList.iterator();
        while (iterator.hasNext() && !availableGoods.isEmpty()) {
            int agent = iterator.next();

            // Check if the agent is unmatched and active
            if (!matchedAgents.containsKey(agent) && activeAgents.contains(agent)) {
                // Get the first available good
                Iterator<Integer> goodsIterator = availableGoods.iterator();
                if (goodsIterator.hasNext()) {
                    int good = goodsIterator.next();
                    goodsIterator.remove(); // Remove the allocated good from availableGoods

                    // Update the allocation for the agent
                    allocationsList.get(agent).add(good, valuationMatrix);
                    availableGoods.remove(good);
                }
            }
        }
    }

    private boolean isAgentActive(int agent, Set<Integer> activeAgents) {
        return activeAgents.contains(agent);
    }

    private void checkFrozenAgents(Map<Integer, Integer> removedAgents, Set<Integer> activeAgents, int round) {
        for(Integer agent : removedAgents.keySet()) {
            if(removedAgents.get(agent)==round) {
                removedAgents.remove(agent);
                activeAgents.add(agent);
            }
        }
    }


    private void removeFreezingAgents(Map<Integer, Integer> matchedAgents, Map<Integer, Integer> frozenAgents, Set<Integer> activeAgents, int[][] valuationMatrix, int aValue, int bValue) {
        // Step 1: Identify set Z (agents not matched to any good of value a)
        Set<Integer> unmatchedAgents = new HashSet<>(activeAgents);
        unmatchedAgents.removeAll(matchedAgents.keySet());


        // Step 2: Construct the set F (agents that need to freeze)
        for (Integer unmatchedAgent : unmatchedAgents) {
            for (Map.Entry<Integer, Integer> entry : matchedAgents.entrySet()) {
                int matchedAgent = entry.getKey();
                int matchedGood = entry.getValue();

                if (valuationMatrix[unmatchedAgent][matchedGood] == aValue) {
                    frozenAgents.put(matchedAgent, (int) Math.floor(aValue/bValue -1));
                    activeAgents.remove(matchedAgent);
                }
            }
        }
    }


    private void putFrozenAgentsAtTheEnd(Map<Integer, Integer> frozenAgents, LinkedList<Integer> lambdaList) {
        for (Integer agent : frozenAgents.keySet()) {
            lambdaList.remove(agent);
            lambdaList.addLast(agent);
        }
    }


}
