package com.zig.autopark.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
public class VehicleDto {
    private Long id;

    @NotNull(message = "Price should not be empty")
    private BigDecimal price;

    @NotEmpty(message = "Year should not be empty")
    private String year;

    @NotEmpty(message = "Mileage should not be empty")
    private String mileage;

    private String brandName;

    private BrandDto brand;

    private List<DriverDto> driverDtoList;
}

//@NotEmpty(message = "Name should not be empty")
//@Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
//private String name;
//
//@Min(value = 0, message = "Age should be greater than 0")
//private int age;
//
//@NotEmpty(message = "Email should not be empty")
//@Email(message = "Email should be valid")
//private String email;