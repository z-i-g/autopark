package com.zig.autopark.controller;

import com.zig.autopark.dto.rs.BrandDto;
import com.zig.autopark.service.BrandService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/brands")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class BrandController {
    private final BrandService brandService;

    @GetMapping
    public String showAll(Model model) {
        model.addAttribute("brands", brandService.findAll());
        return "brand/index";
    }

    @ResponseBody
    @GetMapping("/rest")
    public List<BrandDto> showAllJson() {
        return brandService.findAll();
    }
}