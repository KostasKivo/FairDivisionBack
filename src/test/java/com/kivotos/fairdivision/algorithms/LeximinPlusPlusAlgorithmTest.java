package com.kivotos.fairdivision.algorithms;

import com.kivotos.fairdivision.model.Allocation;
import com.kivotos.fairdivision.model.FairDivisionInput;
import com.kivotos.fairdivision.model.FairDivisionOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class LeximinPlusPlusAlgorithmTest {

    private LeximinPlusPlusAlgorithm algorithm;

    @Mock
    private FairDivisionInput fairDivisionInput;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        algorithm = new LeximinPlusPlusAlgorithm();
    }

    @Test
    public void testBasicAllocation() {
        int[][] valuationMatrix = {
                {10, 20, 30},
                {10, 20, 30}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(2);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(3);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        assertEquals(2, allocations.size());
        assertTrue(allocations.stream().mapToInt(a -> a.getGoodsList().size()).sum() == 3);

        // Ensure no good is allocated to more than one agent
        List<Integer> allGoodsAllocated = allocations.stream()
                .flatMap(a -> a.getGoodsList().stream())
                .collect(Collectors.toList());
        assertEquals(3, allGoodsAllocated.size());
        assertTrue(allGoodsAllocated.containsAll(List.of(0, 1, 2)));
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

        assertEquals(3, allocation.getGoodsList().size());
        assertTrue(allocation.getGoodsList().containsAll(List.of(0, 1, 2)));
    }

    @Test
    public void testMultipleAgentsOneGood() {
        int[][] valuationMatrix = {
                {10},
                {10}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(2);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(1);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        // Ensure that one agent gets the good and the other gets nothing
        List<Integer> allGoodsAllocated = allocations.stream()
                .flatMap(a -> a.getGoodsList().stream())
                .collect(Collectors.toList());
        assertEquals(1, allGoodsAllocated.size());
        assertTrue(allGoodsAllocated.contains(0));
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

        assertEquals(2, allocations.size());
        assertEquals(3, allocations.stream().mapToInt(a -> a.getGoodsList().size()).sum());

        // Ensure no good is allocated to more than one agent
        List<Integer> allGoodsAllocated = allocations.stream()
                .flatMap(a -> a.getGoodsList().stream())
                .collect(Collectors.toList());
        assertEquals(3, allGoodsAllocated.size());
        assertTrue(allGoodsAllocated.containsAll(List.of(0, 1, 2)));
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

        assertEquals(1, allocation.getGoodsList().size());
        assertTrue(allocation.getGoodsList().contains(0));
    }

    @Test
    public void testAllocationWithNegativeValuations() {
        int[][] valuationMatrix = {
                {-10, -10, -10},
                {-10, -10, -10}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(2);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(3);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        assertEquals(2, allocations.size());
        assertEquals(3, allocations.stream().mapToInt(a -> a.getGoodsList().size()).sum());

        // Ensure no good is allocated to more than one agent
        List<Integer> allGoodsAllocated = allocations.stream()
                .flatMap(a -> a.getGoodsList().stream())
                .collect(Collectors.toList());
        assertEquals(3, allGoodsAllocated.size());
        assertTrue(allGoodsAllocated.containsAll(List.of(0, 1, 2)));
    }

    @Test
    public void testAllocationWithEmptyValuationMatrix() {
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
    public void testSingleGoodAllocationWithMultipleAgents() {
        int[][] valuationMatrix = {
                {10},
                {10},
                {10}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(3);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(1);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        List<Integer> allGoodsAllocated = allocations.stream()
                .flatMap(a -> a.getGoodsList().stream())
                .collect(Collectors.toList());
        assertEquals(1, allGoodsAllocated.size());
        assertTrue(allGoodsAllocated.contains(0));
    }

    @Test
    public void testMultipleGoodsWithOneAgentAndZeroValuation() {
        int[][] valuationMatrix = {
                {0, 0, 0}
        };

        when(fairDivisionInput.getAgentNumber()).thenReturn(1);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(3);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();
        Allocation allocation = allocations.get(0);

        assertEquals(3, allocation.getGoodsList().size());
        assertTrue(allocation.getGoodsList().containsAll(List.of(0, 1, 2)));
    }

    @Test
    public void testAllocationWithNoAgents() {
        int[][] valuationMatrix = new int[0][0];

        when(fairDivisionInput.getAgentNumber()).thenReturn(0);
        when(fairDivisionInput.getGoodsNumber()).thenReturn(0);
        when(fairDivisionInput.getValuationMatrix()).thenReturn(valuationMatrix);

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput);

        List<Allocation> allocations = output.getAllocations();

        assertTrue(allocations.isEmpty());
    }
}
