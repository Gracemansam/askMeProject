package com.askMeProject.repository;
import com.askMeProject.model.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByOwnerEmailAndPassword(String email, String password);
    Optional<User> findByEmail(String email);

    List<User> findByAuthorities(String role);

    Optional<User> findByIdAndAuthorities(Long id, String role);

    List<User> findByAuthoritiesAndSpeciality(String role, String speciality);

    List<User> findByAuthoritiesAndQualification(String role, String qualification);
}
