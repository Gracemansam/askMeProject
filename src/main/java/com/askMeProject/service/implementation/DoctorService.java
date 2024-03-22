package com.askMeProject.service.implementation;

import com.askMeProject.dto.UserDTO;
import com.askMeProject.model.User;
import com.askMeProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {


    private final UserRepository userRepository;



    public List<User> getAllDoctors() {
        return userRepository.findByAuthorities("DOCTOR");
    }

    public User getDoctorById(Long doctorId) {
        return userRepository.findByIdAndAuthorities(doctorId, "DOCTOR")
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }
    public User editDoctor(Long doctorId, UserDTO updatedDoctor) {
        // Check if the doctor exists
        User existingDoctor = getDoctorById(doctorId);

        // Update doctor details
        existingDoctor.setFirstName(updatedDoctor.getFirstName());
        existingDoctor.setLastName(updatedDoctor.getLastName());
        existingDoctor.setEmail(updatedDoctor.getEmail());
        existingDoctor.setUsername(updatedDoctor.getUsername());
        existingDoctor.setPhoneNumber(updatedDoctor.getPhoneNumber());
        existingDoctor.setMfaEnabled(updatedDoctor.isMfaEnabled());
        existingDoctor.setSpeciality(updatedDoctor.getSpeciality());
        existingDoctor.setQualification(updatedDoctor.getQualification());




        // Save the updated doctor
        return userRepository.save(existingDoctor);
    }

    public List<User> getDoctorsBySpeciality(String speciality) {
        return userRepository.findByAuthoritiesAndSpeciality("DOCTOR", speciality);
    }

    public List<User> getDoctorsByQualification(String qualification) {
        return userRepository.findByAuthoritiesAndQualification("DOCTOR", qualification);
    }

}
