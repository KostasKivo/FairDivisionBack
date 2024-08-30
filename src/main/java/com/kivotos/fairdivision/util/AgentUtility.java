package com.kivotos.fairdivision.util;

import lombok.Data;

@Data
public class AgentUtility {
    int agentIndex;
    int utility;
    int bundleSize;

    public AgentUtility(int agentIndex, int utility, int bundleSize) {
        this.agentIndex = agentIndex;
        this.utility = utility;
        this.bundleSize = bundleSize;
    }
}
