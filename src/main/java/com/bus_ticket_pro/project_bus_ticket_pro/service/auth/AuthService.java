package com.bus_ticket_pro.project_bus_ticket_pro.service.auth;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.auth.RegisterRequest;

public interface AuthService {
    void registerPassenger(RegisterRequest request);
}
