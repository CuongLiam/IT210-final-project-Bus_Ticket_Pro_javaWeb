package com.bus_ticket_pro.project_bus_ticket_pro.entity;

import com.bus_ticket_pro.project_bus_ticket_pro.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "user_profile")
@Getter
@Setter
public class UserProfile extends BaseEntity {

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(unique = true, length = 20)
    private String phone;

    @Column(unique = true, length = 100)
    private String email;

    private String address;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;


}
