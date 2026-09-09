package com.hospital.dao.impl;

import com.hospital.dao.PatientDAO;
import com.hospital.entity.Patient;
import com.hospital.exception.DatabaseException;
import com.hospital.exception.PatientNotFoundException;
import com.hospital.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAOImpl implements PatientDAO {

    @Override
    public void save(Patient patient) throws DatabaseException {
        String sql = "INSERT INTO patients (first_name, last_name, date_of_birth, gender, phone, email, address, emergency_contact_name, emergency_contact_phone, active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, patient.getFirstName());
            ps.setString(2, patient.getLastName());
            ps.setDate(3, Date.valueOf(patient.getDateOfBirth()));
            ps.setString(4, patient.getGender());
            ps.setString(5, patient.getPhone());
            ps.setString(6, patient.getEmail());
            ps.setString(7, patient.getAddress());
            ps.setString(8, patient.getEmergencyContactName());
            ps.setString(9, patient.getEmergencyContactPhone());
            ps.setBoolean(10, patient.isActive());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                patient.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save patient: " + e.getMessage(), e);
        }
    }

    @Override
    public Patient findById(Long id) throws PatientNotFoundException, DatabaseException {
        String sql = "SELECT * FROM patients WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToPatient(rs);
            } else {
                throw new PatientNotFoundException("Patient with ID " + id + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find patient: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Patient> findAll() throws DatabaseException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                patients.add(mapRowToPatient(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve patients: " + e.getMessage(), e);
        }
        return patients;
    }

    @Override
    public void update(Patient patient) throws PatientNotFoundException, DatabaseException {
        String sql = "UPDATE patients SET first_name=?, last_name=?, date_of_birth=?, gender=?, phone=?, email=?, address=?, emergency_contact_name=?, emergency_contact_phone=?, active=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, patient.getFirstName());
            ps.setString(2, patient.getLastName());
            ps.setDate(3, Date.valueOf(patient.getDateOfBirth()));
            ps.setString(4, patient.getGender());
            ps.setString(5, patient.getPhone());
            ps.setString(6, patient.getEmail());
            ps.setString(7, patient.getAddress());
            ps.setString(8, patient.getEmergencyContactName());
            ps.setString(9, patient.getEmergencyContactPhone());
            ps.setBoolean(10, patient.isActive());
            ps.setLong(11, patient.getId());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new PatientNotFoundException("Patient with ID " + patient.getId() + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update patient: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) throws PatientNotFoundException, DatabaseException {
        String sql = "DELETE FROM patients WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new PatientNotFoundException("Patient with ID " + id + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete patient: " + e.getMessage(), e);
        }
    }

    private Patient mapRowToPatient(ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.setId(rs.getLong("id"));
        p.setFirstName(rs.getString("first_name"));
        p.setLastName(rs.getString("last_name"));
        p.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        p.setGender(rs.getString("gender"));
        p.setPhone(rs.getString("phone"));
        p.setEmail(rs.getString("email"));
        p.setAddress(rs.getString("address"));
        p.setEmergencyContactName(rs.getString("emergency_contact_name"));
        p.setEmergencyContactPhone(rs.getString("emergency_contact_phone"));
        p.setActive(rs.getBoolean("active"));
        return p;
    }
}