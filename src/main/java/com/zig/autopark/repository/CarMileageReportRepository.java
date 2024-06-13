package com.zig.autopark.repository;

import com.zig.autopark.model.CarMileageReport;
import com.zig.autopark.model.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CarMileageReportRepository extends JpaRepository<CarMileageReport, Long> {
    List<CarMileageReport> findByVehicleIdAndStartDateEqualsAndAndEndDateEqualsAndPeriodEquals(Long vehicleId, LocalDate startDate, LocalDate endDate, Period period);
}