package com.bus_ticket_pro.project_bus_ticket_pro.controller.staff;

import com.bus_ticket_pro.project_bus_ticket_pro.entity.Ticket;
import com.bus_ticket_pro.project_bus_ticket_pro.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/staff/tickets")
@RequiredArgsConstructor
public class StaffTicketController {

    private final PaymentService paymentService;

    @GetMapping("/pending")
    public String pendingTickets(Model model) {
        List<Ticket> tickets = paymentService.getPendingTickets();

        model.addAttribute("tickets", tickets);

        return "staff/ticket/pending";
    }

    @PostMapping("/confirm/{ticketId}")
    public String confirmPayment(
            @PathVariable Long ticketId,
            Model model
    ) {
        try {
            paymentService.confirmPayment(ticketId);
            return "redirect:/staff/tickets/pending?confirmed=true";
        } catch (RuntimeException e) {
            return "redirect:/staff/tickets/pending?error=" + e.getMessage();
        }
    }

    @PostMapping("/cancel/{ticketId}")
    public String cancelTicket(
            @PathVariable Long ticketId
    ) {
        try {
            paymentService.cancelPendingTicket(ticketId);
            return "redirect:/staff/tickets/pending?cancelled=true";
        } catch (RuntimeException e) {
            return "redirect:/staff/tickets/pending?error=" + e.getMessage();
        }
    }
}