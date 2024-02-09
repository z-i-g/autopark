package com.zig.autopark.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter @Setter
public class EnterpriseDto {
    private Long id;

    private String name;

    private String city;

    private Integer staffNumber;

    private String superVisorName;

    private List<Long> vehicles;

    private List<Long> drivers;
}