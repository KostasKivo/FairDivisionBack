package com.kivotos.fairdivision.model;

import com.kivotos.fairdivision.util.ValuationChecker;
import lombok.Data;

import java.util.List;

@Data
public class FairDivisionOutput {

    private List<Allocation> allocations;
    private boolean isEF;
    private boolean isEFX;
    private boolean isEF1;


    public FairDivisionOutput(List<Allocation> allocations) {
        this.allocations = allocations;

        this.isEF = isEF(allocations);
        if (this.isEF) {
            this.isEFX = true;
            this.isEF1 = true;
        } else {
            this.isEFX = isEFX(allocations);
            this.isEF1 = this.isEFX || isEF1(allocations);
        }
    }

    private boolean isEF(List<Allocation> allocations) {
        return compareAllocations(allocations, (currentAllocation, otherAllocation) ->
                ValuationChecker.getValuation(currentAllocation.getAgentId(), otherAllocation.getGoodsList())
        );
    }

    public static boolean isEF1(List<Allocation> allocations) {
        return compareAllocations(allocations, (currentAllocation, otherAllocation) ->
                Math.abs(ValuationChecker.getValuation(currentAllocation.getAgentId(), otherAllocation.getGoodsList()) - currentAllocation.getHighestValuedGood())
        );
    }

    public static boolean isEFX(List<Allocation> allocations) {
        return compareAllocations(allocations, (currentAllocation, otherAllocation) ->
                Math.abs(ValuationChecker.getValuation(currentAllocation.getAgentId(), otherAllocation.getGoodsList()) - currentAllocation.getLowestValuedGood())
        );
    }

    private static boolean compareAllocations(List<Allocation> allocations, java.util.function.BiFunction<Allocation, Allocation, Integer> comparisonFunction) {
        for (Allocation currentAllocation : allocations) {
            for (Allocation otherAllocation : allocations) {
                if (currentAllocation.getAgentId() != otherAllocation.getAgentId()) {
                    int currentValue = ValuationChecker.getValuation(currentAllocation.getAgentId(), currentAllocation.getGoodsList());
                    int otherValue = comparisonFunction.apply(currentAllocation, otherAllocation);

                    if (otherValue > currentValue) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
