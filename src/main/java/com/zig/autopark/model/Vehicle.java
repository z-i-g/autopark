package com.zig.autopark.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "mileage")
    private String mileage;

    @Column(name = "purchase_date_time")
    private LocalDateTime purchaseDateTime;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "enterprise_id", referencedColumnName = "id")
    private Enterprise enterprise;

    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private List<CarMileageReport> reports;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "driver_vehicle",
            joinColumns = @JoinColumn(name = "vehicle_id"),
            inverseJoinColumns = @JoinColumn(name = "driver_id")
    )
    private List<Driver> drivers;
}