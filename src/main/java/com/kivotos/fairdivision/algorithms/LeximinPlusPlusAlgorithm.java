package com.kivotos.fairdivision.algorithms;

import com.kivotos.fairdivision.model.Allocation;
import com.kivotos.fairdivision.model.FairDivisionInput;
import com.kivotos.fairdivision.model.FairDivisionOutput;
import com.kivotos.fairdivision.util.AgentUtility;
import com.kivotos.fairdivision.util.ValuationChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
    If true is returned then allocation A is preferred
    If false is returned then allocation B is preferred
 */
public class LeximinPlusPlusAlgorithm implements FairDivisionAlgorithm {

    @Override
    public FairDivisionOutput allocate(FairDivisionInput fairDivisionInput) {

        if(fairDivisionInput.getLeximinFirstAllocation()==null || fairDivisionInput.getLeximinSecondAllocation()==null)
            return new FairDivisionOutput("There was a problem with the leximin allocations provided.");


        if (fairDivisionInput.getAgentNumber() == 2)
            return LeximinPlusPlusTwoPlayersCase(fairDivisionInput);
        return LeximinPlusPlus(fairDivisionInput); // Identical case
    }

    private FairDivisionOutput LeximinPlusPlus(FairDivisionInput fairDivisionInput) {
        List<Integer> firstOrdering = calculateIncreasingOrdering(fairDivisionInput.getLeximinFirstAllocation().getAllocations(), fairDivisionInput.getValuationMatrix());
        List<Integer> secondOrdering = calculateIncreasingOrdering(fairDivisionInput.getLeximinSecondAllocation().getAllocations(), fairDivisionInput.getValuationMatrix());

        for (int l = 0; l < fairDivisionInput.getAgentNumber(); l++) {
            int valueOfFirstAgent = ValuationChecker.getValuation(firstOrdering.get(l), fairDivisionInput.getLeximinFirstAllocation().getAllocations().get(l).getGoodsList());
            int valueOfSecondAgent = ValuationChecker.getValuation(secondOrdering.get(l), fairDivisionInput.getLeximinSecondAllocation().getAllocations().get(l).getGoodsList());

            if (valueOfFirstAgent != valueOfSecondAgent)
                return valueOfFirstAgent < valueOfSecondAgent ? fairDivisionInput.getLeximinFirstAllocation() : fairDivisionInput.getLeximinSecondAllocation();

            int sizeOfFirstAgentAllocation = fairDivisionInput.getLeximinFirstAllocation().getAllocations().get(l).getGoodsList().size();
            int sizeOfSecondAgentAllocation = fairDivisionInput.getLeximinSecondAllocation().getAllocations().get(l).getGoodsList().size();

            if (sizeOfFirstAgentAllocation != sizeOfSecondAgentAllocation)
                return sizeOfFirstAgentAllocation < sizeOfSecondAgentAllocation ? fairDivisionInput.getLeximinFirstAllocation() : fairDivisionInput.getLeximinSecondAllocation();
        }

        return fairDivisionInput.getLeximinSecondAllocation();
    }

    private List<Integer> calculateIncreasingOrdering(List<Allocation> allocations, int[][] valuationMatrix) {
        List<Integer> ordering = new ArrayList<>();
        List<AgentUtility> agentUtilities = new ArrayList<>();

        // Calculate utility for each agent
        for (int i = 0; i < allocations.size(); i++) {
            int utility = ValuationChecker.getValuation(i, allocations.get(i).getGoodsList());
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

    private FairDivisionOutput LeximinPlusPlusTwoPlayersCase(FairDivisionInput fairDivisionInput) {

        FairDivisionOutput leximinSolutionForPlayer1 = LeximinPlusPlus(fairDivisionInput);

        Allocation bundle1 = leximinSolutionForPlayer1.getAllocations().get(0);
        Allocation bundle2 = leximinSolutionForPlayer1.getAllocations().get(1);

        int utility1ForPlayer2 = ValuationChecker.getValuation(1, bundle1.getGoodsList());
        int utility2ForPlayer2 = ValuationChecker.getValuation(1, bundle2.getGoodsList());

        // Player 2 chooses the preferred bundle

        List<Allocation> finalAllocations = new ArrayList<>();
        if (utility1ForPlayer2 >= utility2ForPlayer2) {
            finalAllocations.add(bundle2);
            finalAllocations.add(bundle1);
        } else {
            finalAllocations.add(bundle1);
            finalAllocations.add(bundle2);
        }

        return new FairDivisionOutput (finalAllocations);
    }
}
