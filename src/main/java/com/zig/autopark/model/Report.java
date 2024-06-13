package com.zig.autopark.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Setter @Getter
@Inheritance(strategy = InheritanceType.JOINED)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private String result;

    @Enumerated(EnumType.STRING)
    @Column(name = "period")
    private Period period;
}