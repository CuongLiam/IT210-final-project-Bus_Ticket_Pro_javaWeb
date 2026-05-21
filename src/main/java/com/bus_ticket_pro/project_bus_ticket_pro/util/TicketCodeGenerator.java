package com.bus_ticket_pro.project_bus_ticket_pro.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class TicketCodeGenerator {

    private static final Random RANDOM = new Random();

    private TicketCodeGenerator() {
    }

    public static String generate() {
        String timePart = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        int randomPart = 1000 + RANDOM.nextInt(9000);

        return "BT" + timePart + randomPart;
    }
}