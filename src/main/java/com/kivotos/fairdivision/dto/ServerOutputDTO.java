package com.kivotos.fairdivision.dto;

import com.kivotos.fairdivision.model.Allocation;
import lombok.Data;

import java.util.List;

@Data
public class ServerOutputDTO {

    private List<Allocation> allocations;
    private boolean isEF;
    private boolean isEF1;
    private boolean isEFX;
    private boolean isProp;
    private double nashWelfareValue;
    private String errorMessage;
}


