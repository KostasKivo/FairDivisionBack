package com.kivotos.fairdivision.model;

import lombok.Data;

import java.util.List;

@Data
public class FairDivisionOutput {

    private List<Allocation> allocations;
    private boolean isEF;
    private boolean isEF1;
    private boolean isEFX;

    public FairDivisionOutput(List<Allocation> allocations) {
        this.allocations = allocations;
        this.isEF = false;
        this.isEF1 = false;
        this.isEFX = false;
    }
}
