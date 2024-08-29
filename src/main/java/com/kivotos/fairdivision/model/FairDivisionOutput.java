package com.kivotos.fairdivision.model;

import com.kivotos.fairdivision.util.ValuationChecker;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FairDivisionOutput {

    private List<Allocation> allocations;
    private int [][] valuationMatrix;
    private boolean isEF;
    private boolean isEFX;
    private boolean isEF1;
    private String errorMessage;


    public FairDivisionOutput(List<Allocation> allocations, int [][] valuationMatrix) {
        this.allocations = allocations;
        this.valuationMatrix = valuationMatrix;

        this.isEF = isEF(allocations);
        if (this.isEF) {
            this.isEFX = true;
            this.isEF1 = true;
        } else {
            this.isEFX = isEFX(allocations);
            this.isEF1 = this.isEFX || isEF1(allocations);
        }
    }

    public FairDivisionOutput(String errorMessage) {
        this.allocations = new ArrayList<>();
        this.isEF = false;
        this.isEFX = false;
        this.isEF1 = false;
        this.errorMessage = errorMessage;
    }

    private boolean isEF(List<Allocation> allocations) {
        for (Allocation currentAllocation : allocations) {
            for (Allocation otherAllocation : allocations) {
                if (currentAllocation.getAgentId() != otherAllocation.getAgentId()) {
                    int currentValue = ValuationChecker.getValuation(currentAllocation.getAgentId(), currentAllocation.getGoodsList(),valuationMatrix); // v_i(A_i)
                    int otherValue = ValuationChecker.getValuation(currentAllocation.getAgentId(), otherAllocation.getGoodsList(),valuationMatrix);     // v_i(A_j)

                    if (otherValue > currentValue) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isEF1(List<Allocation> allocations) {
        for (Allocation currentAllocation : allocations) {
            for (Allocation otherAllocation : allocations) {
                if (currentAllocation.getAgentId() != otherAllocation.getAgentId()) {
                    int currentValue = ValuationChecker.getValuation(currentAllocation.getAgentId(), currentAllocation.getGoodsList(),valuationMatrix);
                    int otherValue = ValuationChecker.getValuation(currentAllocation.getAgentId(), getAllocationWithoutHighestValuedGood(otherAllocation).getGoodsList(),valuationMatrix);
                    if (otherValue > currentValue) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isEFX(List<Allocation> allocations) {
        for (Allocation currentAllocation : allocations) {
            for (Allocation otherAllocation : allocations) {
                if (currentAllocation.getAgentId() != otherAllocation.getAgentId()) {
                    int currentValue = ValuationChecker.getValuation(currentAllocation.getAgentId(), currentAllocation.getGoodsList(),valuationMatrix);
                    int otherValue = ValuationChecker.getValuation(currentAllocation.getAgentId(), getAllocationWithoutLowestValuedGood(otherAllocation).getGoodsList(),valuationMatrix);

                    if (otherValue > currentValue) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public static Allocation getAllocationWithoutHighestValuedGood(Allocation allocation) {
        if(allocation.getHighestValuedGood()==Integer.MIN_VALUE) return allocation;

        Allocation temp = new Allocation(allocation);
        temp.getGoodsList().remove(allocation.getHighestValuedGoodIndex());
        return temp;
    }

    public static Allocation getAllocationWithoutLowestValuedGood(Allocation allocation) {
        if(allocation.getLowestValuedGood()==Integer.MAX_VALUE) return allocation;

        Allocation temp = new Allocation(allocation);
        temp.getGoodsList().remove(allocation.getLowestValuedGoodIndex());
        return temp;
    }


}
