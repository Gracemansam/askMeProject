package com.askMeProject.security;

import com.askMeProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
    @RequiredArgsConstructor
    public class UserRegistrationDetailsService implements UserDetailsService {
        private final UserRepository userRepository;





        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            return userRepository.findByOwnerEmail(email)
                    .orElseThrow(()-> new UsernameNotFoundException("User not found with email: " + email));
        }



}
