package com.zig.autopark.controller;

import com.zig.autopark.dto.rs.VehicleDto;
import com.zig.autopark.service.VehicleProcessing;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles/rest")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@PreAuthorize("hasRole('MANAGER')")
public class VehicleRestController {
    private final VehicleProcessing vehicleProcessing;

    //rest
    @GetMapping
    public List<VehicleDto> showAllJson(@RequestParam(defaultValue = "0") Integer pageNo,
                                        @RequestParam(defaultValue = "10") Integer pageSize) {
        return vehicleProcessing.findAll(pageNo, pageSize);
    }

    @PutMapping("/add")
    public void add(@Valid @RequestBody VehicleDto vehicleDto) {
        vehicleProcessing.save(vehicleDto);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/update/{id}")
    public void update(@Valid @RequestBody VehicleDto vehicleDto, @PathVariable long id) {
        vehicleProcessing.update(id, vehicleDto);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id) {
        vehicleProcessing.deleteById(id);
    }

    @GetMapping("gen")
    public List<VehicleDto> fillNumber() {
        return vehicleProcessing.findAll();
    }
}