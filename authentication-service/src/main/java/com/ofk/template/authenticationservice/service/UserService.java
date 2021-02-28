package com.ofk.template.authenticationservice.service;

import com.ofk.template.authenticationservice.dto.UserDTO;
import com.ofk.template.authenticationservice.exception.EmailAlreadyExistsException;
import com.ofk.template.authenticationservice.exception.UsernameAlreadyExistsException;
import com.ofk.template.authenticationservice.exception.UsernameNotFoundException;
import com.ofk.template.authenticationservice.model.Role;
import com.ofk.template.authenticationservice.model.User;
import com.ofk.template.authenticationservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Slf4j
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
            log.info("User dto is null, execution stopped");
            throw new RuntimeException();
        }

        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            log.info("Username already exists. Related username " + userDTO.getUsername());
            throw new UsernameAlreadyExistsException();
        } else if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            log.info("Email already exists. Related email " + userDTO.getEmail());
            throw new EmailAlreadyExistsException();
        }

        User user = modelMapper.map(userDTO, User.class);

        user.setId(UUID.randomUUID().toString());
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        log.info("New user created with username " + savedUser.getUsername());
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @Override
    public User loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s).orElseThrow(UsernameNotFoundException::new);
    }
}
