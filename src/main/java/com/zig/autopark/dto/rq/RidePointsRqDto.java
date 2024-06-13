package com.zig.autopark.dto.rq;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class RidePointsRqDto {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}