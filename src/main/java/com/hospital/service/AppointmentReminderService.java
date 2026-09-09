package com.hospital.service;

import com.hospital.entity.Appointment;
import com.hospital.observer.EmailNotificationObserver;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class AppointmentReminderService {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    public void scheduleReminder(Appointment appointment) {
        // Calculate delay until 24 hours before appointment
        long delay = LocalDateTime.now().until(appointment.getAppointmentDate().minusHours(24), ChronoUnit.SECONDS);
        if (delay < 0) delay = 0;

        scheduler.schedule(() -> {
            new EmailNotificationObserver().onAppointmentCreated(appointment);
            System.out.println("🔔 Reminder sent for appointment ID: " + appointment.getId());
        }, delay, TimeUnit.SECONDS);
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}