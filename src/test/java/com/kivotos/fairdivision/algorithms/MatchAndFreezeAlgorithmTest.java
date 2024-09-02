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

public class MatchAndFreezeAlgorithmTest {

    private final MatchAndFreezeAlgorithm algorithm = new MatchAndFreezeAlgorithm();

    @Mock
    private FairDivisionInput fairDivisionInput;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBasicAllocation() {
        int[][] valuationMatrix = {
                {2, 4, 2},
                {4, 2, 4}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(2);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(3);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        assertNull(output.getErrorMessage());

        List<Allocation> allocations = output.getAllocations();

        // Check allocation for agent 0
        Allocation allocationAgent0 = allocations.get(0);
        assertTrue(allocationAgent0.getGoodsList().contains(1)); // Agent 0 should get good 1
        assertTrue(allocationAgent0.getGoodsList().size() == 1); // Should only have 1 good

        // Check allocation for agent 1
        Allocation allocationAgent1 = allocations.get(1);
        assertTrue(allocationAgent1.getGoodsList().contains(0)); // Agent 1 should get good 0
        assertTrue(allocationAgent1.getGoodsList().size() == 2); // Should get 2 goods
    }

    @Test
    public void testTwoValuedInstanceNotDetected() {
        int[][] valuationMatrix = {
                {1, 2, 3},
                {3, 2, 1}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(2);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(3);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        assertEquals("Not a binary instance.", output.getErrorMessage());
    }

    @Test
    public void testNoGoodsToAllocate() {
        int[][] valuationMatrix = {{}};

        when(fairDivisionInput.getAgentNumber()).thenReturn(2);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(0);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        assertNotNull(output.getErrorMessage());

        assertTrue(output.getAllocations().isEmpty());
    }

    @Test
    public void testOneAgentMultipleGoods() {
        int[][] valuationMatrix = {
                {2, 2, 2}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(1);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(3);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        assertNotNull(output.getErrorMessage());

        assertTrue(output.getAllocations().isEmpty());
    }

    @Test
    public void testMultipleAgentsOneGood() {
        int[][] valuationMatrix = {
                {2},
                {4}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(2);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(1);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        assertNull(output.getErrorMessage());

        List<Allocation> allocations = output.getAllocations();

        // Agent 1 should get the good since it values it more
        assertTrue(allocations.get(1).getGoodsList().contains(0));
        assertTrue(allocations.get(0).getGoodsList().isEmpty());
    }

    @Test
    public void testIdenticalValuations() {
        int[][] valuationMatrix = {
                {2, 2, 2},
                {2, 2, 2}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(2);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(3);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        assertNotNull(output.getErrorMessage());

        assertTrue(output.getAllocations().isEmpty());
    }

    @Test
    public void testEmptyValuationMatrix() {
        int[][] valuationMatrix = new int[2][0]; // 2 agents, 0 goods

        when(fairDivisionInput.getAgentNumber()).thenReturn(2);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(0);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        assertNotNull(output.getErrorMessage());

        assertTrue(output.getAllocations().isEmpty());
    }

    @Test
    public void testNegativeValuationValues() {
        int[][] valuationMatrix = {
                {-2, -4, -2},
                {-4, -2, -4}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(2);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(3);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        assertNotNull(output.getErrorMessage());

        assertTrue(output.getAllocations().isEmpty());
    }

    @Test
    public void testThreeValuedInstance() {
        int[][] valuationMatrix = {
                {1, 2, 3},
                {3, 2, 1}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(2);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(3);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        // Three distinct values should result in an error message
        assertEquals("Not a binary instance.", output.getErrorMessage());
    }
}
