package com.bus_ticket_pro.project_bus_ticket_pro.dto.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelTicketRequest {

    @NotBlank(message = "Mã vé không được để trống")
    private String ticketCode;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$", message = "Số điện thoại không hợp lệ")
    private String phone;
}