package com.zig.autopark.controller;

import com.zig.autopark.dto.rs.DriverDto;
import com.zig.autopark.service.DriverProcessing;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/drivers")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DriverController {
    private final DriverProcessing driverProcessing;

    @ResponseBody
    @GetMapping("/rest")
    public List<DriverDto> showAllJson() {
        return driverProcessing.findAll();
    }

    @GetMapping("/vehicle/{vehicleId}")
    public String showAllByVehicle(@PathVariable("vehicleId") Long vehicleId, Model model) {
        model.addAttribute("drivers", driverProcessing.findAllById(vehicleId));
        model.addAttribute("vehicleId", vehicleId);
        return "driver/index";
    }

    @GetMapping("/add")
    public String addDriver(@ModelAttribute("driver") DriverDto driverDto, Model model, @RequestParam("id") Long vehicleId) {
        model.addAttribute("drivers", driverProcessing.findAllFree());
        model.addAttribute("vehicleId", vehicleId);
        return "driver/add";
    }

    @PatchMapping("/save")
    public String addDriver(@ModelAttribute("driver") DriverDto addedDriver, @RequestParam("id") Long vehicleId) {
        driverProcessing.addVehicleAnsSave(addedDriver.getId(), vehicleId);
        return "driver/add";
    }
}