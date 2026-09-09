package com.hospital.observer;

import com.hospital.entity.Appointment;

public interface AppointmentObserver {
    void onAppointmentCreated(Appointment appointment);
    void onAppointmentCancelled(Appointment appointment);
}