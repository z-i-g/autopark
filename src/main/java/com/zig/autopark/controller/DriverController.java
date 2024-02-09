package com.zig.autopark.controller;

import com.zig.autopark.dto.DriverDto;
import com.zig.autopark.service.DriverService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/drivers")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DriverController {
    private final DriverService driverService;

    @ResponseBody
    @GetMapping("/rest")
    public List<DriverDto> showAllJson() {
        return driverService.findAll();
    }
}