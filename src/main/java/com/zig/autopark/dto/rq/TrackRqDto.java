package com.zig.autopark.dto.rq;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class TrackRqDto {
    private Long vehicleId;
    private LocalDateTime startDateTime;
    private LocalDateTime finishDateTime;
    private RsType rsType;

    public enum RsType {
        JSON,
        GEOJSON;
    }
}