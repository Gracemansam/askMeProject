package com.askMeProject.service.implementation;

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

    public List<User> getDoctorsBySpeciality(String speciality) {
        return userRepository.findByAuthoritiesAndSpeciality("DOCTOR", speciality);
    }

    public List<User> getDoctorsByQualification(String qualification) {
        return userRepository.findByAuthoritiesAndQualification("DOCTOR", qualification);
    }

}
