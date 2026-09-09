package com.hospital.service;

import com.hospital.dao.DoctorDAO;
import com.hospital.dao.impl.DoctorDAOImpl;
import com.hospital.entity.Doctor;
import com.hospital.exception.DatabaseException;
import com.hospital.exception.PatientNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorDAO doctorDAO = new DoctorDAOImpl();

    public void save(Doctor doctor) throws DatabaseException {
        doctorDAO.save(doctor);
    }

    public Doctor findById(Long id) throws PatientNotFoundException, DatabaseException {
        return doctorDAO.findById(id);
    }

    public List<Doctor> findAll() throws DatabaseException {
        return doctorDAO.findAll();
    }

    public void update(Doctor doctor) throws PatientNotFoundException, DatabaseException {
        doctorDAO.update(doctor);
    }

    public void delete(Long id) throws PatientNotFoundException, DatabaseException {
        doctorDAO.delete(id);
    }
}