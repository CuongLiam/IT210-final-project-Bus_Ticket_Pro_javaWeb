package com.bus_ticket_pro.project_bus_ticket_pro.service.bus.impl;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.bus.BusRequest;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Bus;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.BusRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.service.bus.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusServiceImpl implements BusService {

    private final BusRepository busRepository;

    @Override
    public List<Bus> getAllBuses() {
//        return busRepository.findAll();

        return busRepository.findByActiveTrue();
    }

    @Override
    public Bus getBusById(Long id) {
        return busRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));
    }

    @Override
    @Transactional
    public void createBus(BusRequest request) {
        if (busRepository.existsByPlateNumber(request.getPlateNumber())) {
            throw new RuntimeException("Biển số xe đã tồn tại");
        }

        Bus bus = new Bus();
        bus.setPlateNumber(request.getPlateNumber());
        bus.setBusType(request.getBusType());
        bus.setTotalSeats(request.getTotalSeats());
        bus.setCompanyName(request.getCompanyName());
        bus.setDriverName(request.getDriverName());
        bus.setActive(true);

        busRepository.save(bus);
    }

    @Override
    @Transactional
    public void updateBus(Long id, BusRequest request) {
        Bus bus = getBusById(id);

        boolean plateExists = busRepository.existsByPlateNumber(request.getPlateNumber());
        boolean plateChanged = !bus.getPlateNumber().equalsIgnoreCase(request.getPlateNumber());

        if (plateChanged && plateExists) {
            throw new RuntimeException("Biển số xe đã tồn tại");
        }

        bus.setPlateNumber(request.getPlateNumber());
        bus.setBusType(request.getBusType());
        bus.setTotalSeats(request.getTotalSeats());
        bus.setCompanyName(request.getCompanyName());
        bus.setDriverName(request.getDriverName());

        busRepository.save(bus);
    }

    @Override
    @Transactional
    public void deleteBus(Long id) {
        Bus bus = getBusById(id);

        // Soft delete để tránh lỗi FK sau này khi Bus đã gắn với Trip
        bus.setActive(false);

        busRepository.save(bus);
    }
}