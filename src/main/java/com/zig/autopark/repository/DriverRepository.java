package com.zig.autopark.repository;

import com.zig.autopark.model.Driver;
import com.zig.autopark.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    List<Driver> findAllByVehiclesContains(Vehicle vehicle);

    List<Driver> findAllByEnterpriseIsNull();
}