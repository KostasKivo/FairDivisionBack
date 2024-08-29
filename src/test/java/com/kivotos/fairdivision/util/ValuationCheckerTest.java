package com.kivotos.fairdivision.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValuationCheckerTest {

    @Test
    void testGetValuation_SingleGood() {
        int[][] valuationMatrix = {
                {5, 10, 15},
                {10, 20, 30}
        };

        int agentId = 0;
        int totalValue = ValuationChecker.getValuation(agentId, Arrays.asList(0), valuationMatrix);

        assertEquals(5, totalValue);
    }

    @Test
    void testGetValuation_MultipleGoods() {
        int[][] valuationMatrix = {
                {5, 10, 15},
                {10, 20, 30}
        };

        int agentId = 0;
        int totalValue = ValuationChecker.getValuation(agentId, Arrays.asList(0, 2), valuationMatrix);

        assertEquals(20, totalValue); // 5 (for good 0) + 15 (for good 2)
    }

    @Test
    void testGetValuation_AllGoods() {
        int[][] valuationMatrix = {
                {5, 10, 15},
                {10, 20, 30}
        };

        int agentId = 1;
        int totalValue = ValuationChecker.getValuation(agentId, Arrays.asList(0, 1, 2), valuationMatrix);

        assertEquals(60, totalValue); // 10 + 20 + 30
    }

    @Test
    void testGetValuation_EmptyGoodsList() {
        int[][] valuationMatrix = {
                {5, 10, 15},
                {10, 20, 30}
        };

        int agentId = 0;
        int totalValue = ValuationChecker.getValuation(agentId, Arrays.asList(), valuationMatrix);

        assertEquals(0, totalValue);
    }

    @Test
    void testGetValuationMatrixAtIndex_ValidIndices() {
        int[][] valuationMatrix = {
                {5, 10, 15},
                {10, 20, 30}
        };

        int value = ValuationChecker.getValuationMatrixAtIndex(1, 2, valuationMatrix);

        assertEquals(30, value);
    }

    @Test
    void testGetValuationMatrixAtIndex_FirstElement() {
        int[][] valuationMatrix = {
                {5, 10, 15},
                {10, 20, 30}
        };

        int value = ValuationChecker.getValuationMatrixAtIndex(0, 0, valuationMatrix);

        assertEquals(5, value);
    }

    @Test
    void testGetValuationMatrixAtIndex_LastElement() {
        int[][] valuationMatrix = {
                {5, 10, 15},
                {10, 20, 30}
        };

        int value = ValuationChecker.getValuationMatrixAtIndex(1, 2, valuationMatrix);

        assertEquals(30, value);
    }
}
