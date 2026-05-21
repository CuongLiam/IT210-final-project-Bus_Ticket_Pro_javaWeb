package com.bus_ticket_pro.project_bus_ticket_pro.controller.staff;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaffDashboardController {

    @GetMapping("/staff/dashboard")
    public String dashboard() {
        return "staff/dashboard";
    }
}