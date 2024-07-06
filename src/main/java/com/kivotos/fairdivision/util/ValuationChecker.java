package com.kivotos.fairdivision.util;

import lombok.Data;

import java.util.List;

@Data
public class ValuationChecker {


    private static int[][] valuationMatrix;

    public ValuationChecker(int[][] valuationMatrix) {
        ValuationChecker.valuationMatrix = valuationMatrix;
    }

    public static int getValuation(int agentId, List<Integer> goods) {
        int totalValue = 0;

        for (int good :goods) {
            totalValue += valuationMatrix[agentId][good];
        }

        return totalValue;
    }

    public static void setValuationMatrix(int[][] valuationMatrix) {
        ValuationChecker.valuationMatrix = valuationMatrix;
    }
}
