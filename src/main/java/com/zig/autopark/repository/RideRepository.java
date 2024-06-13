package com.zig.autopark.repository;

import com.zig.autopark.model.Ride;
import com.zig.autopark.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findAllByStartDateTimeIsGreaterThanEqualAndEndDateTimeLessThanEqual(LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Ride> findAllByVehicleAndStartDateTimeIsGreaterThanEqualAndEndDateTimeLessThanEqual(Vehicle vehicle, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Ride> findAllByIdIn(List<Long> rideIds);
}