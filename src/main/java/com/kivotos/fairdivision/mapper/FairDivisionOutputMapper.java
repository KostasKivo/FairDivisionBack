package com.kivotos.fairdivision.mapper;

import com.kivotos.fairdivision.dto.ServerOutputDTO;
import com.kivotos.fairdivision.model.Allocation;
import com.kivotos.fairdivision.model.FairDivisionOutput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FairDivisionOutputMapper {

    @Mapping(source = "allocations", target = "allocations", qualifiedByName = "transformAllocations")
    ServerOutputDTO toServerOutputDTO(FairDivisionOutput fairDivisionOutput);

    @Named("transformAllocations")
    static List<Allocation> transformAllocations(List<Allocation> allocations) {

        for(Allocation allocation: allocations) {
            if(allocation.getHighestValuedGood() == Integer.MIN_VALUE) allocation.setHighestValuedGood(0);

            if(allocation.getLowestValuedGood() == Integer.MAX_VALUE) allocation.setLowestValuedGood(0);
        }


        return allocations;
    }
}



