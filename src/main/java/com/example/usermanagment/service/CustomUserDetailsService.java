package com.example.usermanagment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.usermanagment.model.User;
import com.example.usermanagment.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	
	@Autowired
	UserRepository userRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userRepository.findById(username).orElse(null);
		if(user==null) {
			throw new UsernameNotFoundException("No User Found");
		}
		return new CustomUserDetails(user);
	}

}
