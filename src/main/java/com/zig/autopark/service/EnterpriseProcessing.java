
package com.zig.autopark.service;

import com.zig.autopark.dto.rs.EnterpriseDto;
import com.zig.autopark.model.Driver;
import com.zig.autopark.model.Enterprise;
import com.zig.autopark.model.Manager;
import com.zig.autopark.model.Vehicle;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class EnterpriseProcessing {
    private final VehicleService vehicleService;
    private final DriverService driverService;
    private final EnterpriseService enterpriseService;

    public List<EnterpriseDto> findAllAndConvertToDto(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Manager manager = (Manager) MultiUserDetailsService.getAuthUser();
        Page<Enterprise> pagedResult = enterpriseService.findAllByManager(manager, paging);
        if (pagedResult.hasContent()) {
            return pagedResult.stream().map(this::convertToDto).toList();
        }
        return new ArrayList<>();
    }

    public List<EnterpriseDto> findAllAndConvertToDto() {
        Manager manager = (Manager) MultiUserDetailsService.getAuthUser();
        return enterpriseService.findAll(manager).stream().map(this::convertToDto).toList();
    }

    public EnterpriseDto findByIdAndConvertToDto(Long id) {
        return enterpriseService.findById(id).map(this::convertToDto).orElseThrow();
    }

    public Enterprise findById(Long id) {
        return enterpriseService.findById(id).orElseThrow();
    }

    public void convertToDtoAndSave(EnterpriseDto enterpriseDto) {
        enterpriseService.save(convertToEntity(enterpriseDto));
    }

    public void update(EnterpriseDto enterpriseDto, long id) {
        Manager managerDetails = (Manager) MultiUserDetailsService.getAuthUser();

        enterpriseService.findById(id).ifPresent(enterprise -> {
            if (isManagerEnterpriseExists.negate().test(enterprise, managerDetails.getId())) {
                throw new RuntimeException();
            }
            enterprise.setCity(enterpriseDto.getCity());
            enterprise.setName(enterpriseDto.getName());
            enterprise.setStaffNumber(enterpriseDto.getStaffNumber());
            enterprise.setSuperVisorName(enterpriseDto.getSuperVisorName());
            Optional.ofNullable(enterpriseDto.getVehicles()).ifPresent(vehicles -> setVehicles(enterprise, vehicles));
            Optional.ofNullable(enterpriseDto.getDrivers()).ifPresent(drivers -> setDrivers(enterprise, drivers));
            enterpriseService.save(enterprise);
        });
    }

    public void deleteById(long id) {
        Manager managerDetails = (Manager) MultiUserDetailsService.getAuthUser();
        enterpriseService.findById(id).ifPresent(enterprise -> {
            if (isManagerEnterpriseExists.negate().test(enterprise, managerDetails.getId())) {
                throw new RuntimeException();
            }
            enterpriseService.deleteById(id);
        });
    }

    public EnterpriseDto convertToDto(Enterprise enterprise) {
        EnterpriseDto dto = new EnterpriseDto();
        dto.setId(enterprise.getId());
        dto.setCity(enterprise.getCity());
        dto.setName(enterprise.getName());
        dto.setStaffNumber(enterprise.getStaffNumber());
        dto.setSuperVisorName(enterprise.getSuperVisorName());
        dto.setVehicles(enterprise.getVehicles().stream().map(Vehicle::getId).collect(Collectors.toList()));
        dto.setDrivers(enterprise.getDrivers().stream().map(Driver::getId).collect(Collectors.toList()));
        dto.setManagers(enterprise.getManagers().stream().map(Manager::getId).collect(Collectors.toList()));
        return dto;
    }

    public Enterprise convertToEntity(EnterpriseDto enterpriseDto) {
        Enterprise entity = new Enterprise();
        entity.setCity(enterpriseDto.getCity());
        entity.setName(enterpriseDto.getName());
        entity.setStaffNumber(enterpriseDto.getStaffNumber());
        entity.setSuperVisorName(enterpriseDto.getSuperVisorName());
        Optional.ofNullable(enterpriseDto.getVehicles()).ifPresent(vehicles -> setVehicles(entity, vehicles));
        Optional.ofNullable(enterpriseDto.getDrivers()).ifPresent(drivers -> setDrivers(entity, drivers));
        return entity;
    }

    private void setVehicles(Enterprise enterprise, List<Long> vehiclesIds) {
        List<Vehicle> vehicles = vehicleService.findAllByIds(vehiclesIds);
        enterprise.setVehicles(vehicles);
        vehicles.forEach(vehicle -> vehicle.setEnterprise(enterprise));
    }

    private void setDrivers(Enterprise enterprise, List<Long> driverIds) {
        List<Driver> drivers = driverService.findAllByIds(driverIds);
        enterprise.setDrivers(drivers);
        drivers.forEach(vehicle -> vehicle.setEnterprise(enterprise));
    }

    private final BiPredicate<Enterprise, Long> isManagerEnterpriseExists = (enterprise, managerId) ->
            Optional.ofNullable(enterprise)
                    .map(Enterprise::getManagers)
                    .stream()
                    .flatMap(Collection::stream)
                    .map(Manager::getId).anyMatch(manId -> manId.equals(managerId));
}