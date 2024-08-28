package com.kivotos.fairdivision.algorithms;

import com.kivotos.fairdivision.model.FairDivisionInput;
import com.kivotos.fairdivision.model.FairDivisionOutput;

/*
    If true is returned then allocation A is preferred
    If false is returned then allocation B is preferred
 */
public class LeximinPlusPlusAlgorithm implements FairDivisionAlgorithm {

    @Override
    public FairDivisionOutput allocate(FairDivisionInput fairDivisionInput) {


        return new FairDivisionOutput("There was a problem with the leximin allocations provided.");

    }


}
