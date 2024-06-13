package com.zig.autopark.controller;

import com.zig.autopark.dto.rs.VehicleDto;
import com.zig.autopark.service.BrandService;
import com.zig.autopark.service.EnterpriseProcessing;
import com.zig.autopark.service.VehicleProcessing;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@PreAuthorize("hasRole('MANAGER')")
public class VehicleController {
    private final VehicleProcessing vehicleProcessing;
    private final BrandService brandService;
    private final EnterpriseProcessing enterpriseProcessing;

    @GetMapping("/vehicles")
    public String showAll(Model model) {
        model.addAttribute("vehicles", vehicleProcessing.findAll());
        return "vehicle/show";
    }

    @GetMapping("/enterprise/vehicles")
    public String getVehiclesByEnterprise(@RequestParam("enterpriseId") Long enterpriseId,Model model,
                                          @RequestParam(defaultValue = "0") Integer pageNo,
                                          @RequestParam(defaultValue = "10") Integer pageSize) {
        model.addAttribute("vehicles", vehicleProcessing.findAllByEnterprise(enterpriseId, pageNo, pageSize));
        return "vehicle/show";
    }
    @GetMapping("vehicles/details/{vehicleId}")
    public String getAll(@PathVariable("vehicleId") Long vehicleId, Model model) {
        model.addAttribute("vehicle", vehicleProcessing.findByIdAndConvertToDto(vehicleId));
        return "vehicle/details";
    }

    @GetMapping("vehicles/new")
    public String newVehicle(@ModelAttribute("vehicle") VehicleDto vehicleDto, Model model) {
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("enterprises", enterpriseProcessing.findAllAndConvertToDto());
        return "vehicle/new";
    }

    @PostMapping("vehicles")
    public String add(@ModelAttribute("vehicle") @Valid VehicleDto vehicle,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "vehicle/new";

        vehicleProcessing.save(vehicle);
        return "redirect:/vehicles";
    }

    @GetMapping("vehicles/delete/{vehicleId}")
    public String deleteVehicle(@PathVariable("vehicleId") long id) {
        vehicleProcessing.deleteById(id);
        return "redirect:/vehicles";
    }

    @PatchMapping("vehicles/edit")
    public String update(@ModelAttribute("vehicle") VehicleDto vehicleDto, BindingResult bindingResult,
                         @RequestParam("id") long id) {
        if (bindingResult.hasErrors())
            return "vehicles/edit";

        vehicleProcessing.update(id, vehicleDto);
        return "redirect:/vehicles/enterprise";
    }

    @GetMapping("vehicles/{vehicleId}/edit")
    public String edit(Model model, @PathVariable("vehicleId") long id) {
        model.addAttribute("vehicle", vehicleProcessing.findByIdAndConvertToDto(id));
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("enterprises", enterpriseProcessing.findAllAndConvertToDto());
        return "vehicle/edit";
    }
}