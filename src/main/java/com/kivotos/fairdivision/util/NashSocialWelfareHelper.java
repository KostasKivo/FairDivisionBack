package com.kivotos.fairdivision.util;

import com.kivotos.fairdivision.model.Allocation;

import java.util.*;

public class NashSocialWelfareHelper {

    public static double calculateNashSocialWelfare(List<Allocation> allocations, int [][] valuationMatrix) {

        double ret = 1;

        for(Allocation allocation: allocations) {
            ret *= ValuationChecker.getValuation(allocation.getAgentId(),allocation.getGoodsList(),valuationMatrix);
        }

        return ret;
    }
}
