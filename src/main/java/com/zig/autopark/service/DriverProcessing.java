package com.zig.autopark.service;

import com.zig.autopark.dto.rs.DriverDto;
import com.zig.autopark.model.Driver;
import com.zig.autopark.model.Enterprise;
import com.zig.autopark.model.Vehicle;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DriverProcessing {
    private final DriverService driverService;
    private final VehicleService vehicleService;
    private final EnterpriseService enterpriseService;

    public List<DriverDto> findAll() {
        return driverService.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<DriverDto> findAllFree() {
        return driverService.findAllFree().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<DriverDto> findAllById(Long id) {
        Vehicle vehicle = vehicleService.findById(id).orElseThrow();
        return driverService.findAllByVehicle(vehicle).stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public void addVehicleAnsSave(Long id, Long vehicleId) {
        Vehicle vehicle = vehicleService.findById(vehicleId).orElseThrow();
        Driver driver = driverService.findById(id);
        Enterprise enterprise = vehicle.getEnterprise();
        driver.setEnterprise(enterprise);
        Optional.ofNullable(enterprise.getDrivers()).orElse(new ArrayList<>()).add(driver);
        Optional.ofNullable(driver.getVehicles()).orElse(new ArrayList<>()).add(vehicle);
        Optional.ofNullable(vehicle.getDrivers()).orElse(new ArrayList<>()).add(driver);
        driverService.save(driver);
        vehicleService.save(vehicle);
        enterpriseService.save(enterprise);
    }

    public DriverDto convertToDto(Driver driver) {
        DriverDto dto = new DriverDto();
        dto.setId(driver.getId());
        dto.setName(driver.getName());
        dto.setSalary(driver.getSalary());
        dto.setAge(driver.getAge());
        dto.setExperience(driver.getExperience());
        dto.setActive(driver.getActiveInVehicle() != null);
        dto.setActiveInVehicle(Optional.ofNullable(driver.getActiveInVehicle()).map(Vehicle::getId).orElse(null));
        dto.setEnterpriseId(Optional.ofNullable(driver.getEnterprise()).map(Enterprise::getId).orElse(null));
        dto.setVehicleIds(driver.getVehicles().stream().map(Vehicle::getId).collect(Collectors.toList()));
        return dto;
    }
}