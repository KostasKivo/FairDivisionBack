package com.kivotos.fairdivision.algorithms;

import com.kivotos.fairdivision.model.FairDivisionOutput;

public interface FairDivisionAlgorithm {

    FairDivisionOutput allocate(int agents, int goods, int[][] valuationMatrix);
}
