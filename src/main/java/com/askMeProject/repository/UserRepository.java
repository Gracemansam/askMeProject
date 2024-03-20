package com.askMeProject.repository;
import com.askMeProject.model.User;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByOwnerEmailAndPassword(String email, String password);
    Optional<User> findByEmail(String email);
}
