package com.kivotos.fairdivision.service;

import com.kivotos.fairdivision.algorithms.FairDivisionAlgorithm;
import com.kivotos.fairdivision.algorithms.factory.FairDivisionAlgorithmFactory;
import com.kivotos.fairdivision.dto.ServerOutputDTO;
import com.kivotos.fairdivision.mapper.FairDivisionOutputMapper;
import com.kivotos.fairdivision.model.FairDivisionOutput;
import org.springframework.stereotype.Service;

@Service
public class FairDivisionService {

    public ServerOutputDTO performAllocation(int algorithmId, int agents, int goods, int[][] valuationMatrix) {
        FairDivisionAlgorithm algorithm = FairDivisionAlgorithmFactory.getAlgorithm(algorithmId);

        FairDivisionOutput output = algorithm.allocate(agents, goods, valuationMatrix);

        return FairDivisionOutputMapper.INSTANCE.toServerOutputDTO(output);
    }
}

