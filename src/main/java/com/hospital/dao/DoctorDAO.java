package com.hospital.dao;

import com.hospital.entity.Doctor;
import com.hospital.exception.DatabaseException;
import com.hospital.exception.PatientNotFoundException;
import java.util.List;

public interface DoctorDAO {
    void save(Doctor doctor) throws DatabaseException;
    Doctor findById(Long id) throws PatientNotFoundException, DatabaseException;
    List<Doctor> findAll() throws DatabaseException;
    void update(Doctor doctor) throws PatientNotFoundException, DatabaseException;
    void delete(Long id) throws PatientNotFoundException, DatabaseException;
}