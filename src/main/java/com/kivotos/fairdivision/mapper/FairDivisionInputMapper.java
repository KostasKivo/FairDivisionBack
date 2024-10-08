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

    default int[][] convertValuations(String valuations, int agentNumber, int goodsNumber) {
        int[][] matrix = new int[agentNumber][goodsNumber];

        if (valuations == null || valuations.trim().isEmpty()) {
            return matrix;
        }

        String[] values = valuations.split(",");

        if (values.length != agentNumber * goodsNumber) {
            throw new IllegalArgumentException("The number of valuation values does not match the expected matrix size.");
        }

        for (int i = 0; i < agentNumber; i++) {
            for (int j = 0; j < goodsNumber; j++) {
                matrix[i][j] = Integer.parseInt(values[i * goodsNumber + j]);
            }
        }

        return matrix;
    }

}
