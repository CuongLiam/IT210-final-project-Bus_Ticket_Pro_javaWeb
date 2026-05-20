package com.bus_ticket_pro.project_bus_ticket_pro.entity;

import com.bus_ticket_pro.project_bus_ticket_pro.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trips")
@Getter
@Setter
public class Trip extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "routes_id", nullable = false)
    private Route route;

    @ManyToOne
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;

    @Column(name = "departureTime", nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Boolean active = true;
}
