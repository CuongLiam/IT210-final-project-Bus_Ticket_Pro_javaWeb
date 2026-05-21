package com.bus_ticket_pro.project_bus_ticket_pro.controller.passenger;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.ticket.TicketDetailDTO;
import com.bus_ticket_pro.project_bus_ticket_pro.dto.ticket.TicketLookupRequest;
import com.bus_ticket_pro.project_bus_ticket_pro.service.ticket.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/passenger/tickets")
@RequiredArgsConstructor
public class PassengerTicketController {

    private final TicketService ticketService;

    @GetMapping("/lookup")
    public String lookupPage(Model model) {
        model.addAttribute("ticketLookupRequest", new TicketLookupRequest());
        return "passenger/ticket/lookup";
    }

    @PostMapping("/lookup")
    public String lookupTicket(
            @Valid @ModelAttribute("ticketLookupRequest") TicketLookupRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "passenger/ticket/lookup";
        }

        try {
            TicketDetailDTO ticketDetail = ticketService.lookupTicket(request);
            model.addAttribute("ticket", ticketDetail);

            return "passenger/ticket/detail";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "passenger/ticket/lookup";
        }
    }
}