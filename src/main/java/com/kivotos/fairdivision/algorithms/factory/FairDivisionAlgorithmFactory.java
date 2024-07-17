package com.kivotos.fairdivision.algorithms.factory;

import com.kivotos.fairdivision.algorithms.EnvyCycleEliminationAlgorithm;
import com.kivotos.fairdivision.algorithms.FairDivisionAlgorithm;
import com.kivotos.fairdivision.algorithms.MatchAndFreezeAlgorithm;
import com.kivotos.fairdivision.algorithms.RoundRobinAlgorithm;

public class FairDivisionAlgorithmFactory {

    public static FairDivisionAlgorithm getAlgorithm(int algorithmId) {
        switch (algorithmId) {
            case 1:
                return new RoundRobinAlgorithm();
            case 2:
                return new EnvyCycleEliminationAlgorithm();
            case 3:
                return new MatchAndFreezeAlgorithm();
            default:
                throw new IllegalArgumentException("Invalid algorithm ID: " + algorithmId);
        }
    }
}

