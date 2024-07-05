package com.kivotos.fairdivision.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Allocation {

    private int agentId;
    private int highestValuedGood;
    private int lowestValuedGood;
    private List<Integer> goodsList;

    public Allocation(int agentId) {
        this.agentId = agentId;
        this.highestValuedGood = Integer.MIN_VALUE;
        this.lowestValuedGood = Integer.MAX_VALUE;
        this.goodsList = new ArrayList<>();
    }

    public void add(int item, int[][] valuationMatrix) {
        this.goodsList.add(item);

        if (this.highestValuedGood < valuationMatrix[this.agentId][item]) {
            this.highestValuedGood = valuationMatrix[this.agentId][item];
        }

        if (this.lowestValuedGood > valuationMatrix[this.agentId][item]) {
            this.lowestValuedGood = valuationMatrix[this.agentId][item];
        }
    }
}

