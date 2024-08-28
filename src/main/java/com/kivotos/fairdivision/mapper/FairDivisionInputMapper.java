package com.kivotos.fairdivision.mapper;

import com.kivotos.fairdivision.dto.WebsiteInputDTO;
import com.kivotos.fairdivision.model.FairDivisionInput;
import com.kivotos.fairdivision.util.ValuationChecker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FairDivisionInputMapper {

    @Mapping(target = "agentNumber", source = "agentSliderValue")
    @Mapping(target = "goodsNumber", source = "goodsSliderValue")
    @Mapping(target = "valuationType", source = "valuationDropdownValue")
    @Mapping(target = "algorithmId", source = "algorithmDropdownValue")
    @Mapping(target = "valuationMatrix", expression = "java(convertValuations(dto.getValuationContainer(), dto.getAgentSliderValue(), dto.getGoodsSliderValue()))")
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
