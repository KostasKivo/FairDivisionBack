package com.kivotos.fairdivision.service;

import com.kivotos.fairdivision.algorithms.FairDivisionAlgorithm;
import com.kivotos.fairdivision.algorithms.factory.FairDivisionAlgorithmFactory;
import com.kivotos.fairdivision.dto.ServerOutputDTO;
import com.kivotos.fairdivision.dto.WebsiteInputDTO;
import com.kivotos.fairdivision.mapper.FairDivisionInputMapper;
import com.kivotos.fairdivision.mapper.FairDivisionOutputMapper;
import com.kivotos.fairdivision.model.FairDivisionInput;
import com.kivotos.fairdivision.model.FairDivisionOutput;
import com.kivotos.fairdivision.util.ValuationChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FairDivisionService {

    @Autowired
    FairDivisionOutputMapper fairDivisionOutputMapper;

    @Autowired
    FairDivisionInputMapper fairDivisionInputMapper;

    public ServerOutputDTO performAllocation(WebsiteInputDTO websiteInputDTO) {
        FairDivisionInput fairDivisionInput = fairDivisionInputMapper.toFairDivisionInput(websiteInputDTO);

        FairDivisionAlgorithm algorithm = FairDivisionAlgorithmFactory.getAlgorithm(fairDivisionInput.getAlgorithmId());

        ValuationChecker.setValuationMatrix(fairDivisionInput.getValuationMatrix());

        FairDivisionOutput output = algorithm.allocate(fairDivisionInput.getAgentNumber(), fairDivisionInput.getGoodsNumber(), fairDivisionInput.getValuationMatrix());

        return fairDivisionOutputMapper.toServerOutputDTO(output);
    }
}

