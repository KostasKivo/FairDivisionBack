package com.kivotos.fairdivision.mapper;

import com.kivotos.fairdivision.dto.ServerOutputDTO;
import com.kivotos.fairdivision.model.FairDivisionOutput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FairDivisionOutputMapper {
    FairDivisionOutputMapper INSTANCE = Mappers.getMapper(FairDivisionOutputMapper.class);

    @Mapping(source = "allocations", target = "allocations")
    ServerOutputDTO toServerOutputDTO(FairDivisionOutput fairDivisionOutput);

    @Mapping(source = "allocations", target = "allocations")
    FairDivisionOutput toFairDivisionOutput(ServerOutputDTO serverOutputDTO);
}



