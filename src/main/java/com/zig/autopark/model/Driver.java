package com.zig.autopark.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "driver")
@Getter
@Setter
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "salary")
    private BigDecimal salary;

    @Column(name = "age")
    private Integer age;

    @Column(name = "experience")
    private Integer experience;

    @OneToOne
    @JoinColumn(name = "active_in_vehicle_id")
    private Vehicle activeInVehicle;

    @ManyToOne
    @JoinColumn(name = "enterprise_id", referencedColumnName = "id")
    private Enterprise enterprise;

    @ManyToMany(mappedBy = "drivers")
    private List<Vehicle> vehicles;
}