package com.bus_ticket_pro.project_bus_ticket_pro.entity;

import com.bus_ticket_pro.project_bus_ticket_pro.entity.base.BaseEntity;
import com.bus_ticket_pro.project_bus_ticket_pro.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(nullable = false)
    private Boolean enabled = true;

}
