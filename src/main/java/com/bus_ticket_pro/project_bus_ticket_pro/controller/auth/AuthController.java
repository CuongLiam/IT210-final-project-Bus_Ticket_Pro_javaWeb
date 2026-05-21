package com.bus_ticket_pro.project_bus_ticket_pro.controller.auth;


import com.bus_ticket_pro.project_bus_ticket_pro.dto.auth.RegisterRequest;
import com.bus_ticket_pro.project_bus_ticket_pro.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }

    public String registerPage(Model model){
        model.addAttribute("registerRequest", new RegisterRequest());

        return "auth/register";
    }

    public String register(
            @Valid @ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult bindingResult,
            Model model
    ){

        if (bindingResult.hasErrors()){
            return "auth/register";
        }

        try{
            authService.registerPassenger(request);
            return "redirect:/login?=registered=true";
        } catch (RuntimeException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/register";
        }

    }
}
