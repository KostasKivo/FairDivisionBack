package com.kivotos.fairdivision.dto;

import lombok.Data;

/** Valuation types:
 *  Additive 1
 *
 *
 */

@Data
public class WebsiteInputDTO {
    private int agentSliderValue;
    private int goodsSliderValue;
    private int valuationDropdownValue;
    private int algorithmDropdownValue;
    private String valuationContainer;
    private int[][] leximinFirstAllocation;
    private int[][] leximinSecondAllocation;
}
