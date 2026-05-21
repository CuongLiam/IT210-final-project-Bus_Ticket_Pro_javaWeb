package com.bus_ticket_pro.project_bus_ticket_pro.repository;

import com.bus_ticket_pro.project_bus_ticket_pro.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusRepository extends JpaRepository<Bus, Long> {

    boolean existsByPlateNumber(String plateNumber);

    List<Bus> findByActiveTrue();
}
