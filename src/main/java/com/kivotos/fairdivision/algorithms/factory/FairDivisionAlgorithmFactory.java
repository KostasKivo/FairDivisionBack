package com.kivotos.fairdivision.algorithms.factory;

import com.kivotos.fairdivision.algorithms.*;

public class FairDivisionAlgorithmFactory {

    public static FairDivisionAlgorithm getAlgorithm(int algorithmId) {
        switch (algorithmId) {
            case 1:
                return new RoundRobinAlgorithm();
            case 2:
                return new EnvyCycleEliminationAlgorithm();
            case 3:
                return new MatchAndFreezeAlgorithm();
            case 4:
                return new LeximinPlusPlusAlgorithm();
            case 5:
                return new MaximumNashWelfareAlgorithm();
            case 6:
                return new IdenticalValuationAlgorithm();
            default:
                throw new IllegalArgumentException("Invalid algorithm ID: " + algorithmId);
        }
    }
}

