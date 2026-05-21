package com.bus_ticket_pro.project_bus_ticket_pro.dto.trip;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class TripSearchRequest {

    @NotNull(message = "vui lòng chọn điểm đi")
    private Long fromLocationId;

    @NotNull(message = "Vui lòng chọn điểm đến")
    private Long toLocationId;

    @NotNull(message = "Vui lòng chọn ngày đi")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDate;

}
