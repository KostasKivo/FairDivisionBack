package com.kivotos.fairdivision.util;

import lombok.Data;

import java.util.List;

@Data
public class ValuationChecker {

    public static int getValuation(int agentId, List<Integer> goods, int [][] valuationMatrix) {
        int totalValue = 0;

        for (int good :goods) {
            totalValue += valuationMatrix[agentId][good];
        }

        return totalValue;
    }

    public static int getValuationMatrixAtIndex(int i,int j, int [][] valuationMatrix) {
        return valuationMatrix[i][j];
    }
}
