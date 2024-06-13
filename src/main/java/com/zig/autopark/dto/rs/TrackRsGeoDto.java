package com.zig.autopark.dto.rs;

import lombok.Getter;
import lombok.Setter;

import org.locationtech.jts.geom.Point;

@Getter @Setter
public class TrackRsGeoDto extends AbstractTrackDto {
    private String dateTime;
    private Point point;
}