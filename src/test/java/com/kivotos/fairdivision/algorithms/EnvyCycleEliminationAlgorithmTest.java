package com.kivotos.fairdivision.algorithms;

import com.kivotos.fairdivision.model.Allocation;
import com.kivotos.fairdivision.model.FairDivisionInput;
import com.kivotos.fairdivision.model.FairDivisionOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class EnvyCycleEliminationAlgorithmTest {

    private final EnvyCycleEliminationAlgorithm algorithm = new EnvyCycleEliminationAlgorithm();

    @Mock
    private FairDivisionInput fairDivisionInput;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBasicAllocation() {
        int[][] valuationMatrix = {
                {10, 20, 30},
                {5, 15, 25}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(2);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(3);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        // Check allocation for agent 0
        Allocation allocationAgent0 = allocations.get(0);
        assertTrue(allocationAgent0.getGoodsList().contains(2)); // Should get the highest valued good (index 2)

        // Check allocation for agent 1
        Allocation allocationAgent1 = allocations.get(1);
        assertTrue(allocationAgent1.getGoodsList().contains(1));
        assertTrue(allocationAgent1.getGoodsList().contains(0)); // Should get the remaining goods
    }

    @Test
    public void testNoGoodsToAllocate() {
        int[][] valuationMatrix = {{}};

        when(fairDivisionInput.getAgentNumber()).thenReturn(2);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(0);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        assertTrue(allocations.get(0).getGoodsList().isEmpty());
        assertTrue(allocations.get(1).getGoodsList().isEmpty());
    }

    @Test
    public void testOneAgentMultipleGoods() {
        int[][] valuationMatrix = {
                {10, 20, 30}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(1);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(3);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();
        Allocation allocation = allocations.get(0);

        // Agent should get all goods
        assertTrue(allocation.getGoodsList().contains(0));
        assertTrue(allocation.getGoodsList().contains(1));
        assertTrue(allocation.getGoodsList().contains(2));
    }

    @Test
    public void testMultipleAgentsOneGood() {
        int[][] valuationMatrix = {
                {10},
                {20}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(2);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(1);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        // Both agents should have an empty allocation list
        assertTrue(allocations.get(0).getGoodsList().contains(0));
        assertTrue(allocations.get(1).getGoodsList().isEmpty());
    }

    @Test
    public void testIdenticalValuations() {
        int[][] valuationMatrix = {
                {10, 10, 10},
                {10, 10, 10}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(2);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(3);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        // Check allocation for agents
        assertTrue(allocations.get(0).getGoodsList().size() > 0);
        assertTrue(allocations.get(1).getGoodsList().size() > 0);
        // Since valuations are identical, we only need to verify that both agents get goods
    }

    @Test
    public void testEmptyValuationMatrix() {
        int[][] valuationMatrix = new int[2][0]; // 2 agents, 0 goods

        when(fairDivisionInput.getAgentNumber()).thenReturn(2);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(0);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        assertTrue(allocations.get(0).getGoodsList().isEmpty());
        assertTrue(allocations.get(1).getGoodsList().isEmpty());
    }

    @Test
    public void testNegativeValuationValues() {
        int[][] valuationMatrix = {
                {-10, -20, -30},
                {-5, -15, -25}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(2);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(3);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        // Check allocation for agent 0
        Allocation allocationAgent0 = allocations.get(0);
        assertTrue(allocationAgent0.getGoodsList().contains(2));
        assertTrue(allocationAgent0.getGoodsList().contains(1));
        assertTrue(allocationAgent0.getGoodsList().contains(0));

        // Check allocation for agent 1
        Allocation allocationAgent1 = allocations.get(1);
        assertTrue(allocationAgent1.getGoodsList().isEmpty());
    }

    @Test
    public void testCycleDetectionAndResolution() {
        int[][] valuationMatrix = {
                {2,1,4,6,6,9},
                {4,6,10,6,8,5},
                {2,3,8,4,1,7}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(3);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(6);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        // Check allocation for agent 0
        Allocation allocationAgent0 = allocations.get(0);
        assertTrue(allocationAgent0.getGoodsList().contains(5));
        assertTrue(allocationAgent0.getGoodsList().contains(4));


        // Check allocation for agent 1
        Allocation allocationAgent1 = allocations.get(1);
        assertTrue(allocationAgent1.getGoodsList().contains(0));
        assertTrue(allocationAgent1.getGoodsList().contains(1));
        assertTrue(allocationAgent1.getGoodsList().contains(3));

        // Check allocation for agent 1
        Allocation allocationAgent2 = allocations.get(2);
        assertTrue(allocationAgent2.getGoodsList().contains(2));
    }

    @Test
    public void testSingleAgentSingleGood() {
        int[][] valuationMatrix = {
                {10}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(1);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(1);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();
        Allocation allocation = allocations.get(0);

        // Single agent should get the single good
        assertTrue(allocation.getGoodsList().contains(0));
    }

    @Test
    public void testMultipleGoodsWithOneAgentAndEmptyValuation() {
        int[][] valuationMatrix = {
                {0, 0, 0}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(1);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(3);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();
        Allocation allocation = allocations.get(0);

        // Agent should get all goods
        assertTrue(allocation.getGoodsList().size() == 3);
    }

    @Test
    public void testNoAgents() {
        int[][] valuationMatrix = new int[0][0];

        when(fairDivisionInput.getAgentNumber()).thenReturn(0);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(0);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        // No allocations should be present
        assertTrue(allocations.isEmpty());
    }
}
