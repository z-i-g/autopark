package com.zig.autopark.controller;

import com.zig.autopark.dto.rq.TrackRqDto;
import com.zig.autopark.dto.rs.AbstractTrackDto;
import com.zig.autopark.service.TrackService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tracks")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@PreAuthorize("hasRole('MANAGER')")
public class TrackRestController {
    private final TrackService trackService;

    @PostMapping("/rest")
    public List<AbstractTrackDto> showAllByVehicle(@RequestBody TrackRqDto rqDto) {
        return trackService.findAllGeoByVehicleAndTimeBetween(rqDto);
    }
}