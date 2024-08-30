package com.kivotos.fairdivision.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FairDivisionOutputTest {

    private int[][] valuationMatrix;
    private List<Allocation> allocations;

    @BeforeEach
    void setUp() {
        valuationMatrix = new int[][]{
                {2, 10,9,4,4,8},
                {2,2,1,3,5,4},
                {3,3,4,4,10,5}
        };

        // Create allocations based on this valuation matrix
        allocations = new ArrayList<>();
        Allocation allocation1 = new Allocation(0); // Agent 0
        allocation1.add(1, valuationMatrix); // Good 0 with value 4
        allocation1.add(2, valuationMatrix); // Good 1 with value 2

        Allocation allocation2 = new Allocation(1); // Agent 1
        allocation2.add(3, valuationMatrix); // Good 2 with value 3
        allocation2.add(4, valuationMatrix); // Good 3 with value 6

        Allocation allocation3 = new Allocation(2); // Agent 2
        allocation3.add(5, valuationMatrix); // Good 2 with value 4
        allocation3.add(0, valuationMatrix); // Good 3 with value 5

        allocations.add(allocation1);
        allocations.add(allocation2);
        allocations.add(allocation3);
    }


    @Test
    void testConstructorWithErrorMessage() {
        FairDivisionOutput fairDivisionOutput = new FairDivisionOutput("Error occurred");

        assertTrue(fairDivisionOutput.getAllocations().isEmpty());
        assertFalse(fairDivisionOutput.isEF());
        assertFalse(fairDivisionOutput.isEFX());
        assertFalse(fairDivisionOutput.isEF1());
        assertFalse(fairDivisionOutput.isProp());
        assertTrue(fairDivisionOutput.getNashWelfareValue() > 0);
        assertEquals("Error occurred", fairDivisionOutput.getErrorMessage());
    }

    @Test
    void testGetAllocationWithoutHighestValuedGood() {
        Allocation originalAllocation = new Allocation(0);
        originalAllocation.add(1, valuationMatrix); // Good 2, Value 10
        originalAllocation.add(3, valuationMatrix); // Good 3, Value 9

        Allocation resultAllocation = FairDivisionOutput.getAllocationWithoutHighestValuedGood(originalAllocation);

        assertFalse(resultAllocation.getGoodsList().contains(1)); // The good with the highest value should be removed
        assertTrue(resultAllocation.getGoodsList().contains(3));
    }

    @Test
    void testGetAllocationWithoutLowestValuedGood() {
        Allocation originalAllocation = new Allocation(0);
        originalAllocation.add(1, valuationMatrix); // Good 2, Value 10
        originalAllocation.add(3, valuationMatrix); // Good 3, Value 9

        Allocation resultAllocation = FairDivisionOutput.getAllocationWithoutLowestValuedGood(originalAllocation);

        assertFalse(resultAllocation.getGoodsList().contains(3)); // The good with the lowest value should be removed
        assertTrue(resultAllocation.getGoodsList().contains(1));
    }

    @Test
    void testEF() {
        FairDivisionOutput fairDivisionOutput = new FairDivisionOutput(allocations, valuationMatrix);
        assertFalse(fairDivisionOutput.isEF());
    }

    @Test
    void testEFX() {
        FairDivisionOutput fairDivisionOutput = new FairDivisionOutput(allocations, valuationMatrix);
        assertFalse(fairDivisionOutput.isEFX());
    }

    @Test
    void testEF1() {
        FairDivisionOutput fairDivisionOutput = new FairDivisionOutput(allocations, valuationMatrix);
        assertTrue(fairDivisionOutput.isEF1());
    }

    @Test
    void testProp() {
        FairDivisionOutput fairDivisionOutput = new FairDivisionOutput(allocations, valuationMatrix);
        assertFalse(fairDivisionOutput.isProp());
    }
}
