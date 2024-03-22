package com.askMeProject.controller;

import com.askMeProject.dto.UserDTO;
import org.springframework.web.bind.annotation.RestController;

import com.askMeProject.model.User;
import com.askMeProject.service.implementation.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllPatients() {
        List<User> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getPatientById(@PathVariable Long id) {
        User patient = patientService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> editPatient(@PathVariable Long id, @RequestBody UserDTO updatedPatient) {
        User editedPatient = patientService.editPatient(id, updatedPatient);
        return ResponseEntity.ok(editedPatient);
    }
}

