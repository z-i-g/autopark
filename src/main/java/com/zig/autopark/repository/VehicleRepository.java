package com.zig.autopark.repository;

import com.zig.autopark.model.Enterprise;
import com.zig.autopark.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Page<Vehicle> findAllByEnterprise(Enterprise enterprise, Pageable pageable);
}