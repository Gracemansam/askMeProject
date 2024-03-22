package com.askMeProject.controller;

import com.askMeProject.dto.UserDTO;
import com.askMeProject.model.User;
import com.askMeProject.service.implementation.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;



    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllDoctors() {
        List<User> doctors = doctorService.getAllDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<User> getDoctorById(@PathVariable Long doctorId) {
        User doctor = doctorService.getDoctorById(doctorId);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    @GetMapping("/speciality/{speciality}")
    public ResponseEntity<List<User>> getDoctorsBySpeciality(@PathVariable String speciality) {
        List<User> doctors = doctorService.getDoctorsBySpeciality(speciality);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/qualification/{qualification}")
    public ResponseEntity<List<User>> getDoctorsByQualification(@PathVariable String qualification) {
        List<User> doctors = doctorService.getDoctorsByQualification(qualification);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> editDoctor(@PathVariable Long id, @RequestBody UserDTO updatedDoctor) {
        User editedDoctor = doctorService.editDoctor(id, updatedDoctor);
        return ResponseEntity.ok(editedDoctor);
    }
}
