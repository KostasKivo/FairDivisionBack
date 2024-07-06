package com.kivotos.fairdivision.algorithms.factory;

import com.kivotos.fairdivision.algorithms.FairDivisionAlgorithm;
import com.kivotos.fairdivision.algorithms.RoundRobinAlgorithm;

public class FairDivisionAlgorithmFactory {

    public static FairDivisionAlgorithm getAlgorithm(int algorithmId) {
        switch (algorithmId) {
            case 1:
                return new RoundRobinAlgorithm();
            // Add more cases here for different algorithms
            default:
                throw new IllegalArgumentException("Invalid algorithm ID: " + algorithmId);
        }
    }
}

