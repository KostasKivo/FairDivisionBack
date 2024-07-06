package com.kivotos.fairdivision.mapper;

import com.kivotos.fairdivision.dto.ServerOutputDTO;
import com.kivotos.fairdivision.model.FairDivisionOutput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FairDivisionOutputMapper {

    @Mapping(source = "allocations", target = "allocations")
    ServerOutputDTO toServerOutputDTO(FairDivisionOutput fairDivisionOutput);
}



