package com.zig.autopark.service;

import com.zig.autopark.dto.DriverDto;
import com.zig.autopark.model.Driver;
import com.zig.autopark.model.Enterprise;
import com.zig.autopark.model.Vehicle;
import com.zig.autopark.repository.DriverRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DriverService {
    private final DriverRepository repository;
    private final EnterpriseService enterpriseService;

    public List<DriverDto> findAll() {
        return repository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public DriverDto convertToDto(Driver driver) {
        DriverDto dto = new DriverDto();
        dto.setId(driver.getId());
        dto.setName(driver.getName());
        dto.setSalary(driver.getSalary());
        dto.setAge(driver.getAge());
        dto.setExperience(driver.getExperience());
//        dto.setActiveInVehicle(driver.getActive());
        dto.setEnterpriseId(Optional.ofNullable(driver.getEnterprise()).map(Enterprise::getId).orElse(null));
        dto.setVehicleIds(driver.getVehicles().stream().map(Vehicle::getId).collect(Collectors.toList()));
        return dto;
    }
}