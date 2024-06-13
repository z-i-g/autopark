package com.zig.autopark.service;

import com.zig.autopark.dto.rs.VehicleDto;
import com.zig.autopark.model.Enterprise;
import com.zig.autopark.model.Manager;
import com.zig.autopark.model.Vehicle;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class VehicleProcessing {
    private final VehicleService vehicleService;
    private final EnterpriseService enterpriseService;
    private final BrandService brandService;
    private final DriverService driverService;

    public List<VehicleDto> findAll(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Manager userDetails = (Manager) MultiUserDetailsService.getAuthUser();
        Page<Vehicle> pagedResult = vehicleService.findAll(paging);
        if (pagedResult.hasContent()) {
            List<VehicleDto> dtos = pagedResult.stream().filter(vehicle -> isManagerVehicleExists.test(vehicle, userDetails.getId()))
                    .map(this::convertToDto).collect(Collectors.toList());
            return dtos;
        }
        return new ArrayList<>();
    }

    public List<VehicleDto> findAll() {
        Manager userDetails = (Manager) MultiUserDetailsService.getAuthUser();
        List<Vehicle> vehicles = vehicleService.findAll();

        Integer num = 100;
        for (int i = 0; i < vehicles.size(); i++) {
            vehicles.get(i).setRegistrationNumber(num.toString());
            num++;
        }

        vehicleService.saveAllEntities(vehicles);

        List<VehicleDto> dtos = vehicleService.findAll().stream().filter(vehicle -> isManagerVehicleExists.negate().test(vehicle, userDetails.getId()))
                .map(this::convertToDto).collect(Collectors.toList());
        return dtos;
    }

    public List<VehicleDto> findAllByEnterprise(Long enterpriseId, int pageNo, int pageSize) {
        Enterprise enterprise = enterpriseService.findById(enterpriseId).get();
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Vehicle> pagedResult = vehicleService.findAllByEnterprise(enterprise, paging);
        if (pagedResult.hasContent()) {
            return pagedResult.stream().map(this::convertToDto).toList();
        }
        return new ArrayList<>();
    }

    public VehicleDto findByIdAndConvertToDto(Long id) {
        return vehicleService.findById(id).map(this::convertToDto).orElseThrow();
    }

    public void save(VehicleDto vehicleDto) {
        Vehicle vehicle = convertToEntity(vehicleDto);
        vehicleService.save(vehicle);
    }

    public void deleteById(Long id) {
        Manager managerDetails = (Manager) MultiUserDetailsService.getAuthUser();
        vehicleService.findById(id).ifPresent(vehicle -> {
            if (isManagerVehicleExists.negate().test(vehicle, managerDetails.getId()))
                throw new RuntimeException();
            vehicleService.findById(id).ifPresent(val -> {
                vehicle.setEnterprise(null);
                vehicleService.save(val);
            });
        });
    }

    public void update(Long id, VehicleDto vehicleDto) {
        Manager managerDetails = (Manager) MultiUserDetailsService.getAuthUser();
        vehicleService.findById(id).ifPresent(vehicle -> {
            if (isManagerVehicleExists.negate().test(vehicle, managerDetails.getId()))
                throw new BadCredentialsException("");
            vehicle.setYear(vehicleDto.getYear());
            vehicle.setMileage(vehicleDto.getMileage());
            vehicle.setPrice(vehicleDto.getPrice());
            if (!vehicleDto.getBrand().getId().equals(vehicle.getBrand().getId())) {
                vehicle.setBrand(brandService.findById(vehicleDto.getBrand().getId()));
            }
            if (!vehicleDto.getEnterpriseId().equals(vehicle.getEnterprise().getId())) {
                Enterprise enterprise = enterpriseService.findById(vehicleDto.getEnterpriseId()).get();
                vehicle.setEnterprise(enterprise);
                Optional.ofNullable(enterprise.getVehicles()).orElse(new ArrayList<>()).add(vehicle);
            }
            vehicleService.save(vehicle);
        });
    }

    public VehicleDto convertToDto(Vehicle vehicle) {
        VehicleDto dto = new VehicleDto();
        dto.setId(vehicle.getId());
        dto.setPrice(vehicle.getPrice());
        dto.setYear(vehicle.getYear());
        dto.setMileage(vehicle.getMileage());
        dto.setBrandName(vehicle.getBrand().getName());
        Manager manager = (Manager) MultiUserDetailsService.getAuthUser();
        dto.setPurchaseDateTime(manager.getFormattedDateTime(vehicle.getPurchaseDateTime()));
        dto.setBrand(brandService.convertToDto(vehicle.getBrand()));
        if (vehicle.getDrivers() != null)
            dto.setDriverDtoList(vehicle.getDrivers().stream().map(driverService::convertToDto).collect(Collectors.toList()));
        if (vehicle.getEnterprise() != null)
            dto.setEnterpriseId(vehicle.getEnterprise().getId());
        return dto;
    }

    private Vehicle convertToEntity(VehicleDto vehicleDto) {
        Vehicle entity = new Vehicle();
        entity.setPrice(vehicleDto.getPrice());
        entity.setYear(vehicleDto.getYear());
        entity.setMileage(vehicleDto.getMileage());
        LocalDateTime localDateTime = LocalDateTime.now();
        entity.setPurchaseDateTime(localDateTime);
        entity.setBrand(brandService.findById(vehicleDto.getBrand().getId()));
        entity.setEnterprise(enterpriseService.findById(vehicleDto.getEnterpriseId()).get());
        return entity;
    }

    private final BiPredicate<Vehicle, Long> isManagerVehicleExists = (vehicle, managerId) ->
            Optional.ofNullable(vehicle.getEnterprise())
                    .map(Enterprise::getManagers)
                    .stream()
                    .flatMap(Collection::stream)
                    .map(Manager::getId).anyMatch(manId -> manId.equals(managerId));
}