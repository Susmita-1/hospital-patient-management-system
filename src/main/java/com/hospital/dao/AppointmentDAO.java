package com.hospital.dao;

import com.hospital.entity.Appointment;
import com.hospital.exception.DatabaseException;
import com.hospital.exception.PatientNotFoundException;
import java.util.List;

public interface AppointmentDAO {
    void save(Appointment appointment) throws DatabaseException;
    Appointment findById(Long id) throws PatientNotFoundException, DatabaseException;
    List<Appointment> findAll() throws PatientNotFoundException, DatabaseException;
    List<Appointment> findByPatientId(Long patientId) throws PatientNotFoundException, DatabaseException;
    List<Appointment> findByDoctorId(Long doctorId) throws PatientNotFoundException, DatabaseException;
    void update(Appointment appointment) throws PatientNotFoundException, DatabaseException;
    void delete(Long id) throws PatientNotFoundException, DatabaseException;
}