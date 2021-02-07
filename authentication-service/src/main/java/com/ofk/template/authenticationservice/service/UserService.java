package com.ofk.template.authenticationservice.service;

import com.ofk.template.authenticationservice.dto.UserDTO;
import com.ofk.template.authenticationservice.exception.BadCredentialsException;
import com.ofk.template.authenticationservice.exception.EmailAlreadyExistsException;
import com.ofk.template.authenticationservice.exception.UsernameAlreadyExistsException;
import com.ofk.template.authenticationservice.exception.UsernameNotFoundException;
import com.ofk.template.authenticationservice.model.User;
import com.ofk.template.authenticationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<User> signup(User user) {
        if (user == null) {
            throw new RuntimeException();
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException();
        } else if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s).orElseThrow(UsernameNotFoundException::new);
    }
}
