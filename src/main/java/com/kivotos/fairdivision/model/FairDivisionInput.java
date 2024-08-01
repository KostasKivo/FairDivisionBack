package com.kivotos.fairdivision.model;

import lombok.Data;

@Data
public class FairDivisionInput {
    private int agentNumber;
    private int goodsNumber;
    private int valuationType;
    private int algorithmId;
    private int[][] valuationMatrix;
    private FairDivisionOutput leximinFirstAllocation;
    private FairDivisionOutput leximinSecondAllocation;

}
