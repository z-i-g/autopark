package com.zig.autopark.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "enterprise")
@Getter
@Setter
public class Enterprise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    @Column(name = "staff_number")
    private Integer staffNumber;

    @Column(name = "super_visor_name")
    private String superVisorName;

    @OneToMany(mappedBy = "enterprise")
    private List<Vehicle> vehicles;

    @OneToMany(mappedBy = "enterprise")
    private List<Driver> drivers;
}