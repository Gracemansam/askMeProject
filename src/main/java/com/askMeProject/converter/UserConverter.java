package com.askMeProject.converter;

import com.askMeProject.dto.UserDTO;
import com.askMeProject.model.User;
import org.springframework.stereotype.Component;


@Component
public class UserConverter {


    public User convertDTOtoPatientEntity(UserDTO userDTO){
        User patientEntity = new User();
        patientEntity.setFirstName(userDTO.getFirstName());
        patientEntity.setLastName(userDTO.getLastName());
        patientEntity.setPhoneNumber(userDTO.getPhoneNumber());
        patientEntity.setUsername(userDTO.getUserName());
        patientEntity.setEmail(userDTO.getEmail());
        return patientEntity;
    }
    public User convertDTOtoDoctorEntity(UserDTO userDTO){
        User doctorEntity = new User();
        doctorEntity.setFirstName(userDTO.getFirstName());
        doctorEntity.setLastName(userDTO.getLastName());
        doctorEntity.setPassword(userDTO.getPassword());
        doctorEntity.setUsername(userDTO.getUserName());
        doctorEntity.setEmail(userDTO.getEmail());
        doctorEntity.setSpeciality(userDTO.getSpeciality());
        doctorEntity.setQualification(userDTO.getQualification());
        return doctorEntity;
    }

    public UserDTO convertPatientEntityToDTO(User userEntity){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setFirstName(userEntity.getFirstName());
        userDTO.setLastName(userEntity.getLastName());
        userDTO.setPhoneNumber(userEntity.getPhoneNumber());
        userDTO.setUserName(userEntity.getUsername());
        return userDTO;
    }
    public UserDTO convertDoctorEntityToDTO(User userEntity){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setFirstName(userEntity.getFirstName());
        userDTO.setLastName(userEntity.getLastName());
        userDTO.setPhoneNumber(userEntity.getPhoneNumber());
        userDTO.setUserName(userEntity.getUsername());
        userDTO.setQualification(userEntity.getQualification());
        userDTO.setSpeciality(userEntity.getSpeciality());
        return userDTO;
    }
}