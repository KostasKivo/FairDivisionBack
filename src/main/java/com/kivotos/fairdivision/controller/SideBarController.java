package com.kivotos.fairdivision.controller;


import com.kivotos.fairdivision.dto.ServerOutputDTO;
import com.kivotos.fairdivision.dto.WebsiteInputDTO;
import com.kivotos.fairdivision.mapper.FairDivisionInputMapper;
import com.kivotos.fairdivision.model.FairDivisionInput;
import com.kivotos.fairdivision.model.FairDivisionOutput;
import com.kivotos.fairdivision.service.FairDivisionService;
import com.kivotos.fairdivision.util.PropertyChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SideBarController {

    @Autowired
    private FairDivisionService fairDivisionService;

    @PostMapping(value = "/submit")
    public ResponseEntity<ServerOutputDTO> submitInput(@RequestBody WebsiteInputDTO websiteInputDTO) {

            FairDivisionInput fairDivisionInput = FairDivisionInputMapper.INSTANCE.toFairDivisionInput(websiteInputDTO);

            ServerOutputDTO output = fairDivisionService.performAllocation(fairDivisionInput.getAlgorithmId(),fairDivisionInput.getAgentNumber(),fairDivisionInput.getGoodsNumber(),fairDivisionInput.getValuationMatrix());

            return ResponseEntity.ok(output);
    }

}
