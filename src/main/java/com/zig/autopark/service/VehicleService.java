package com.zig.autopark.service;

import com.zig.autopark.dto.VehicleDto;
import com.zig.autopark.model.Brand;
import com.zig.autopark.model.Vehicle;
import com.zig.autopark.repository.VehicleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class VehicleService {
    private final VehicleRepository repository;
    private final BrandService brandService;
    private final DriverService driverService;

    public List<VehicleDto> findAll() {
        return repository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public VehicleDto findById(Long id) {
        return repository.findById(id).map(this::convertToDto).orElseThrow();
    }

    public void save(VehicleDto vehicleDto) {
        Vehicle vehicle = convertToEntity(vehicleDto);
        repository.save(vehicle);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void update(Long id, VehicleDto vehicleDto) {
        Vehicle vehicle = repository.findById(id).orElseThrow();
        vehicle.setYear(vehicleDto.getYear());
        vehicle.setMileage(vehicleDto.getMileage());
        vehicle.setPrice(vehicleDto.getPrice());
        if (!vehicleDto.getBrand().getId().equals(vehicle.getBrand().getId())) {
            vehicle.setBrand(brandService.findById(vehicleDto.getBrand().getId()));
        }
        repository.save(vehicle);
    }

    public VehicleDto convertToDto(Vehicle vehicle) {
        VehicleDto dto = new VehicleDto();
        dto.setId(vehicle.getId());
        dto.setPrice(vehicle.getPrice());
        dto.setYear(vehicle.getYear());
        dto.setMileage(vehicle.getMileage());
        dto.setBrandName(vehicle.getBrand().getName());
        dto.setBrand(brandService.convertToDto(vehicle.getBrand()));
        dto.setDriverDtoList(vehicle.getDrivers().stream().map(driverService::convertToDto).collect(Collectors.toList()));
        return dto;
    }

    private Vehicle convertToEntity(VehicleDto vehicleDto) {
        Vehicle entity = new Vehicle();
        entity.setPrice(vehicleDto.getPrice());
        entity.setYear(vehicleDto.getYear());
        entity.setMileage(vehicleDto.getMileage());
        entity.setBrand(brandService.findById(vehicleDto.getBrand().getId()));
//        entity.getBrand().setVehicles(entity);
        return entity;
    }

}