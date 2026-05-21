package com.bus_ticket_pro.project_bus_ticket_pro.service.auth.impl;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.auth.RegisterRequest;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.User;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.UserProfile;
import com.bus_ticket_pro.project_bus_ticket_pro.enums.Role;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.UserProfileRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.UserRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void registerPassenger(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }

        if (userProfileRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }

        if (userProfileRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.PASSENGER);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        UserProfile profile = new UserProfile();
        profile.setFullName(request.getFullName());
        profile.setPhone(request.getPhone());
        profile.setEmail(request.getEmail());
        profile.setAddress(request.getAddress());
        profile.setUser(savedUser);

        userProfileRepository.save(profile);
    }
}