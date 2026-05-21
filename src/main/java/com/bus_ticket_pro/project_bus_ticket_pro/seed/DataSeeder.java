package com.bus_ticket_pro.project_bus_ticket_pro.seed;

import com.bus_ticket_pro.project_bus_ticket_pro.entity.User;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.UserProfile;
import com.bus_ticket_pro.project_bus_ticket_pro.enums.Role;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.UserProfileRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        seedAdmin();
        seedStaff();
    }

    private void seedAdmin() {
        if (userRepository.existsByUsername("admin")) {
            return;
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("123456"));
        admin.setRole(Role.ADMIN);
        admin.setEnabled(true);

        User savedAdmin = userRepository.save(admin);

        UserProfile profile = new UserProfile();
        profile.setFullName("System Admin");
        profile.setPhone("0900000001");
        profile.setEmail("admin@gmail.com");
        profile.setAddress("Hà Nội");
        profile.setUser(savedAdmin);

        userProfileRepository.save(profile);
    }

    private void seedStaff() {
        if (userRepository.existsByUsername("staff")) {
            return;
        }

        User staff = new User();
        staff.setUsername("staff");
        staff.setPassword(passwordEncoder.encode("123456"));
        staff.setRole(Role.STAFF);
        staff.setEnabled(true);

        User savedStaff = userRepository.save(staff);

        UserProfile profile = new UserProfile();
        profile.setFullName("System Staff");
        profile.setPhone("0900000002");
        profile.setEmail("staff@gmail.com");
        profile.setAddress("Hà Nội");
        profile.setUser(savedStaff);

        userProfileRepository.save(profile);
    }
}