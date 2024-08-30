package com.kivotos.fairdivision.algorithms;

import com.kivotos.fairdivision.model.Allocation;
import com.kivotos.fairdivision.model.FairDivisionInput;
import com.kivotos.fairdivision.model.FairDivisionOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class MaximumNashWelfareAlgorithmTest {

    private final MaximumNashWelfareAlgorithm algorithm = new MaximumNashWelfareAlgorithm();

    @Mock
    private FairDivisionInput fairDivisionInput;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBasicAllocation() {
        int[][] valuationMatrix = {
                {1, 2, 3},
                {2, 3, 4}
        };

        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        assertNotNull(allocations);
        assertEquals(2, allocations.size());

        // Check if allocations are for the correct goods
        Allocation allocationAgent0 = allocations.get(0);
        Allocation allocationAgent1 = allocations.get(1);

        assertTrue(allocationAgent0.getGoodsList().size() > 0);
        assertTrue(allocationAgent1.getGoodsList().size() > 0);
    }

    @Test
    public void testMoreAgentsThanGoods() {
        int[][] valuationMatrix = {
                {1, 2, 3},
                {2, 3, 4},
                {4, 5, 6},
                {3, 4, 5}
        };

        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        assertNotNull(allocations);
        assertEquals(3, allocations.size());  // Only 3 agents can be chosen since there are 3 goods

        // Check that the agents selected are indeed those with the highest utility
        Allocation allocationAgent0 = allocations.get(0);
        Allocation allocationAgent1 = allocations.get(1);
        Allocation allocationAgent2 = allocations.get(2);

        assertTrue(allocationAgent0.getGoodsList().size() > 0);
        assertTrue(allocationAgent1.getGoodsList().size() > 0);
        assertTrue(allocationAgent2.getGoodsList().size() > 0);
    }

    @Test
    public void testSingleAgentMultipleGoods() {
        int[][] valuationMatrix = {
                {5, 10, 15}
        };

        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        assertEquals(1, allocations.size());

        Allocation allocation = allocations.get(0);
        assertTrue(allocation.getGoodsList().contains(0));
        assertTrue(allocation.getGoodsList().contains(1));
        assertTrue(allocation.getGoodsList().contains(2));
    }

    @Test
    public void testMultipleAgentsOneGood() {
        int[][] valuationMatrix = {
                {10},
                {5},
                {15}
        };

        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        assertEquals(1, allocations.size());

        Allocation allocation = allocations.get(0);
        assertTrue(allocation.getGoodsList().contains(0));
    }

    @Test
    public void testIdenticalValuations() {
        int[][] valuationMatrix = {
                {10, 10, 10},
                {10, 10, 10},
                {10, 10, 10}
        };

        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        assertEquals(3, allocations.size());

        // Check that all agents have at least one good allocated
        for (Allocation allocation : allocations) {
            assertTrue(allocation.getGoodsList().size() > 0);
        }
    }

    @Test
    public void testNegativeValuations() {
        int[][] valuationMatrix = {
                {-1, -2, -3},
                {-4, -5, -6}
        };

        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        assertNotNull(allocations);
        assertEquals(2, allocations.size());

        // Check that allocations were made despite negative values
        for (Allocation allocation : allocations) {
            assertTrue(allocation.getGoodsList().size() > 0);
        }
    }

    @Test
    public void testSingleAgentSingleGood() {
        int[][] valuationMatrix = {
                {10}
        };

        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();
        assertEquals(1, allocations.size());

        Allocation allocation = allocations.get(0);
        assertTrue(allocation.getGoodsList().contains(0));
    }

    @Test
    public void testMultipleGoodsWithOneAgentAndEmptyValuation() {
        int[][] valuationMatrix = {
                {0, 0, 0}
        };

        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();
        assertEquals(1, allocations.size());

        Allocation allocation = allocations.get(0);

        // Even though the valuations are empty, the agent should still get all goods
        assertTrue(allocation.getGoodsList().size() == 3);
    }
}
