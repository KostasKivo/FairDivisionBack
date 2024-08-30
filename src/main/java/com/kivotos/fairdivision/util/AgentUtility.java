package com.kivotos.fairdivision.util;

import lombok.Data;

@Data
public class AgentUtility {
    int agentIndex;
    int utility;
    int bundleSize;
    int product;

    public AgentUtility(int agentIndex, int utility, int bundleSize) {
        this.agentIndex = agentIndex;
        this.utility = utility;
        this.bundleSize = bundleSize;
    }

    public AgentUtility(int agentIndex, int product) {
        this.agentIndex = agentIndex;
        this.product = product;
    }

    public int getAgentIndex() {
        return agentIndex;
    }

    public int getProduct() {
        return product;
    }
}
