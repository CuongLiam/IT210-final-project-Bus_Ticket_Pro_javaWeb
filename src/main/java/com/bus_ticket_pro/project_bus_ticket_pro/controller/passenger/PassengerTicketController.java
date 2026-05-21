package com.bus_ticket_pro.project_bus_ticket_pro.controller.passenger;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.ticket.CancelTicketRequest;
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

            CancelTicketRequest cancelTicketRequest = new CancelTicketRequest();
            cancelTicketRequest.setTicketCode(request.getTicketCode());
            cancelTicketRequest.setPhone(request.getPhone());

            model.addAttribute("ticket", ticketDetail);
            model.addAttribute("cancelTicketRequest", cancelTicketRequest);

            return "passenger/ticket/detail";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "passenger/ticket/lookup";
        }
    }

    @PostMapping("/cancel")
    public String cancelTicket(
            @Valid @ModelAttribute("cancelTicketRequest") CancelTicketRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Thông tin hủy vé không hợp lệ");
            model.addAttribute("ticketLookupRequest", new TicketLookupRequest());
            return "passenger/ticket/lookup";
        }

        try {
            ticketService.cancelTicket(request);
            return "redirect:/passenger/tickets/lookup?cancelled=true";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("ticketLookupRequest", new TicketLookupRequest());
            return "passenger/ticket/lookup";
        }
    }
}