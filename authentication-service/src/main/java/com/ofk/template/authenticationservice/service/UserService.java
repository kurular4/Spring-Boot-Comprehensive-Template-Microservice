package com.ofk.template.authenticationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ofk.template.authenticationservice.dto.UserDTO;
import com.ofk.template.authenticationservice.exception.BadCredentialsException;
import com.ofk.template.authenticationservice.exception.EmailAlreadyExistsException;
import com.ofk.template.authenticationservice.exception.UsernameAlreadyExistsException;
import com.ofk.template.authenticationservice.exception.UsernameNotFoundException;
import com.ofk.template.authenticationservice.model.Role;
import com.ofk.template.authenticationservice.model.User;
import com.ofk.template.authenticationservice.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<User> signup(UserDTO userDTO) {
        if (userDTO == null) {
            throw new RuntimeException();
        }

        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException();
        } else if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        User user = modelMapper.map(userDTO, User.class);

        user.setId(UUID.randomUUID().toString());
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s).orElseThrow(UsernameNotFoundException::new);
    }
}
