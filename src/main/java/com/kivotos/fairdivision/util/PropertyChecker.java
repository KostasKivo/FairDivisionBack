package com.kivotos.fairdivision.util;

import com.kivotos.fairdivision.model.Allocation;

import java.util.List;

public class PropertyChecker {


    public static boolean isEF(List<Allocation> allocations, ValuationChecker valuationChecker) {
        for (Allocation currentAllocation : allocations) {
            for (Allocation otherAllocation : allocations) {
                if (currentAllocation.getAgentId() != otherAllocation.getAgentId()) {
                    int currentValue = valuationChecker.getValuation(currentAllocation.getAgentId(), currentAllocation.getGoodsList());
                    int otherValue = valuationChecker.getValuation(currentAllocation.getAgentId(), otherAllocation.getGoodsList());

                    if (otherValue > currentValue) {
                        System.out.println(otherValue + " " + currentValue);
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public static boolean isEF1(List<Allocation> allocations, ValuationChecker valuationChecker) {
        return false;
    }

    public static boolean isEFX(List<Allocation> allocations, ValuationChecker valuationChecker) {
        return false;
    }
}
