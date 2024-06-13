package com.zig.autopark.service;

import com.zig.autopark.model.Enterprise;
import com.zig.autopark.model.Vehicle;
import com.zig.autopark.repository.VehicleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class VehicleService {
    private final VehicleRepository repository;

    public Page<Vehicle> findAll(Pageable paging) {
        return repository.findAll(paging);
    }

    public List<Vehicle> findAll() {
        return repository.findAll();
    }

    public Page<Vehicle> findAllByEnterprise(Enterprise enterprise, Pageable paging) {
        return repository.findAllByEnterprise(enterprise, paging);
    }

    public Optional<Vehicle> findById(Long id) {
        return repository.findById(id);
    }

    public List<Vehicle> findAllByIds(List<Long> ids) {
        return repository.findAllById(ids);
    }

    public void save(Vehicle vehicle) {
        repository.save(vehicle);
    }

    public void saveAllEntities(List<Vehicle> vehicles) {
        repository.saveAll(vehicles);
    }
}