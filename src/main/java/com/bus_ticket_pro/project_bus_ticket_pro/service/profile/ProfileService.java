package com.bus_ticket_pro.project_bus_ticket_pro.service.profile;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.profile.ProfileRequest;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.UserProfile;

public interface ProfileService {

    UserProfile getCurrentUserProfile(String username);

    ProfileRequest getProfileForm(String username);

    void updateProfile(String username, ProfileRequest request);
}