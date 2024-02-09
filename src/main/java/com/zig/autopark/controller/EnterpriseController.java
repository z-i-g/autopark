package com.zig.autopark.controller;

import com.zig.autopark.dto.EnterpriseDto;
import com.zig.autopark.service.EnterpriseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/enterprises")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class EnterpriseController {
    private final EnterpriseService enterpriseService;

    @ResponseBody
    @GetMapping("/rest")
    public List<EnterpriseDto> showAllJson() {
        return enterpriseService.findAll();
    }
}