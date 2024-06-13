package com.zig.autopark.controller;

import com.zig.autopark.dto.rq.RidePointsRqDto;
import com.zig.autopark.dto.rs.RideInfoRsDto;
import com.zig.autopark.dto.rs.RidePointsRsDto;
import com.zig.autopark.service.RideService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@PreAuthorize("hasRole('MANAGER')")
public class RideController {
    private final RideService rideService;

    @PostMapping("/rides/rest/points")
    @ResponseBody
    public List<RidePointsRsDto> showAllRidePointsByDateTime(@RequestBody RidePointsRqDto rqDto) {
        return rideService.findAllRidePointsBetweenDateTime(rqDto);
    }

    @PostMapping("/rides/rest/info")
    @ResponseBody
    public List<RideInfoRsDto> showAllRidesInfoByDateTime(@RequestBody RidePointsRqDto rqDto) {
        return rideService.findAllRidesBetweenDateTime(rqDto);
    }

    //view
    @PostMapping("/rides")
    public String showRidesBetweenDate(@RequestParam Long vehicleId, @RequestParam String fromDate, @RequestParam String toDate, Model model) {
        model.addAttribute("rides", rideService.findAllRidesByVehicleAndBetweenDateTimeAndConvertToDto(vehicleId, fromDate, toDate));
        return "ride/show";
    }

    @PostMapping("/rides/route")
    public String showRidesBetweenDate(@RequestParam("selectedRides") List<Long> selectedRides, Model model) {
        model.addAttribute("routes", rideService.findAllRidesById(selectedRides));
        return "ride/route";
    }
}