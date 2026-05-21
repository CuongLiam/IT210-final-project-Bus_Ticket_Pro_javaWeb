package com.bus_ticket_pro.project_bus_ticket_pro.repository;

import com.bus_ticket_pro.project_bus_ticket_pro.entity.User;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    Optional<UserProfile> findByUser(User user);

    Optional<UserProfile> findByUserUsername(String username);

    boolean existsByPhoneAndUserUsernameNot(String phone, String username);

    boolean existsByEmailAndUserUsernameNot(String email, String username);
}