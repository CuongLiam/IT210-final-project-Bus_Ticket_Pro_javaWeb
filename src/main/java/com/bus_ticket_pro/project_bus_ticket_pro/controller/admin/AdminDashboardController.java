package com.bus_ticket_pro.project_bus_ticket_pro.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {

    @GetMapping("admin/dashboard")
    public String dashboard(){
        return "admin/dashboard";
    }
}
