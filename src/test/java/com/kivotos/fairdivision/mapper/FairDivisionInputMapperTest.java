package com.kivotos.fairdivision.mapper;

import com.kivotos.fairdivision.dto.WebsiteInputDTO;
import com.kivotos.fairdivision.model.FairDivisionInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FairDivisionInputMapperTest {

    private FairDivisionInputMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(FairDivisionInputMapper.class);
    }

    @Test
    void testToFairDivisionInput_SimpleMapping() {
        // Arrange
        WebsiteInputDTO dto = new WebsiteInputDTO();
        dto.setAgentSliderValue(2);
        dto.setGoodsSliderValue(3);
        dto.setValuationDropdownValue(1);  // Using integer value
        dto.setAlgorithmDropdownValue(3);  // Using integer value
        dto.setValuationContainer("1,2,3,4,5,6");

        // Act
        FairDivisionInput fairDivisionInput = mapper.toFairDivisionInput(dto);

        // Assert
        assertEquals(2, fairDivisionInput.getAgentNumber());
        assertEquals(3, fairDivisionInput.getGoodsNumber());
        assertEquals(1, fairDivisionInput.getValuationType());  // Expecting integer value
        assertEquals(3, fairDivisionInput.getAlgorithmId());    // Expecting integer value

        int[][] expectedMatrix = {
                {1, 2, 3},
                {4, 5, 6}
        };
        assertArrayEquals(expectedMatrix, fairDivisionInput.getValuationMatrix());
    }

    @Test
    void testConvertValuations_ValidMatrix() {
        // Arrange
        String valuations = "1,2,3,4,5,6";
        int agentNumber = 2;
        int goodsNumber = 3;

        // Act
        int[][] matrix = mapper.convertValuations(valuations, agentNumber, goodsNumber);

        // Assert
        int[][] expectedMatrix = {
                {1, 2, 3},
                {4, 5, 6}
        };
        assertArrayEquals(expectedMatrix, matrix);
    }

    @Test
    void testConvertValuations_EmptyMatrix() {
        // Arrange
        String valuations = "";
        int agentNumber = 2;
        int goodsNumber = 3;

        // Act
        int[][] matrix = mapper.convertValuations(valuations, agentNumber, goodsNumber);

        // Assert
        int[][] expectedMatrix = {
                {0, 0, 0},
                {0, 0, 0}
        };
        assertArrayEquals(expectedMatrix, matrix);
    }

    @Test
    void testToFairDivisionInput_InvalidValuationsFormat() {
        // Arrange
        WebsiteInputDTO dto = new WebsiteInputDTO();
        dto.setAgentSliderValue(2);
        dto.setGoodsSliderValue(3);
        dto.setValuationDropdownValue(2);  // Using integer value
        dto.setAlgorithmDropdownValue(4);  // Using integer value
        dto.setValuationContainer("1,2,three,4,5,6");  // Contains an invalid value

        // Act & Assert
        try {
            mapper.toFairDivisionInput(dto);
        } catch (NumberFormatException e) {
            assertEquals("For input string: \"three\"", e.getMessage());
        }
    }

    @Test
    void testToFairDivisionInput_MatrixWithDifferentDimensions() {
        // Arrange
        WebsiteInputDTO dto = new WebsiteInputDTO();
        dto.setAgentSliderValue(3);
        dto.setGoodsSliderValue(2);
        dto.setValuationDropdownValue(3);  // Using integer value
        dto.setAlgorithmDropdownValue(5);  // Using integer value
        dto.setValuationContainer("1,2,3,4,5,6");

        // Act
        FairDivisionInput fairDivisionInput = mapper.toFairDivisionInput(dto);

        // Assert
        assertEquals(3, fairDivisionInput.getAgentNumber());
        assertEquals(2, fairDivisionInput.getGoodsNumber());
        assertEquals(3, fairDivisionInput.getValuationType());  // Expecting integer value
        assertEquals(5, fairDivisionInput.getAlgorithmId());    // Expecting integer value

        int[][] expectedMatrix = {
                {1, 2},
                {3, 4},
                {5, 6}
        };
        assertArrayEquals(expectedMatrix, fairDivisionInput.getValuationMatrix());
    }
}
