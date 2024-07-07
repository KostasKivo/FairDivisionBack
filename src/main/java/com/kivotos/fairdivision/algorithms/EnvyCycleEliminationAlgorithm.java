package com.kivotos.fairdivision.algorithms;

import com.kivotos.fairdivision.model.Allocation;
import com.kivotos.fairdivision.model.FairDivisionOutput;
import com.kivotos.fairdivision.util.ValuationChecker;

import java.util.*;


public class EnvyCycleEliminationAlgorithm implements FairDivisionAlgorithm {

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

        for (int l = 0; l < goods; l++) {
            int[][] adjacencyMatrix = createAdjacencyMatrix(allocationsList);
            int unenviedAgentIndex = returnUnenviedAgentIndex(adjacencyMatrix);

            // If there is no unenvied agent, find the cycle and change it
            if (unenviedAgentIndex == -1) {
                List<Integer> envyCycle = findCycle(adjacencyMatrix);
                resolveCycle(allocationsList, envyCycle);
                adjacencyMatrix = createAdjacencyMatrix(allocationsList); // Recompute the adjacency matrix after resolving the cycle
                unenviedAgentIndex = returnUnenviedAgentIndex(adjacencyMatrix); // Update the unenvied agent after resolving the cycle
            }


            // Give them their argmax item

            int selectedGood = -1;
            int maxValuation = Integer.MIN_VALUE;

            // Find the good with the highest valuation for the current agent
            for (int good : availableGoods) {
                if (valuationMatrix[unenviedAgentIndex][good] > maxValuation) {
                    maxValuation = valuationMatrix[unenviedAgentIndex][good];
                    selectedGood = good;
                }
            }

            // Allocate the selected good to the current agent with identifier g(selectedGood+1)
            allocationsList.get(unenviedAgentIndex).add(selectedGood, valuationMatrix);
            availableGoods.remove(selectedGood);

        }

        return new FairDivisionOutput(allocationsList);
    }

    private int[][] createAdjacencyMatrix(List<Allocation> allocationList) {
        int numAgents = allocationList.size();
        int[][] adjacencyMatrix = new int[numAgents][numAgents];

        for (int i = 0; i < numAgents; i++) {
            for (int j = 0; j < numAgents; j++) {
                if (i != j) {
                    int agentIValue = ValuationChecker.getValuation(allocationList.get(i).getAgentId(), allocationList.get(i).getGoodsList());
                    int agentJValue = ValuationChecker.getValuation(allocationList.get(i).getAgentId(), allocationList.get(j).getGoodsList());

                    if (agentIValue < agentJValue) {
                        adjacencyMatrix[i][j] = 1;
                    }
                }
            }
        }

        return adjacencyMatrix;
    }

    private int returnUnenviedAgentIndex(int[][] adjacencyMatrix) {
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            boolean isEnvied = false;
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                if (!isEnvied && adjacencyMatrix[j][i] == 1) {
                    isEnvied = true;
                    break;
                }
            }

            if (!isEnvied) {
                return i;
            }
        }
        return -1;
    }

    private List<Integer> findCycle(int[][] adjacencyMatrix) {
        int numAgents = adjacencyMatrix.length;
        boolean[] visited = new boolean[numAgents];
        boolean[] inStack = new boolean[numAgents];
        List<Integer> cycle = new ArrayList<>();

        for (int i = 0; i < numAgents; i++) {
            if (findCycleUtil(i, adjacencyMatrix, visited, inStack, cycle)) {
                Collections.reverse(cycle);
                return cycle;
            }
        }
        return cycle;
    }

    private boolean findCycleUtil(int v, int[][] adjacencyMatrix, boolean[] visited, boolean[] inStack, List<Integer> cycle) {
        if (inStack[v]) {
            cycle.add(v);
            return true;
        }

        if (visited[v]) {
            return false;
        }

        visited[v] = true;
        inStack[v] = true;

        for (int i = 0; i < adjacencyMatrix.length; i++) {
            if (adjacencyMatrix[v][i] == 1 && findCycleUtil(i, adjacencyMatrix, visited, inStack, cycle)) {
                cycle.add(v);
                return true;
            }
        }

        inStack[v] = false;
        return false;
    }

    private void resolveCycle(List<Allocation> allocationsList, List<Integer> envyCycle) {
        if (envyCycle.isEmpty()) return;

        List<List<Integer>> tempAllocations = new ArrayList<>();
        for (int agent : envyCycle) {
            tempAllocations.add(new ArrayList<>(allocationsList.get(agent).getGoodsList()));
        }

        for (int i = 0; i < envyCycle.size(); i++) {
            int nextAgent = (i + 1) % envyCycle.size();
            allocationsList.get(envyCycle.get(i)).setGoodsList(tempAllocations.get(nextAgent));
        }
    }

    public static void printTwoDimensionalArray(int[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                System.out.printf("%d ", a[i][j]);
            }
            System.out.println();
        }

        System.out.println("--------");
    }

}
