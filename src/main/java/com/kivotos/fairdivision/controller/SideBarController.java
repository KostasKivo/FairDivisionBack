package com.kivotos.fairdivision.controller;


import com.kivotos.fairdivision.dto.WebsiteInputDTO;
import com.kivotos.fairdivision.mapper.FairDivisionInputMapper;
import com.kivotos.fairdivision.model.Allocation;
import com.kivotos.fairdivision.model.FairDivisionInput;
import com.kivotos.fairdivision.service.InputHandlerService;
import com.kivotos.fairdivision.util.FairDivisionAlgorithms;
import com.kivotos.fairdivision.util.PropertyChecker;
import com.kivotos.fairdivision.util.ValuationChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SideBarController {

    @Autowired
    private InputHandlerService inputHandlerService;

    @PostMapping(value = "/submit")
    public ResponseEntity<WebsiteInputDTO> submitInput(@RequestBody WebsiteInputDTO websiteInputDTO) {

            FairDivisionInput fairDivisionInput = FairDivisionInputMapper.INSTANCE.toFairDivisionInput(websiteInputDTO);
            System.out.println(fairDivisionInput.toString());

//            fairDivisionInput = inputHandlerService.processInput(fairDivisionInput);

            websiteInputDTO = FairDivisionInputMapper.INSTANCE.toWebsiteInputDTO(fairDivisionInput);

            List<Allocation> output = FairDivisionAlgorithms.RoundRobin(fairDivisionInput.getAgentNumber(),fairDivisionInput.getGoodsNumber(),fairDivisionInput.getValuationMatrix());
            System.out.println(output);

            ValuationChecker v = new ValuationChecker(fairDivisionInput.getValuationMatrix());

            PropertyChecker.isEF(output,v);


            return ResponseEntity.ok(websiteInputDTO);
    }

}
