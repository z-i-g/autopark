package com.zig.autopark.controller;

import com.zig.autopark.dto.VehicleDto;
import com.zig.autopark.service.BrandService;
import com.zig.autopark.service.VehicleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/vehicles")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class VehicleController {
    private final VehicleService vehicleService;
    private final BrandService brandService;

    @GetMapping
    public String showAll(Model model) {
        model.addAttribute("vehicles", vehicleService.findAll());
        return "vehicle/index";
    }

    @GetMapping("/rest")
    @ResponseBody
    public List<VehicleDto> showAllJson() {
        return vehicleService.findAll();
    }

    @GetMapping("/new")
    public String newVehicle(@ModelAttribute("vehicle") VehicleDto vehicleDto, Model model) {
        model.addAttribute("brands", brandService.findAll());
        return "vehicle/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("vehicle") @Valid VehicleDto vehicle,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "vehicle/new";

        vehicleService.save(vehicle);
        return "redirect:/vehicles";
    }

    @GetMapping("/delete")
    public String deleteVehicle(@RequestParam long id) {
        vehicleService.deleteById(id);
        return "redirect:/vehicles";
    }

    @PatchMapping("/edit")
    public String update(@ModelAttribute("vehicle") VehicleDto vehicleDto, BindingResult bindingResult,
                         @RequestParam("id") long id) {
        if (bindingResult.hasErrors())
            return "vehicles/edit";

        vehicleService.update(id, vehicleDto);
        return "redirect:/vehicles";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        model.addAttribute("vehicle", vehicleService.findById(id));
        model.addAttribute("brands", brandService.findAll());
        return "vehicle/edit";
    }
}