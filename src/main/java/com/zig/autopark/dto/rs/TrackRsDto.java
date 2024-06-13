package com.zig.autopark.dto.rs;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TrackRsDto extends AbstractTrackDto {
    private Long id;

    private String dateTime;

    private String point;
}