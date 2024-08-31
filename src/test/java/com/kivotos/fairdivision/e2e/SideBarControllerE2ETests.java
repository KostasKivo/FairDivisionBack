package com.kivotos.fairdivision.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kivotos.fairdivision.dto.WebsiteInputDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SideBarControllerE2ETests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testIncorrectValuationContainerSize() throws Exception {
        WebsiteInputDTO inputDTO = new WebsiteInputDTO();
        inputDTO.setAgentSliderValue(3);
        inputDTO.setGoodsSliderValue(6);
        inputDTO.setAlgorithmDropdownValue(3); // MatchAndFreeze
        inputDTO.setValuationContainer("1,3,1,1,1,3,3,3,1,1,3,3,3,1,1"); // Example values

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(inputDTO);

        Exception exception = assertThrows(Exception.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.post("/submit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonPayload))
                    .andExpect(status().is5xxServerError()) // Expect a server error status
                    .andReturn();
        });

        // Check if the exception message contains the expected error message
        String expectedMessage = "The number of valuation values does not match the expected matrix size.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage), "Expected exception message to contain: " + expectedMessage);
    }

    @Test
    public void testSuccessfulInvocationOfRoundRobinAlgorithm() throws Exception {
        WebsiteInputDTO inputDTO = new WebsiteInputDTO();
        inputDTO.setAgentSliderValue(2);
        inputDTO.setGoodsSliderValue(4);
        inputDTO.setAlgorithmDropdownValue(1);
        inputDTO.setValuationContainer("2,6,10,1,1,10,8,4");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(inputDTO);

        mockMvc.perform(post("/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nashWelfareValue").value(168))
                .andExpect(jsonPath("$.errorMessage").doesNotExist())
                .andExpect(jsonPath("$.efx").value(true))
                .andExpect(jsonPath("$.ef").value(true))
                .andExpect(jsonPath("$.prop").value(true))
                .andExpect(jsonPath("$.ef1").value(true))

                // Check allocations for agent 0
                .andExpect(jsonPath("$.allocations[0].agentId").value(0))
                .andExpect(jsonPath("$.allocations[0].highestValuedGood").value(10))
                .andExpect(jsonPath("$.allocations[0].lowestValuedGood").value(2))
                .andExpect(jsonPath("$.allocations[0].highestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[0].lowestValuedGoodIndex").value(1))
                .andExpect(jsonPath("$.allocations[0].goodsList[0]").value(2))
                .andExpect(jsonPath("$.allocations[0].goodsList[1]").value(0))

                // Check allocations for agent 1
                .andExpect(jsonPath("$.allocations[1].agentId").value(1))
                .andExpect(jsonPath("$.allocations[1].highestValuedGood").value(10))
                .andExpect(jsonPath("$.allocations[1].lowestValuedGood").value(4))
                .andExpect(jsonPath("$.allocations[1].highestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[1].lowestValuedGoodIndex").value(1))
                .andExpect(jsonPath("$.allocations[1].goodsList[0]").value(1))
                .andExpect(jsonPath("$.allocations[1].goodsList[1]").value(3))

                .andReturn();
    }

    @Test
    public void testSuccessfulInvocationOfRoundRobinAlgorithm2Agents1Good() throws Exception {
        WebsiteInputDTO inputDTO = new WebsiteInputDTO();
        inputDTO.setAgentSliderValue(2);
        inputDTO.setGoodsSliderValue(1);
        inputDTO.setAlgorithmDropdownValue(1);
        inputDTO.setValuationContainer("1,4");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(inputDTO);

        mockMvc.perform(post("/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nashWelfareValue").value(0))
                .andExpect(jsonPath("$.errorMessage").doesNotExist())
                .andExpect(jsonPath("$.efx").value(true))
                .andExpect(jsonPath("$.ef").value(false))
                .andExpect(jsonPath("$.prop").value(false))
                .andExpect(jsonPath("$.ef1").value(true))

                .andExpect(jsonPath("$.allocations[0].agentId").value(0))
                .andExpect(jsonPath("$.allocations[0].highestValuedGood").value(1))
                .andExpect(jsonPath("$.allocations[0].lowestValuedGood").value(1))
                .andExpect(jsonPath("$.allocations[0].highestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[0].lowestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[0].goodsList[0]").value(0))

                .andExpect(jsonPath("$.allocations[1].agentId").value(1))
                .andExpect(jsonPath("$.allocations[1].highestValuedGood").value(0))
                .andExpect(jsonPath("$.allocations[1].lowestValuedGood").value(0))
                .andExpect(jsonPath("$.allocations[1].highestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[1].lowestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[1].goodsList").isEmpty())

                .andReturn();
    }

    @Test
    public void testSuccessfulInvocationOfEnvyCycleAlgorithmWithCycleResolution() throws Exception {
        WebsiteInputDTO inputDTO = new WebsiteInputDTO();
        inputDTO.setAgentSliderValue(3);
        inputDTO.setGoodsSliderValue(6);
        inputDTO.setAlgorithmDropdownValue(2);
        inputDTO.setValuationContainer("2,1,4,6,6,9,4,6,10,6,8,5,2,3,8,4,1,7");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(inputDTO);

        mockMvc.perform(post("/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nashWelfareValue").value(1920))
                .andExpect(jsonPath("$.errorMessage").doesNotExist())
                .andExpect(jsonPath("$.efx").value(true))
                .andExpect(jsonPath("$.ef").value(false))
                .andExpect(jsonPath("$.prop").value(true))
                .andExpect(jsonPath("$.ef1").value(true))

                .andExpect(jsonPath("$.allocations[0].agentId").value(0))
                .andExpect(jsonPath("$.allocations[0].highestValuedGood").value(9))
                .andExpect(jsonPath("$.allocations[0].lowestValuedGood").value(6))
                .andExpect(jsonPath("$.allocations[0].highestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[0].lowestValuedGoodIndex").value(1))
                .andExpect(jsonPath("$.allocations[0].goodsList[0]").value(5))
                .andExpect(jsonPath("$.allocations[0].goodsList[1]").value(4))

                .andExpect(jsonPath("$.allocations[1].agentId").value(1))
                .andExpect(jsonPath("$.allocations[1].highestValuedGood").value(6))
                .andExpect(jsonPath("$.allocations[1].lowestValuedGood").value(4))
                .andExpect(jsonPath("$.allocations[1].highestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[1].lowestValuedGoodIndex").value(2))
                .andExpect(jsonPath("$.allocations[1].goodsList[0]").value(3))
                .andExpect(jsonPath("$.allocations[1].goodsList[1]").value(1))
                .andExpect(jsonPath("$.allocations[1].goodsList[2]").value(0))

                .andExpect(jsonPath("$.allocations[2].agentId").value(2))
                .andExpect(jsonPath("$.allocations[2].highestValuedGood").value(8))
                .andExpect(jsonPath("$.allocations[2].lowestValuedGood").value(8))
                .andExpect(jsonPath("$.allocations[2].highestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[2].lowestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[2].goodsList[0]").value(2))

                .andReturn();
    }

    @Test
    public void testUnsuccessfulInvocationOfMatchAndFreezeAlgorithmForRandomInstance() throws Exception {
        WebsiteInputDTO inputDTO = new WebsiteInputDTO();
        inputDTO.setAgentSliderValue(2);
        inputDTO.setGoodsSliderValue(2);
        inputDTO.setAlgorithmDropdownValue(3); // MatchAndFreeze
        inputDTO.setValuationContainer("3,7,5,6"); // Non-bivalued instance

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(inputDTO);

        mockMvc.perform(post("/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nashWelfareValue").value(0))
                .andExpect(jsonPath("$.errorMessage").value("Not a 2-valued instance."))
                .andExpect(jsonPath("$.efx").value(false))
                .andExpect(jsonPath("$.ef").value(false))
                .andExpect(jsonPath("$.prop").value(false))
                .andExpect(jsonPath("$.ef1").value(false))
                .andExpect(jsonPath("$.allocations").isArray())
                .andExpect(jsonPath("$.allocations").isEmpty())
                .andExpect(jsonPath("$.valuationMatrix").isEmpty())
                .andReturn();
    }


    @Test
    public void testSuccessfulInvocationOfMatchAndFreezeAlgorithm() throws Exception {
        WebsiteInputDTO inputDTO = new WebsiteInputDTO();
        inputDTO.setAgentSliderValue(3);
        inputDTO.setGoodsSliderValue(6);
        inputDTO.setAlgorithmDropdownValue(3); // Match and Freeze
        inputDTO.setValuationContainer("1,1,1,1,3,3,1,1,3,1,3,1,1,3,1,3,1,1"); // Example values

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(inputDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nashWelfareValue").value(144))
                .andExpect(jsonPath("$.errorMessage").doesNotExist())
                .andExpect(jsonPath("$.efx").value(true))
                .andExpect(jsonPath("$.ef").value(true))
                .andExpect(jsonPath("$.prop").value(true))
                .andExpect(jsonPath("$.ef1").value(true))

                .andExpect(jsonPath("$.allocations[0].agentId").value(0))
                .andExpect(jsonPath("$.allocations[0].highestValuedGood").value(3))
                .andExpect(jsonPath("$.allocations[0].lowestValuedGood").value(3))
                .andExpect(jsonPath("$.allocations[0].highestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[0].lowestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[0].goodsList[0]").value(4))
                .andExpect(jsonPath("$.allocations[0].goodsList[1]").value(5))

                .andExpect(jsonPath("$.allocations[1].agentId").value(1))
                .andExpect(jsonPath("$.allocations[1].highestValuedGood").value(3))
                .andExpect(jsonPath("$.allocations[1].lowestValuedGood").value(1))
                .andExpect(jsonPath("$.allocations[1].highestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[1].lowestValuedGoodIndex").value(1))
                .andExpect(jsonPath("$.allocations[1].goodsList[0]").value(2))
                .andExpect(jsonPath("$.allocations[1].goodsList[1]").value(0))

                .andExpect(jsonPath("$.allocations[2].agentId").value(2))
                .andExpect(jsonPath("$.allocations[2].highestValuedGood").value(3))
                .andExpect(jsonPath("$.allocations[2].lowestValuedGood").value(3))
                .andExpect(jsonPath("$.allocations[2].highestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[2].lowestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[2].goodsList[0]").value(1))
                .andExpect(jsonPath("$.allocations[2].goodsList[1]").value(3))
                .andReturn();
    }


    @Test
    public void testSuccessfulInvocationOfLeximinPlusPlusAlgorithmIdenticalValuation() throws Exception {
        WebsiteInputDTO inputDTO = new WebsiteInputDTO();
        inputDTO.setAgentSliderValue(3);
        inputDTO.setGoodsSliderValue(4);
        inputDTO.setAlgorithmDropdownValue(4); // Leximin++
        inputDTO.setValuationContainer("6,2,4,8,6,2,4,8,6,2,4,8"); // Example values

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(inputDTO);

        mockMvc.perform(post("/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorMessage").doesNotExist())
                .andExpect(jsonPath("$.efx").value(true))
                .andExpect(jsonPath("$.ef1").value(true))

                .andReturn();
    }



    @Test
    public void testSuccessfulInvocationOfLeximinPlusPlusAlgorithmGeneralValuation() throws Exception {
        WebsiteInputDTO inputDTO = new WebsiteInputDTO();
        inputDTO.setAgentSliderValue(3);
        inputDTO.setGoodsSliderValue(4);
        inputDTO.setAlgorithmDropdownValue(4); // Leximin++
        inputDTO.setValuationContainer("1,1,10,9,3,2,5,4,2,5,2,5"); // Example values

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(inputDTO);

        mockMvc.perform(post("/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nashWelfareValue").value(360))
                .andExpect(jsonPath("$.errorMessage").doesNotExist())
                .andExpect(jsonPath("$.efx").value(false))
                .andExpect(jsonPath("$.ef").value(false))
                .andExpect(jsonPath("$.prop").value(true))
                .andExpect(jsonPath("$.ef1").value(true))

                .andExpect(jsonPath("$.allocations[0].agentId").value(0))
                .andExpect(jsonPath("$.allocations[0].highestValuedGood").value(9))
                .andExpect(jsonPath("$.allocations[0].lowestValuedGood").value(9))
                .andExpect(jsonPath("$.allocations[0].highestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[0].lowestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[0].goodsList[0]").value(3))

                .andExpect(jsonPath("$.allocations[1].agentId").value(1))
                .andExpect(jsonPath("$.allocations[1].highestValuedGood").value(5))
                .andExpect(jsonPath("$.allocations[1].lowestValuedGood").value(3))
                .andExpect(jsonPath("$.allocations[1].highestValuedGoodIndex").value(1))
                .andExpect(jsonPath("$.allocations[1].lowestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[1].goodsList[0]").value(0))
                .andExpect(jsonPath("$.allocations[1].goodsList[1]").value(2))

                .andExpect(jsonPath("$.allocations[2].agentId").value(2))
                .andExpect(jsonPath("$.allocations[2].highestValuedGood").value(5))
                .andExpect(jsonPath("$.allocations[2].lowestValuedGood").value(5))
                .andExpect(jsonPath("$.allocations[2].highestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[2].lowestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[2].goodsList[0]").value(1))

                .andReturn();
    }


    @Test
    public void testSuccessfulInvocationOfMaximumNashWelfareAlgorithmGeneralValuation() throws Exception {
        WebsiteInputDTO inputDTO = new WebsiteInputDTO();
        inputDTO.setAgentSliderValue(3);
        inputDTO.setGoodsSliderValue(4);
        inputDTO.setAlgorithmDropdownValue(5); // Maximum Nash Welfare
        inputDTO.setValuationContainer("6,4,2,10,1,6,7,7,3,1,8,8"); // Example values

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(inputDTO);

        mockMvc.perform(post("/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nashWelfareValue").value(768))
                .andExpect(jsonPath("$.errorMessage").doesNotExist())
                .andExpect(jsonPath("$.efx").value(false))
                .andExpect(jsonPath("$.ef").value(false))
                .andExpect(jsonPath("$.prop").value(false))
                .andExpect(jsonPath("$.ef1").value(true))

                .andExpect(jsonPath("$.allocations[0].agentId").value(0))
                .andExpect(jsonPath("$.allocations[0].highestValuedGood").value(10))
                .andExpect(jsonPath("$.allocations[0].lowestValuedGood").value(6))
                .andExpect(jsonPath("$.allocations[0].highestValuedGoodIndex").value(1))
                .andExpect(jsonPath("$.allocations[0].lowestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[0].goodsList[0]").value(0))
                .andExpect(jsonPath("$.allocations[0].goodsList[1]").value(3))

                .andExpect(jsonPath("$.allocations[1].agentId").value(1))
                .andExpect(jsonPath("$.allocations[1].highestValuedGood").value(6))
                .andExpect(jsonPath("$.allocations[1].lowestValuedGood").value(6))
                .andExpect(jsonPath("$.allocations[1].highestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[1].lowestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[1].goodsList[0]").value(1))

                .andExpect(jsonPath("$.allocations[2].agentId").value(2))
                .andExpect(jsonPath("$.allocations[2].highestValuedGood").value(8))
                .andExpect(jsonPath("$.allocations[2].lowestValuedGood").value(8))
                .andExpect(jsonPath("$.allocations[2].highestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[2].lowestValuedGoodIndex").value(0))
                .andExpect(jsonPath("$.allocations[2].goodsList[0]").value(2))

                .andReturn();
    }


}
