package com.bus_ticket_pro.project_bus_ticket_pro.controller.admin;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.bus.BusRequest;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Bus;
import com.bus_ticket_pro.project_bus_ticket_pro.enums.BusType;
import com.bus_ticket_pro.project_bus_ticket_pro.service.bus.BusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/buses")
@RequiredArgsConstructor
public class BusController {

    private final BusService busService;

    @GetMapping
    public String listBuses(Model model) {
        List<Bus> buses = busService.getAllBuses();

        model.addAttribute("buses", buses);

        return "admin/bus/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("busRequest", new BusRequest());
        model.addAttribute("busTypes", BusType.values());

        return "admin/bus/form";
    }

    @PostMapping("/create")
    public String createBus(
            @Valid @ModelAttribute("busRequest") BusRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("busTypes", BusType.values());
            return "admin/bus/form";
        }

        try {
            busService.createBus(request);
            return "redirect:/admin/buses?created=true";
        } catch (RuntimeException e) {
            model.addAttribute("busTypes", BusType.values());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/bus/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Bus bus = busService.getBusById(id);

        BusRequest request = new BusRequest();
        request.setPlateNumber(bus.getPlateNumber());
        request.setBusType(bus.getBusType());
        request.setTotalSeats(bus.getTotalSeats());
        request.setCompanyName(bus.getCompanyName());
        request.setDriverName(bus.getDriverName());

        model.addAttribute("busId", id);
        model.addAttribute("busRequest", request);
        model.addAttribute("busTypes", BusType.values());

        return "admin/bus/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateBus(
            @PathVariable Long id,
            @Valid @ModelAttribute("busRequest") BusRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("busId", id);
            model.addAttribute("busTypes", BusType.values());
            return "admin/bus/edit";
        }

        try {
            busService.updateBus(id, request);
            return "redirect:/admin/buses?updated=true";
        } catch (RuntimeException e) {
            model.addAttribute("busId", id);
            model.addAttribute("busTypes", BusType.values());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/bus/edit";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteBus(@PathVariable Long id) {
        busService.deleteBus(id);
        return "redirect:/admin/buses?deleted=true";
    }
}