package com.zig.autopark.dto;

import com.zig.autopark.model.Period;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class ReportCreateDto {
    private Long vehicleId;
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private Period period;
}