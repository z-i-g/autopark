package com.zig.autopark.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Ride {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date_time ")
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time ")
    private LocalDateTime endDateTime;

    @Column(name = "distance")
    private Long distance;

    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
}