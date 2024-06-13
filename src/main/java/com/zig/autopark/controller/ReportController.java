package com.zig.autopark.controller;

import com.zig.autopark.dto.ReportCreateDto;
import com.zig.autopark.service.ReportService;
import com.zig.autopark.service.VehicleProcessing;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@PreAuthorize("hasRole('MANAGER')")
public class ReportController {

    private final ReportService reportService;
    private final VehicleProcessing vehicleProcessing;

    //view
    @GetMapping("/reports")
    public String create(@ModelAttribute("report") ReportCreateDto reportCreateDto, Model model) {
        model.addAttribute("vehicles", vehicleProcessing.findAll());
        return "report/new";
    }

    @PostMapping("/reports/gen")
    public String showAll(@ModelAttribute("report") ReportCreateDto reportCreateDto, Model model) {
        model.addAttribute("reports", reportService.getOrGenerateAndSave(reportCreateDto));
        return "report/details";
    }
}