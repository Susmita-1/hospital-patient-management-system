package com.hospital.controller;

import com.hospital.entity.Patient;
import com.hospital.exception.DatabaseException;
import com.hospital.exception.PatientNotFoundException;
import com.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patients")
public class PatientUIController {

    @Autowired
    private PatientService patientService;

    @GetMapping
    public String listPatients(Model model) {
        try {
            model.addAttribute("patients", patientService.findAll());
        } catch (DatabaseException e) {
            model.addAttribute("error", "Failed to load patients");
        }
        return "patients/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patients/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("patient", patientService.findById(id));
        } catch (PatientNotFoundException | DatabaseException e) {
            model.addAttribute("error", "Patient not found");
            return "redirect:/patients";
        }
        return "patients/form";
    }

    @PostMapping
    public String savePatient(@ModelAttribute Patient patient) {
        try {
            if (patient.getId() != null) {
                patientService.update(patient);
            } else {
                patientService.save(patient);
            }
        } catch (PatientNotFoundException | DatabaseException e) {
            return "redirect:/patients?error=save failed";
        }
        return "redirect:/patients";
    }

    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id) {
        try {
            patientService.delete(id);
        } catch (PatientNotFoundException | DatabaseException e) {
            return "redirect:/patients?error=delete failed";
        }
        return "redirect:/patients";
    }
}