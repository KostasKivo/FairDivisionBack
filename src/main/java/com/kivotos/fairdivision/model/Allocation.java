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

    public void recalculateIndexes(int[][] valuationMatrix) {
        int agentId = this.getAgentId();
        List<Integer> goodsList = this.getGoodsList();

        int highestValuedGood = Integer.MIN_VALUE;
        int highestValuedGoodIndex = -1;
        int lowestValuedGood = Integer.MAX_VALUE;
        int lowestValuedGoodIndex = -1;

        for (int i = 0; i < goodsList.size(); i++) {
            int item = goodsList.get(i);
            int value = valuationMatrix[agentId][item];

            if (value > highestValuedGood) {
                highestValuedGood = value;
                highestValuedGoodIndex = i;
            }

            if (value < lowestValuedGood) {
                lowestValuedGood = value;
                lowestValuedGoodIndex = i;
            }
        }

        this.setHighestValuedGood(highestValuedGood);
        this.setHighestValuedGoodIndex(highestValuedGoodIndex);
        this.setLowestValuedGood(lowestValuedGood);
        this.setLowestValuedGoodIndex(lowestValuedGoodIndex);
    }

}

