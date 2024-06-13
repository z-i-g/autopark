package com.zig.autopark.service;

import com.zig.autopark.dto.rs.DriverDto;
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

    public List<Driver> findAll() {
        return repository.findAll();
    }

    public List<Driver> findAllByIds(List<Long> ids) {
        return repository.findAllById(ids);
    }

    public Driver findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public List<Driver> findAllByVehicle(Vehicle vehicle) {
        return repository.findAllByVehiclesContains(vehicle);
    }

    public List<Driver> findAllFree() {
        return repository.findAllByEnterpriseIsNull();
    }
    public void saveAllEntities(List<Driver> drivers) {
        repository.saveAll(drivers);
    }

    public void save(Driver driver) {
        repository.save(driver);
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