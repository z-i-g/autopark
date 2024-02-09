package com.zig.autopark.service;

import com.zig.autopark.dto.BrandDto;
import com.zig.autopark.model.Brand;
import com.zig.autopark.repository.BrandRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class BrandService {
    private final BrandRepository repository;

    public List<BrandDto> findAll() {
        return repository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public Brand findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public BrandDto convertToDto(Brand brand) {
        BrandDto dto = new BrandDto();
        dto.setId(brand.getId());
        dto.setName(brand.getName());
        dto.setLoadCapacity(brand.getLoadCapacity());
        dto.setFuelTankCapacity(brand.getFuelTankCapacity());
        dto.setFuelTankCapacity(brand.getFuelTankCapacity());
        dto.setSeatsNumber(brand.getSeatsNumber());
        dto.setVehicleType(brand.getVehicleType());
        return dto;
    }

    public Brand convertToEntity(BrandDto brandDto) {
        Brand entity = new Brand();
        entity.setName(brandDto.getName());
        entity.setLoadCapacity(brandDto.getLoadCapacity());
        entity.setFuelTankCapacity(brandDto.getFuelTankCapacity());
        entity.setFuelTankCapacity(brandDto.getFuelTankCapacity());
        entity.setSeatsNumber(brandDto.getSeatsNumber());
        entity.setVehicleType(brandDto.getVehicleType());
        return entity;
    }
}