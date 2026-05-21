package com.bus_ticket_pro.project_bus_ticket_pro.controller;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.profile.ProfileRequest;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.UserProfile;
import com.bus_ticket_pro.project_bus_ticket_pro.service.profile.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public String viewProfile(Authentication authentication, Model model) {
        String username = authentication.getName();

        UserProfile profile = profileService.getCurrentUserProfile(username);

        model.addAttribute("profile", profile);

        return "profile/view";
    }

    @GetMapping("/edit")
    public String editProfileForm(Authentication authentication, Model model) {
        String username = authentication.getName();

        ProfileRequest request = profileService.getProfileForm(username);

        model.addAttribute("profileRequest", request);

        return "profile/edit";
    }

    @PostMapping("/edit")
    public String updateProfile(
            Authentication authentication,
            @Valid @ModelAttribute("profileRequest") ProfileRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "profile/edit";
        }

        try {
            String username = authentication.getName();
            profileService.updateProfile(username, request);

            return "redirect:/profile?updated=true";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "profile/edit";
        }
    }
}