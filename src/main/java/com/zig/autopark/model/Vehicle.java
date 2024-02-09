package com.zig.autopark.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "vehicle")
@Getter @Setter
public class Vehicle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "year")
    private String year;

    @Column(name = "mileage")
    private String mileage;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "enterprise_id", referencedColumnName = "id")
    private Enterprise enterprise;

    @ManyToMany
    @JoinTable(
            name = "driver_vehicle",
            joinColumns = @JoinColumn(name = "vehicle_id"),
            inverseJoinColumns = @JoinColumn(name = "driver_id")
    )
    private List<Driver> drivers;
}