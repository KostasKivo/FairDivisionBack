package com.kivotos.fairdivision.mapper;

import com.kivotos.fairdivision.dto.WebsiteInputDTO;
import com.kivotos.fairdivision.model.Allocation;
import com.kivotos.fairdivision.model.FairDivisionInput;
import com.kivotos.fairdivision.model.FairDivisionOutput;
import com.kivotos.fairdivision.util.ValuationChecker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface FairDivisionInputMapper {

    @Mapping(target = "agentNumber", source = "agentSliderValue")
    @Mapping(target = "goodsNumber", source = "goodsSliderValue")
    @Mapping(target = "valuationType", source = "valuationDropdownValue")
    @Mapping(target = "algorithmId", source = "algorithmDropdownValue")
    @Mapping(target = "valuationMatrix", expression = "java(convertValuations(dto.getValuationContainer(), dto.getAgentSliderValue(), dto.getGoodsSliderValue()))")
    @Mapping(target = "leximinFirstAllocation", expression = "java(convertLeximinAllocations(dto.getLeximinFirstAllocation(),dto.getGoodsSliderValue()))")
    @Mapping(target = "leximinSecondAllocation", expression = "java(convertLeximinAllocations(dto.getLeximinSecondAllocation(),dto.getGoodsSliderValue()))")
    @Mapping(target = "identicalValuation", expression = "java(isValuationIdentical())")
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

    default FairDivisionOutput convertLeximinAllocations(int[][] leximinAllocation, int numberOfGoods) {
        List<Allocation> allocationsList = new ArrayList<>();
        Set<Integer> uniqueItems = new HashSet<>();

        // Check if the total number of items matches the number of goods
        int totalItems = 0;
        for (int i = 0; i < leximinAllocation.length; i++) {
            totalItems += leximinAllocation[i].length;
            for (int j = 0; j < leximinAllocation[i].length; j++) {
                if (leximinAllocation[i][j] < 0 || leximinAllocation[i][j] >= numberOfGoods) {
                    return null;
                }
                uniqueItems.add(leximinAllocation[i][j]);
            }
        }

        // Ensure the number of unique items equals the number of goods
        if (uniqueItems.size() != numberOfGoods) {
            return null;
        }

        // Ensure the total number of items equals the number of goods
        if (totalItems != numberOfGoods) {
            return null;
        }

        // Create allocations
        for (int i = 0; i < leximinAllocation.length; i++) {
            Allocation allocation = new Allocation(i);
            for (int j = 0; j < leximinAllocation[i].length; j++) {
                allocation.add(leximinAllocation[i][j], ValuationChecker.getValuationMatrix());
            }
            allocationsList.add(allocation);
        }

        return new FairDivisionOutput(allocationsList);
    }


    default boolean isValuationIdentical() {
        boolean isIdentical = true;

        int[][] valuationMatrix = ValuationChecker.getValuationMatrix();

        int[] referenceValuations = valuationMatrix[0];

        for (int i = 1; i < valuationMatrix.length; i++) {
            for (int j = 0; j < valuationMatrix[0].length; j++) {
                if (valuationMatrix[i][j] != referenceValuations[j]) {
                    isIdentical = false;
                    break;
                }
            }
            if (!isIdentical) {
                break;
            }
        }

        return isIdentical;
    }
}
