package com.kivotos.fairdivision.mapper;

import com.kivotos.fairdivision.dto.WebsiteInputDTO;
import com.kivotos.fairdivision.model.Allocation;
import com.kivotos.fairdivision.model.FairDivisionInput;
import com.kivotos.fairdivision.model.FairDivisionOutput;
import com.kivotos.fairdivision.util.ValuationChecker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface FairDivisionInputMapper {

    @Mapping(target = "agentNumber", source = "agentSliderValue")
    @Mapping(target = "goodsNumber", source = "goodsSliderValue")
    @Mapping(target = "valuationType", source = "valuationDropdownValue")
    @Mapping(target = "algorithmId", source = "algorithmDropdownValue")
    @Mapping(target = "valuationMatrix", expression = "java(convertValuations(dto.getValuationContainer(), dto.getAgentSliderValue(), dto.getGoodsSliderValue()))")
    @Mapping(target = "leximinFirstAllocation", expression = "java(convertLeximinAllocations(dto.getLeximinFirstAllocation()))")
    @Mapping(target = "leximinSecondAllocation", expression = "java(convertLeximinAllocations(dto.getLeximinSecondAllocation()))")
    FairDivisionInput toFairDivisionInput(WebsiteInputDTO dto);

    default int[][] convertValuations(String valuations, int agentNumber, int goodsNumber)  {
        String[] values = valuations.split(",");


        int[][] matrix = new int[agentNumber][goodsNumber];
        for (int i = 0; i < agentNumber; i++) {
            for (int j = 0; j < goodsNumber; j++) {
                matrix[i][j] = Integer.parseInt(values[i * goodsNumber + j]);
            }
        }
        ValuationChecker.setValuationMatrix(matrix);

        return matrix;
    }

    default FairDivisionOutput convertLeximinAllocations( int[][] leximinAllocation) {
        List<Allocation> allocationsList = new ArrayList<>();


        for(int i=0; i<leximinAllocation.length;i++) {
            Allocation allocation = new Allocation(i);
            for(int j=0; j<leximinAllocation[i].length;j++) {
                allocation.add(leximinAllocation[i][j], ValuationChecker.getValuationMatrix());
            }
            allocationsList.add(allocation);
        }

        return new FairDivisionOutput(allocationsList);
    }
}
