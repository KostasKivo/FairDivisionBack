package com.kivotos.fairdivision.mapper;

import com.kivotos.fairdivision.dto.WebsiteInputDTO;
import com.kivotos.fairdivision.model.FairDivisionInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FairDivisionInputMapper {

    @Mapping(target = "agentNumber", source = "agentSliderValue")
    @Mapping(target = "goodsNumber", source = "goodsSliderValue")
    @Mapping(target = "valuationType", source = "valuationDropdownValue")
    @Mapping(target = "algorithmId", source = "algorithmDropdownValue")
    @Mapping(target = "valuationMatrix", expression = "java(convertValuations(dto.getValuationContainer(), dto.getAgentSliderValue(), dto.getGoodsSliderValue()))")
    FairDivisionInput toFairDivisionInput(WebsiteInputDTO dto);

    default int[][] convertValuations(String valuations, int agentNumber, int goodsNumber)  {
        String[] values = valuations.split(",");


        int[][] matrix = new int[agentNumber][goodsNumber];
        for (int i = 0; i < agentNumber; i++) {
            for (int j = 0; j < goodsNumber; j++) {
                matrix[i][j] = Integer.parseInt(values[i * goodsNumber + j]);
            }
        }
        return matrix;
    }

    default String convertMatrixToString(int[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                sb.append(matrix[i][j]);
                if (j < matrix[i].length - 1) {
                    sb.append(",");
                }
            }
            if (i < matrix.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
