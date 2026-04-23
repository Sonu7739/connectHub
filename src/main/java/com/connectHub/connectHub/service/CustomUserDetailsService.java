package com.connectHub.connectHub.service;

import com.connectHub.connectHub.model.User;
import com.connectHub.connectHub.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
// 1. You must implement UserDetailsService
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override // 2. Uncomment this
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->

                    new UsernameNotFoundException("User not found")
                );

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                // Strip the prefix if it exists, or just pass the raw name
                .roles(user.getRole().replace("ROLE_", ""))
                .build();
    }
}
