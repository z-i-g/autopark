package com.zig.autopark.repository;

import com.zig.autopark.model.Track;
import com.zig.autopark.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
    List<Track> findAllByVehicleAndCreatedAtBetweenOrderByCreatedAt(Vehicle vehicle, LocalDateTime startTime, LocalDateTime finishTime);

    List<Track> findAllByVehicleAndCreatedAtBetween(Vehicle vehicle, LocalDateTime startDaeTime, LocalDateTime endDatetime);

    List<Track> findByVehicleAndCreatedAtIs(Vehicle vehicle, LocalDateTime startDaeTime);
}