package com.bus_ticket_pro.project_bus_ticket_pro.dto.bus;

import com.bus_ticket_pro.project_bus_ticket_pro.enums.BusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusRequest {

    @NotBlank(message = "Biển số xe không được để trống")
    private String plateNumber;

    @NotNull(message = "Loại xe không được để trống")
    private BusType busType;

    @NotNull(message = "Tổng số ghế không được để trống")
    @Positive(message = "Tổng số ghế phải lớn hơn 0")
    private Integer totalSeats;

    @NotBlank(message = "Tên nhà xe không được để trống")
    private String companyName;

    @NotBlank(message = "Tên tài xế không được để trống")
    private String driverName;
}