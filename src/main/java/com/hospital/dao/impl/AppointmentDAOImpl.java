package com.hospital.dao.impl;

import com.hospital.dao.AppointmentDAO;
import com.hospital.dao.DoctorDAO;
import com.hospital.dao.PatientDAO;
import com.hospital.entity.Appointment;
import com.hospital.exception.DatabaseException;
import com.hospital.exception.PatientNotFoundException;
import com.hospital.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAOImpl implements AppointmentDAO {

    private final PatientDAO patientDAO = new PatientDAOImpl();
    private final DoctorDAO doctorDAO = new DoctorDAOImpl();

    @Override
    public void save(Appointment appointment) throws DatabaseException {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, status, reason, notes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, appointment.getPatient().getId());
            ps.setLong(2, appointment.getDoctor().getId());
            ps.setTimestamp(3, Timestamp.valueOf(appointment.getAppointmentDate()));
            ps.setString(4, appointment.getStatus());
            ps.setString(5, appointment.getReason());
            ps.setString(6, appointment.getNotes());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                appointment.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save appointment: " + e.getMessage(), e);
        }
    }

    @Override
    public Appointment findById(Long id) throws PatientNotFoundException, DatabaseException {
        String sql = "SELECT * FROM appointments WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToAppointment(rs);
            } else {
                throw new PatientNotFoundException("Appointment with ID " + id + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find appointment: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Appointment> findAll() throws PatientNotFoundException, DatabaseException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments ORDER BY appointment_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                appointments.add(mapRowToAppointment(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve appointments: " + e.getMessage(), e);
        }
        return appointments;
    }

    @Override
    public List<Appointment> findByPatientId(Long patientId) throws PatientNotFoundException, DatabaseException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE patient_id = ? ORDER BY appointment_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                appointments.add(mapRowToAppointment(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve appointments for patient: " + e.getMessage(), e);
        }
        return appointments;
    }

    @Override
    public List<Appointment> findByDoctorId(Long doctorId) throws PatientNotFoundException, DatabaseException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE doctor_id = ? ORDER BY appointment_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, doctorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                appointments.add(mapRowToAppointment(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve appointments for doctor: " + e.getMessage(), e);
        }
        return appointments;
    }

    @Override
    public void update(Appointment appointment) throws PatientNotFoundException, DatabaseException {
        String sql = "UPDATE appointments SET patient_id=?, doctor_id=?, appointment_date=?, status=?, reason=?, notes=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, appointment.getPatient().getId());
            ps.setLong(2, appointment.getDoctor().getId());
            ps.setTimestamp(3, Timestamp.valueOf(appointment.getAppointmentDate()));
            ps.setString(4, appointment.getStatus());
            ps.setString(5, appointment.getReason());
            ps.setString(6, appointment.getNotes());
            ps.setLong(7, appointment.getId());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new PatientNotFoundException("Appointment with ID " + appointment.getId() + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update appointment: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) throws PatientNotFoundException, DatabaseException {
        String sql = "DELETE FROM appointments WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new PatientNotFoundException("Appointment with ID " + id + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete appointment: " + e.getMessage(), e);
        }
    }

    private Appointment mapRowToAppointment(ResultSet rs) throws SQLException, PatientNotFoundException, DatabaseException {
        Appointment a = new Appointment();
        a.setId(rs.getLong("id"));
        a.setAppointmentDate(rs.getTimestamp("appointment_date").toLocalDateTime());
        a.setStatus(rs.getString("status"));
        a.setReason(rs.getString("reason"));
        a.setNotes(rs.getString("notes"));

        Long patientId = rs.getLong("patient_id");
        Long doctorId = rs.getLong("doctor_id");
        a.setPatient(patientDAO.findById(patientId));
        a.setDoctor(doctorDAO.findById(doctorId));

        return a;
    }
}