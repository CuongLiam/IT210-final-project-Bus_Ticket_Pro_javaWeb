package com.bus_ticket_pro.project_bus_ticket_pro.entity;

import com.bus_ticket_pro.project_bus_ticket_pro.entity.base.BaseEntity;
import com.bus_ticket_pro.project_bus_ticket_pro.enums.SeatStatus;
import jakarta.persistence.*;


@Entity
@Table(
        name = "seats",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"trip_id", "seat_number"}) // để 1 trip k có 2 seat trùng số
        }
)
public class Seat extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(name = "seat_number", nullable = false, length = 20)
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeatStatus status = SeatStatus.AVAILABLE;

    @Version
    private Long version; // UPDATE seats
                        //    SET status = 'PENDING', version = 2
                        //    WHERE id = 1 AND version = 1;

}
