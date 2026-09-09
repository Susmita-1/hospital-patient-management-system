package com.hospital.controller;

import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.exception.DatabaseException;
import com.hospital.exception.PatientNotFoundException;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/appointments")
public class AppointmentUIController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public String listAppointments(Model model) {
        try {
            model.addAttribute("appointments", appointmentService.findAll());
        } catch (PatientNotFoundException | DatabaseException e) {
            model.addAttribute("error", "Failed to load appointments");
        }
        return "appointments/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        try {
            // Initialize with empty Patient and Doctor to avoid null in Thymeleaf
            Appointment appointment = new Appointment();
            appointment.setPatient(new Patient());
            appointment.setDoctor(new Doctor());
            model.addAttribute("appointment", appointment);

            model.addAttribute("patients", patientService.findAll());
            model.addAttribute("doctors", doctorService.findAll());
        } catch (DatabaseException e) {
            model.addAttribute("error", "Failed to load data");
        }
        return "appointments/form";
    }

    @PostMapping
    public String saveAppointment(@RequestParam Map<String, String> allParams) {
        System.out.println("=== APPOINTMENT FORM DATA ===");
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
        System.out.println("==============================");

        try {
            Long patientId = Long.parseLong(allParams.get("patientId"));
            Long doctorId = Long.parseLong(allParams.get("doctorId"));
            String appointmentDateStr = allParams.get("appointmentDate");
            String reason = allParams.get("reason");
            String notes = allParams.get("notes");

            Patient patient = patientService.findById(patientId);
            Doctor doctor = doctorService.findById(doctorId);

            // Fix date: browser sends "2026-09-10T10:00" – add ":00" if missing
            if (!appointmentDateStr.contains(":")) {
                appointmentDateStr = appointmentDateStr + ":00";
            }
            // If it already has seconds, use as is
            LocalDateTime dateTime = LocalDateTime.parse(appointmentDateStr);

            Appointment appointment = new Appointment();
            appointment.setPatient(patient);
            appointment.setDoctor(doctor);
            appointment.setAppointmentDate(dateTime);
            appointment.setStatus("SCHEDULED");
            appointment.setReason(reason);
            appointment.setNotes(notes);

            appointmentService.save(appointment);
            appointmentService.scheduleReminder(appointment);

            return "redirect:/appointments";
        } catch (Exception e) {
            e.printStackTrace(); // Print the exact error
            return "redirect:/appointments?error=save failed: " + e.getMessage();
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable Long id) {
        try {
            appointmentService.delete(id);
        } catch (PatientNotFoundException | DatabaseException e) {
            return "redirect:/appointments?error=delete failed";
        }
        return "redirect:/appointments";
    }
}