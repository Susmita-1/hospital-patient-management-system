package com.hospital.dao;

import com.hospital.entity.Patient;
import com.hospital.exception.DatabaseException;
import com.hospital.exception.PatientNotFoundException;
import java.util.List;

public interface PatientDAO {
    void save(Patient patient) throws DatabaseException;
    Patient findById(Long id) throws PatientNotFoundException, DatabaseException;
    List<Patient> findAll() throws DatabaseException;
    void update(Patient patient) throws PatientNotFoundException, DatabaseException;
    void delete(Long id) throws PatientNotFoundException, DatabaseException;
}