package com.kivotos.fairdivision.algorithms;

import com.kivotos.fairdivision.model.FairDivisionInput;
import com.kivotos.fairdivision.model.FairDivisionOutput;

public interface FairDivisionAlgorithm {

    FairDivisionOutput allocate(FairDivisionInput fairDivisionInput);
}
