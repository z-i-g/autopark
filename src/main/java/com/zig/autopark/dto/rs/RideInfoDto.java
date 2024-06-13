package com.zig.autopark.dto.rs;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RideInfoDto {
    private Long id;
    private Long vehicleId;
    private String startDateTime;
}