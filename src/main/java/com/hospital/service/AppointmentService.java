package com.hospital.service;

import com.hospital.dao.AppointmentDAO;
import com.hospital.dao.impl.AppointmentDAOImpl;
import com.hospital.entity.Appointment;
import com.hospital.exception.DatabaseException;
import com.hospital.exception.PatientNotFoundException;
import com.hospital.observer.AppointmentObserver;
import com.hospital.observer.EmailNotificationObserver;
import com.hospital.observer.SmsNotificationObserver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
    private final List<AppointmentObserver> observers = new ArrayList<>();
    private final AppointmentReminderService reminderService = new AppointmentReminderService();

    public AppointmentService() {
        observers.add(new EmailNotificationObserver());
        observers.add(new SmsNotificationObserver());
    }

    public void save(Appointment appointment) throws DatabaseException {
        appointmentDAO.save(appointment);
        notifyObservers(appointment);
    }

    public Appointment findById(Long id) throws PatientNotFoundException, DatabaseException {
        return appointmentDAO.findById(id);
    }

    public List<Appointment> findAll() throws PatientNotFoundException, DatabaseException {
        return appointmentDAO.findAll();
    }

    public List<Appointment> findByPatientId(Long patientId) throws PatientNotFoundException, DatabaseException {
        return appointmentDAO.findByPatientId(patientId);
    }

    public List<Appointment> findByDoctorId(Long doctorId) throws PatientNotFoundException, DatabaseException {
        return appointmentDAO.findByDoctorId(doctorId);
    }

    public void update(Appointment appointment) throws PatientNotFoundException, DatabaseException {
        appointmentDAO.update(appointment);
    }

    public void delete(Long id) throws PatientNotFoundException, DatabaseException {
        appointmentDAO.delete(id);
    }

    public void scheduleReminder(Appointment appointment) {
        reminderService.scheduleReminder(appointment);
    }

    private void notifyObservers(Appointment appointment) {
        for (AppointmentObserver observer : observers) {
            observer.onAppointmentCreated(appointment);
        }
    }
}