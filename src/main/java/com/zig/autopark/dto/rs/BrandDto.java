package com.zig.autopark.dto.rs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zig.autopark.model.VehicleType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class BrandDto {
    private Long id;

    @JsonIgnore
    private String name;

    @JsonIgnore
    private Double loadCapacity;

    @JsonIgnore
    private Double fuelTankCapacity;

    @JsonIgnore
    private Integer seatsNumber;

    @JsonIgnore
    private VehicleType vehicleType;
}