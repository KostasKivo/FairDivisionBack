package com.kivotos.fairdivision.mapper;

import com.kivotos.fairdivision.dto.ServerOutputDTO;
import com.kivotos.fairdivision.model.Allocation;
import com.kivotos.fairdivision.model.FairDivisionOutput;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FairDivisionOutputMapperTest {

    private final FairDivisionOutputMapper mapper = Mappers.getMapper(FairDivisionOutputMapper.class);

    @Test
    public void testTransformAllocations() {
        List<Allocation> allocations = new ArrayList<>();

        // Add test data with extreme values
        Allocation allocation1 = new Allocation(1);
        allocation1.setHighestValuedGood(Integer.MIN_VALUE);
        allocation1.setLowestValuedGood(Integer.MAX_VALUE);

        Allocation allocation2 = new Allocation(2);
        allocation2.setHighestValuedGood(10);
        allocation2.setLowestValuedGood(5);

        allocations.add(allocation1);
        allocations.add(allocation2);

        List<Allocation> transformedAllocations = FairDivisionOutputMapper.transformAllocations(allocations);

        assertEquals(2, transformedAllocations.size());

        Allocation transformed1 = transformedAllocations.get(0);
        Allocation transformed2 = transformedAllocations.get(1);

        // Verify that extreme values are handled correctly
        assertEquals(0, transformed1.getHighestValuedGood());
        assertEquals(0, transformed1.getLowestValuedGood());

        // Verify that other values are unchanged
        assertEquals(10, transformed2.getHighestValuedGood());
        assertEquals(5, transformed2.getLowestValuedGood());
    }

    @Test
    public void testToServerOutputDTO() {
        List<Allocation> allocations = new ArrayList<>();

        Allocation allocation1 = new Allocation(1);
        allocation1.setHighestValuedGood(Integer.MIN_VALUE);
        allocation1.setLowestValuedGood(Integer.MAX_VALUE);

        Allocation allocation2 = new Allocation(2);
        allocation2.setHighestValuedGood(10);
        allocation2.setLowestValuedGood(5);

        allocations.add(allocation1);
        allocations.add(allocation2);

        FairDivisionOutput fairDivisionOutput = new FairDivisionOutput(allocations, new int[2][2]);

        ServerOutputDTO serverOutputDTO = mapper.toServerOutputDTO(fairDivisionOutput);

        assertTrue(serverOutputDTO.getAllocations().size() == 2);

        Allocation dtoAllocation1 = serverOutputDTO.getAllocations().get(0);
        Allocation dtoAllocation2 = serverOutputDTO.getAllocations().get(1);

        // Verify that extreme values are handled correctly in the DTO
        assertEquals(0, dtoAllocation1.getHighestValuedGood());
        assertEquals(0, dtoAllocation1.getLowestValuedGood());

        // Verify that other values are unchanged
        assertEquals(10, dtoAllocation2.getHighestValuedGood());
        assertEquals(5, dtoAllocation2.getLowestValuedGood());
    }
}
