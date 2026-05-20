package com.bus_ticket_pro.project_bus_ticket_pro.entity;

import com.bus_ticket_pro.project_bus_ticket_pro.entity.base.BaseEntity;
import com.bus_ticket_pro.project_bus_ticket_pro.enums.BusType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "buses")
@Getter
@Setter
public class Bus extends BaseEntity {

    @Column(name = "plate_number", nullable = false, unique = true, length = 30)
    private String plateNumber;

    @Enumerated(EnumType.STRING)
    private BusType busType;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @Column(name = "company_name", length = 100)
    private String companyName;

    @Column(name = "driver_name", length = 100)
    private String driverName;

    @Column(nullable = false)
    private Boolean active = true;
}
