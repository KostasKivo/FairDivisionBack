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

public class IdenticalValuationAlgorithmTest {

    private final IdenticalValuationAlgorithm algorithm = new IdenticalValuationAlgorithm();

    @Mock
    private FairDivisionInput fairDivisionInput;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBasicAllocationWithIdenticalValuations() {
        int[][] valuationMatrix = {
                {10, 10, 10},
                {10, 10, 10}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(2);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(3);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        // Check that both agents have been allocated goods
        assertTrue(allocations.get(0).getGoodsList().size() > 0);
        assertTrue(allocations.get(1).getGoodsList().size() > 0);

        // Ensure that all goods are allocated
        int totalGoodsAllocated = allocations.get(0).getGoodsList().size() + allocations.get(1).getGoodsList().size();
        assertTrue(totalGoodsAllocated == 3);
    }

    @Test
    public void testOneAgentMultipleGoodsIdenticalValuation() {
        int[][] valuationMatrix = {
                {10, 10, 10}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(1);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(3);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();
        Allocation allocation = allocations.get(0);

        // Ensure the single agent gets all goods
        assertTrue(allocation.getGoodsList().size() == 3);
    }

    @Test
    public void testMultipleAgentsOneGoodIdenticalValuation() {
        int[][] valuationMatrix = {
                {10},
                {10}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(2);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(1);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        // Only one agent should receive the good, the other should receive nothing
        assertTrue(allocations.get(0).getGoodsList().contains(0) || allocations.get(1).getGoodsList().contains(0));
        assertTrue(allocations.get(0).getGoodsList().isEmpty() || allocations.get(1).getGoodsList().isEmpty());
    }

    @Test
    public void testNoGoodsToAllocateWithIdenticalValuation() {
        int[][] valuationMatrix = {{}};

        when(fairDivisionInput.getAgentNumber()).thenReturn(2);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(0);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        // Both agents should have empty allocations
        assertTrue(allocations.get(0).getGoodsList().isEmpty());
        assertTrue(allocations.get(1).getGoodsList().isEmpty());
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
    public void testNoAgentsWithIdenticalValuation() {
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
