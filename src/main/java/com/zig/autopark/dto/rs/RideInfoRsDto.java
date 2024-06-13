package com.zig.autopark.dto.rs;

import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Getter @Setter
public class RideInfoRsDto {
    private Long vehicleId;
    private Long duration;
    private Long distance;
    private Point startPoint;
    private String startDateTime;
    private String startAddress;
    private Point endPoint;
    private String endDateTime;
    private String endAddress;
}