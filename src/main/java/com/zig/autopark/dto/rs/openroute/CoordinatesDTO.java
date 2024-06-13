package com.zig.autopark.dto.rs.openroute;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CoordinatesDTO {
    private List<List<Double>> coordinates;
}