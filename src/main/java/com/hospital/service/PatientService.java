package com.hospital.service;

import com.hospital.dao.PatientDAO;
import com.hospital.dao.impl.PatientDAOImpl;
import com.hospital.entity.Patient;
import com.hospital.exception.DatabaseException;
import com.hospital.exception.PatientNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PatientService {

    private final PatientDAO patientDAO = new PatientDAOImpl();

    public void save(Patient patient) throws DatabaseException {
        patientDAO.save(patient);
    }

    public Patient findById(Long id) throws PatientNotFoundException, DatabaseException {
        return patientDAO.findById(id);
    }

    public List<Patient> findAll() throws DatabaseException {
        return patientDAO.findAll();
    }

    public void update(Patient patient) throws PatientNotFoundException, DatabaseException {
        patientDAO.update(patient);
    }

    public void delete(Long id) throws PatientNotFoundException, DatabaseException {
        patientDAO.delete(id);
    }
}