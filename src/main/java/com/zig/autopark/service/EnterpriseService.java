package com.zig.autopark.service;

import com.zig.autopark.dto.EnterpriseDto;
import com.zig.autopark.model.Brand;
import com.zig.autopark.model.Driver;
import com.zig.autopark.model.Enterprise;
import com.zig.autopark.model.Vehicle;
import com.zig.autopark.repository.EnterpriseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class EnterpriseService {
    private final EnterpriseRepository repository;

    public List<EnterpriseDto> findAll() {
        return repository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public EnterpriseDto convertToDto(Enterprise enterprise) {
        EnterpriseDto dto = new EnterpriseDto();
        dto.setId(enterprise.getId());
        dto.setCity(enterprise.getCity());
        dto.setName(enterprise.getName());
        dto.setStaffNumber(enterprise.getStaffNumber());
        dto.setSuperVisorName(enterprise.getSuperVisorName());
        dto.setVehicles(enterprise.getVehicles().stream().map(Vehicle::getBrand).map(Brand::getId).collect(Collectors.toList()));
        dto.setDrivers(enterprise.getDrivers().stream().map(Driver::getId).collect(Collectors.toList()));
        return dto;
    }
}