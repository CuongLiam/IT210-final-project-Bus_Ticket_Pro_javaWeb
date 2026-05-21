package com.bus_ticket_pro.project_bus_ticket_pro.dto.seat;


import com.bus_ticket_pro.project_bus_ticket_pro.enums.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SeatViewDTO {
    private Long seatId;

    private String seatNumber;

    private SeatStatus status;
}
