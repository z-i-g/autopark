package com.zig.autopark.dto.rs;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
public class DriverDto {
    private Long id;

    private String name;

    private BigDecimal salary;

    private Integer age;

    private Integer experience;

    private Long enterpriseId;

    private Boolean active;

    private Long activeInVehicle;

    private List<Long> vehicleIds;
}