package com.askMeProject.service.implementation;

import com.askMeProject.dto.UserDTO;
import com.askMeProject.model.User;
import com.askMeProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final UserRepository userRepository;

    public List<User> getAllPatients() {
        return userRepository.findByAuthorities("PATIENT");
    }

    public User getPatientById(Long patientId) {
        return userRepository.findByIdAndAuthorities(patientId, "PATIENT")
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    public User editPatient(Long patientId, UserDTO updatedPatient) {
        // Check if the patient exists
        User existingPatient = getPatientById(patientId);

        // Update patient details
        existingPatient.setFirstName(updatedPatient.getFirstName());
        existingPatient.setLastName(updatedPatient.getLastName());
        existingPatient.setEmail(updatedPatient.getEmail());
        existingPatient.setPhoneNumber(updatedPatient.getPhoneNumber());
        existingPatient.setMfaEnabled(updatedPatient.isMfaEnabled());
        existingPatient.setUsername(updatedPatient.getUsername());


        // Save the updated patient
        return userRepository.save(existingPatient);
    }
}
