package com.kivotos.fairdivision.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Allocation {

    private int agentId;
    private int highestValuedGood;
    private int lowestValuedGood;
    private int highestValuedGoodIndex;
    private int lowestValuedGoodIndex;
    private List<Integer> goodsList;

    public Allocation(int agentId) {
        this.agentId = agentId;
        this.highestValuedGood = Integer.MIN_VALUE;
        this.lowestValuedGood = Integer.MAX_VALUE;
        this.goodsList = new ArrayList<>();
    }

    public Allocation(Allocation allocation) {
        this.agentId = allocation.agentId;
        this.goodsList = new ArrayList<>(allocation.goodsList);
        this.highestValuedGood = allocation.highestValuedGood;
        this.lowestValuedGood = allocation.lowestValuedGood;
        this.highestValuedGoodIndex = allocation.highestValuedGoodIndex;
        this.lowestValuedGoodIndex = allocation.lowestValuedGoodIndex;
    }

    public void add(int item, int[][] valuationMatrix) {
        int index = this.goodsList.size(); // Get the index where the item will be added
        this.goodsList.add(item);

        if (this.highestValuedGood < valuationMatrix[this.agentId][item]) {
            this.highestValuedGood = valuationMatrix[this.agentId][item];
            this.highestValuedGoodIndex = index;
        }

        if (this.lowestValuedGood > valuationMatrix[this.agentId][item]) {
            this.lowestValuedGood = valuationMatrix[this.agentId][item];
            this.lowestValuedGoodIndex = index;
        }
    }
}

