package com.bus_ticket_pro.project_bus_ticket_pro.service.bus;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.bus.BusRequest;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Bus;

import java.util.List;

public interface BusService {

    List<Bus> getAllBuses();

    Bus getBusById(Long id);

    void createBus(BusRequest request);

    void updateBus(Long id, BusRequest request);

    void deleteBus(Long id);
}