package com.zig.autopark.dto.rs;

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

    private Long enterpriseId;

    private String purchaseDateTime;

    private List<RideInfoRsDto> rides;
}