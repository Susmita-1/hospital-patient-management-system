package com.hospital.observer;

import com.hospital.entity.Appointment;

public class SmsNotificationObserver implements AppointmentObserver {
    @Override
    public void onAppointmentCreated(Appointment appointment) {
        System.out.println("📱 SMS sent to: " + appointment.getPatient().getPhone());
        System.out.println("   Message: Appointment confirmed with Dr. " +
                appointment.getDoctor().getFirstName() + " " +
                appointment.getDoctor().getLastName() +
                " on " + appointment.getAppointmentDate());
        System.out.println("   Reason: " + appointment.getReason());
        System.out.println("--------------------------------------------------");
    }

    @Override
    public void onAppointmentCancelled(Appointment appointment) {
        System.out.println("📱 SMS sent to: " + appointment.getPatient().getPhone());
        System.out.println("   Message: Your appointment with Dr. " +
                appointment.getDoctor().getFirstName() + " " +
                appointment.getDoctor().getLastName() +
                " has been cancelled.");
        System.out.println("--------------------------------------------------");
    }
}