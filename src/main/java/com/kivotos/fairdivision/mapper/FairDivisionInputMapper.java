package com.kivotos.fairdivision.mapper;

import com.kivotos.fairdivision.dto.WebsiteInputDTO;
import com.kivotos.fairdivision.exception.InsufficientValuationsException;
import com.kivotos.fairdivision.model.FairDivisionInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FairDivisionInputMapper {
    FairDivisionInputMapper INSTANCE = Mappers.getMapper(FairDivisionInputMapper.class);

    @Mapping(target = "agentNumber", source = "agentSliderValue")
    @Mapping(target = "goodsNumber", source = "goodsSliderValue")
    @Mapping(target = "valuationType", source = "valuationDropdownValue")
    @Mapping(target = "valuationMatrix", expression = "java(convertValuations(dto.getValuationContainer(), dto.getAgentSliderValue(), dto.getGoodsSliderValue()))")
    FairDivisionInput toFairDivisionInput(WebsiteInputDTO dto) throws InsufficientValuationsException;

    @Mapping(target = "agentSliderValue", source = "agentNumber")
    @Mapping(target = "goodsSliderValue", source = "goodsNumber")
    @Mapping(target = "valuationDropdownValue", source = "valuationType")
    @Mapping(target = "message", ignore = true)
    @Mapping(target = "valuationContainer", expression = "java(convertMatrixToString(dto.getValuationMatrix()))")
    WebsiteInputDTO toWebsiteInputDTO(FairDivisionInput dto);

    default int[][] convertValuations(String valuations, int agentNumber, int goodsNumber) throws InsufficientValuationsException {
        String[] values = valuations.split(",");
        int expectedSize = agentNumber * goodsNumber;
        int actualSize = values.length;


        if (actualSize != expectedSize) {
            throw new InsufficientValuationsException("The number of valuations must be equal to " + agentNumber * goodsNumber + ".");
        }

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
