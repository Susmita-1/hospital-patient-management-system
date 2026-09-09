package com.hospital.observer;

import com.hospital.entity.Appointment;

public class EmailNotificationObserver implements AppointmentObserver {

    @Override
    public void onAppointmentCreated(Appointment appointment) {
        System.out.println("📧 EMAIL sent to: " + appointment.getPatient().getEmail());
        System.out.println("   Subject: Appointment Confirmation");
        System.out.println("   Body: Your appointment with Dr. " +
                appointment.getDoctor().getFirstName() + " " +
                appointment.getDoctor().getLastName() +
                " is scheduled for " + appointment.getAppointmentDate());
        System.out.println("   Reason: " + appointment.getReason());
        System.out.println("   Status: " + appointment.getStatus());
        System.out.println("--------------------------------------------------");
    }

    @Override
    public void onAppointmentCancelled(Appointment appointment) {
        System.out.println("📧 EMAIL sent to: " + appointment.getPatient().getEmail());
        System.out.println("   Subject: Appointment Cancellation");
        System.out.println("   Body: Your appointment with Dr. " +
                appointment.getDoctor().getFirstName() + " " +
                appointment.getDoctor().getLastName() +
                " has been cancelled.");
        System.out.println("   Date: " + appointment.getAppointmentDate());
        System.out.println("--------------------------------------------------");
    }
}