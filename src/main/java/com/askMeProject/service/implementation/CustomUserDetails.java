package com.askMeProject.service.implementation;


import com.askMeProject.model.User;
import com.askMeProject.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetails implements UserDetailsService {
	
	private UserRepository userRepository;
	
	public CustomUserDetails(UserRepository userRepository) {
		this.userRepository=userRepository;
		
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> findUser = userRepository.findByEmail(username);
		if(findUser.isEmpty()) {
			throw new UsernameNotFoundException("user not found with email "+username);
		}
		User user = findUser.get();
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		if(user.getAuthorities().equals("DOCTOR")) {
			authorities.add((GrantedAuthority) () -> "DOCTOR");}
			if(user.getAuthorities().equals("PATIENT")) {
				authorities.add((GrantedAuthority) () -> "PATIENT");
			}
		
		return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities);
	}

}
