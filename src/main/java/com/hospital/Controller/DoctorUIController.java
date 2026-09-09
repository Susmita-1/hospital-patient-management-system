package com.hospital.controller;

import com.hospital.entity.Doctor;
import com.hospital.exception.DatabaseException;
import com.hospital.exception.PatientNotFoundException;
import com.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/doctors")
public class DoctorUIController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public String listDoctors(Model model) {
        try {
            model.addAttribute("doctors", doctorService.findAll());
        } catch (DatabaseException e) {
            model.addAttribute("error", "Failed to load doctors");
        }
        return "doctors/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "doctors/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("doctor", doctorService.findById(id));
        } catch (PatientNotFoundException | DatabaseException e) {
            model.addAttribute("error", "Doctor not found");
            return "redirect:/doctors";
        }
        return "doctors/form";
    }

    @PostMapping
    public String saveDoctor(@ModelAttribute Doctor doctor) {
        try {
            if (doctor.getId() != null) {
                doctorService.update(doctor);
            } else {
                doctorService.save(doctor);
            }
        } catch (PatientNotFoundException | DatabaseException e) {
            return "redirect:/doctors?error=save failed";
        }
        return "redirect:/doctors";
    }

    @GetMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable Long id) {
        try {
            doctorService.delete(id);
        } catch (PatientNotFoundException | DatabaseException e) {
            return "redirect:/doctors?error=delete failed";
        }
        return "redirect:/doctors";
    }
}