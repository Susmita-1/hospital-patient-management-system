package com.hospital.dao.impl;

import com.hospital.dao.DoctorDAO;
import com.hospital.entity.Doctor;
import com.hospital.exception.DatabaseException;
import com.hospital.exception.PatientNotFoundException;
import com.hospital.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAOImpl implements DoctorDAO {

    @Override
    public void save(Doctor doctor) throws DatabaseException {
        String sql = "INSERT INTO doctors (first_name, last_name, specialty, phone, email, qualification) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, doctor.getFirstName());
            ps.setString(2, doctor.getLastName());
            ps.setString(3, doctor.getSpecialty());
            ps.setString(4, doctor.getPhone());
            ps.setString(5, doctor.getEmail());
            ps.setString(6, doctor.getQualification());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                doctor.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save doctor: " + e.getMessage(), e);
        }
    }

    @Override
    public Doctor findById(Long id) throws PatientNotFoundException, DatabaseException {
        String sql = "SELECT * FROM doctors WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToDoctor(rs);
            } else {
                throw new PatientNotFoundException("Doctor with ID " + id + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find doctor: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Doctor> findAll() throws DatabaseException {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors ORDER BY first_name, last_name";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                doctors.add(mapRowToDoctor(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve doctors: " + e.getMessage(), e);
        }
        return doctors;
    }

    @Override
    public void update(Doctor doctor) throws PatientNotFoundException, DatabaseException {
        String sql = "UPDATE doctors SET first_name=?, last_name=?, specialty=?, phone=?, email=?, qualification=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, doctor.getFirstName());
            ps.setString(2, doctor.getLastName());
            ps.setString(3, doctor.getSpecialty());
            ps.setString(4, doctor.getPhone());
            ps.setString(5, doctor.getEmail());
            ps.setString(6, doctor.getQualification());
            ps.setLong(7, doctor.getId());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new PatientNotFoundException("Doctor with ID " + doctor.getId() + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update doctor: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) throws PatientNotFoundException, DatabaseException {
        String sql = "DELETE FROM doctors WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new PatientNotFoundException("Doctor with ID " + id + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete doctor: " + e.getMessage(), e);
        }
    }

    private Doctor mapRowToDoctor(ResultSet rs) throws SQLException {
        Doctor d = new Doctor();
        d.setId(rs.getLong("id"));
        d.setFirstName(rs.getString("first_name"));
        d.setLastName(rs.getString("last_name"));
        d.setSpecialty(rs.getString("specialty"));
        d.setPhone(rs.getString("phone"));
        d.setEmail(rs.getString("email"));
        d.setQualification(rs.getString("qualification"));
        return d;
    }
}