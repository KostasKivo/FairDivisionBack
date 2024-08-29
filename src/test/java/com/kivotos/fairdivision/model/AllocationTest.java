package com.kivotos.fairdivision.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AllocationTest {

    private int[][] valuationMatrix;

    @BeforeEach
    void setUp() {
        valuationMatrix = new int[][]{
                {3, 1, 4, 5},
                {2, 3, 6, 1}
        };
    }

    @Test
    void testAllocationInitialization() {
        Allocation allocation = new Allocation(0);

        assertEquals(0, allocation.getAgentId());
        assertEquals(Integer.MIN_VALUE, allocation.getHighestValuedGood());
        assertEquals(Integer.MAX_VALUE, allocation.getLowestValuedGood());
        assertTrue(allocation.getGoodsList().isEmpty());
    }

    @Test
    void testAddSingleGood() {
        Allocation allocation = new Allocation(0);
        allocation.add(2, valuationMatrix); // Agent 0, Good 2 has value 4

        assertEquals(4, allocation.getHighestValuedGood());
        assertEquals(4, allocation.getLowestValuedGood());
        assertEquals(0, allocation.getHighestValuedGoodIndex());
        assertEquals(0, allocation.getLowestValuedGoodIndex());
        assertEquals(List.of(2), allocation.getGoodsList());
    }

    @Test
    void testAddMultipleGoods() {
        Allocation allocation = new Allocation(0);
        allocation.add(1, valuationMatrix); // Good 1 has value 1
        allocation.add(3, valuationMatrix); // Good 3 has value 5
        allocation.add(0, valuationMatrix); // Good 0 has value 3

        assertEquals(5, allocation.getHighestValuedGood());
        assertEquals(1, allocation.getLowestValuedGood());
        assertEquals(0, allocation.getLowestValuedGoodIndex());
        assertEquals(1, allocation.getGoodsList().get(allocation.getLowestValuedGoodIndex())); // Ensure correct good
        assertEquals(valuationMatrix[0][allocation.getGoodsList().get(allocation.getLowestValuedGoodIndex())],
                allocation.getLowestValuedGood());

        assertEquals(1, allocation.getHighestValuedGoodIndex());
        assertEquals(valuationMatrix[0][allocation.getGoodsList().get(allocation.getHighestValuedGoodIndex())],
                allocation.getHighestValuedGood());
    }

    @Test
    void testCopyConstructor() {
        Allocation original = new Allocation(0);
        original.add(1, valuationMatrix);
        original.add(2, valuationMatrix);

        Allocation copy = new Allocation(original);

        assertEquals(original.getAgentId(), copy.getAgentId());
        assertEquals(original.getHighestValuedGood(), copy.getHighestValuedGood());
        assertEquals(original.getLowestValuedGood(), copy.getLowestValuedGood());
        assertEquals(original.getGoodsList(), copy.getGoodsList());

        // Ensure modifying the copy doesn't affect the original
        copy.add(3, valuationMatrix);
        assertNotEquals(original.getGoodsList().size(), copy.getGoodsList().size());
    }

    @Test
    void testRecalculateIndexesAfterModification() {
        Allocation allocation = new Allocation(0);
        allocation.add(1, valuationMatrix); // Good 1, Value 1
        allocation.add(2, valuationMatrix); // Good 2, Value 4
        allocation.add(3, valuationMatrix); // Good 3, Value 5

        // Now remove good 3 from the list to simulate a change in allocation
        allocation.getGoodsList().remove(2);
        allocation.recalculateIndexes(valuationMatrix);

        assertEquals(4, allocation.getHighestValuedGood());
        assertEquals(1, allocation.getHighestValuedGoodIndex());
        assertEquals(1, allocation.getLowestValuedGood());
        assertEquals(0, allocation.getLowestValuedGoodIndex());
    }

    @Test
    void testRecalculateIndexesWithEmptyGoodsList() {
        Allocation allocation = new Allocation(0);

        // Attempt to recalculate indexes with an empty goods list
        allocation.recalculateIndexes(valuationMatrix);

        assertEquals(Integer.MIN_VALUE, allocation.getHighestValuedGood());
        assertEquals(Integer.MAX_VALUE, allocation.getLowestValuedGood());
        assertEquals(-1, allocation.getHighestValuedGoodIndex());
        assertEquals(-1, allocation.getLowestValuedGoodIndex());
    }

    @Test
    void testNegativeValuations() {
        int[][] negativeValuationMatrix = {
                {-3, -1, -4, -5},
                {-2, -3, -6, -1}
        };

        Allocation allocation = new Allocation(0);
        allocation.add(0, negativeValuationMatrix);
        allocation.add(2, negativeValuationMatrix);

        // Check if the algorithm handles negative values properly
        assertEquals(-3, allocation.getHighestValuedGood());
        assertEquals(-4, allocation.getLowestValuedGood());
    }
}
