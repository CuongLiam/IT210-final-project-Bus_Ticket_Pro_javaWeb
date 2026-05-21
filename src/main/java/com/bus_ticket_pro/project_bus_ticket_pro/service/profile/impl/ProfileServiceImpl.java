package com.bus_ticket_pro.project_bus_ticket_pro.service.profile.impl;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.profile.ProfileRequest;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.User;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.UserProfile;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.UserProfileRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.UserRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.service.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public UserProfile getCurrentUserProfile(String username) {
        return userProfileRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ cá nhân"));
    }

    @Override
    public ProfileRequest getProfileForm(String username) {
        UserProfile profile = getCurrentUserProfile(username);

        ProfileRequest request = new ProfileRequest();
        request.setFullName(profile.getFullName());
        request.setPhone(profile.getPhone());
        request.setEmail(profile.getEmail());
        request.setAddress(profile.getAddress());

        return request;
    }

    @Override
    @Transactional
    public void updateProfile(String username, ProfileRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        UserProfile profile = userProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ cá nhân"));

        if (userProfileRepository.existsByPhoneAndUserUsernameNot(request.getPhone(), username)) {
            throw new RuntimeException("Số điện thoại đã được sử dụng bởi tài khoản khác");
        }

        if (userProfileRepository.existsByEmailAndUserUsernameNot(request.getEmail(), username)) {
            throw new RuntimeException("Email đã được sử dụng bởi tài khoản khác");
        }

        profile.setFullName(request.getFullName());
        profile.setPhone(request.getPhone());
        profile.setEmail(request.getEmail());
        profile.setAddress(request.getAddress());

        userProfileRepository.save(profile);
    }
}