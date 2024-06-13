package com.zig.autopark.dto.rq;

import com.zig.autopark.model.Period;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReportDto {
    private String name;
    private String vehicleRegistrationNumber;
    private Period period;
    private String mileage;
}