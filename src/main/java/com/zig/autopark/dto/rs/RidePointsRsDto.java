package com.zig.autopark.dto.rs;

import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Getter @Setter
public class RidePointsRsDto {
    private String dateTime;
    private Long vehicleId;
    private Point point;
}