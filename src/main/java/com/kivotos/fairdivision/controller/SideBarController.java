package com.kivotos.fairdivision.controller;


import com.kivotos.fairdivision.dto.ServerOutputDTO;
import com.kivotos.fairdivision.dto.WebsiteInputDTO;
import com.kivotos.fairdivision.service.FairDivisionService;
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

            ServerOutputDTO output = fairDivisionService.performAllocation(websiteInputDTO);

            return ResponseEntity.ok(output);
    }

}
