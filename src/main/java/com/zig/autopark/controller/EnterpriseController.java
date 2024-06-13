package com.zig.autopark.controller;

import com.zig.autopark.dto.rs.EnterpriseDto;
import com.zig.autopark.service.EnterpriseProcessing;
import com.zig.autopark.service.VehicleProcessing;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/enterprises")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@PreAuthorize("hasRole('MANAGER')")
public class EnterpriseController {
    private final EnterpriseProcessing enterpriseProcessing;
    private final VehicleProcessing vehicleProcessing;

    @ResponseBody
    @GetMapping("/rest")
    public List<EnterpriseDto> showAllJson(@RequestParam(defaultValue = "0") Integer pageNo,
                                           @RequestParam(defaultValue = "10") Integer pageSize) {
        return enterpriseProcessing.findAllAndConvertToDto(pageNo, pageSize);
    }

    @PutMapping("/rest/add")
    public void add(@Valid @RequestBody EnterpriseDto enterpriseDto) {
        enterpriseProcessing.convertToDtoAndSave(enterpriseDto);
    }

    @PostMapping("/rest/update/{id}")
    @ResponseBody
    public void update(@Valid @RequestBody EnterpriseDto enterpriseDto, @PathVariable long id) {
        enterpriseProcessing.update(enterpriseDto, id);
    }

    @DeleteMapping("/rest/delete/{id}")
    public void delete(@PathVariable long id) {
        enterpriseProcessing.deleteById(id);
    }

    //view2
    @GetMapping
    public String showAll(Model model,
                          @RequestParam(defaultValue = "0") Integer pageNo,
                          @RequestParam(defaultValue = "10") Integer pageSize) {
        model.addAttribute("enterprises", enterpriseProcessing.findAllAndConvertToDto(pageNo, pageSize));
        return "enterprise/show";
    }

    @GetMapping("/details/{enterpriseId}")
    public String getAll(@PathVariable("enterpriseId") Long enterpriseId, Model model) {
        model.addAttribute("enterprise", enterpriseProcessing.findByIdAndConvertToDto(enterpriseId));
        return "enterprise/details";
    }
}