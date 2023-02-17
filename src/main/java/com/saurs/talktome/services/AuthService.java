package com.saurs.talktome.services;

import com.saurs.talktome.dtos.LoginDTO;
import com.saurs.talktome.dtos.RegisterDTO;
import com.saurs.talktome.models.User;
import com.saurs.talktome.repositories.RoleRepository;
import com.saurs.talktome.repositories.UserRepository;
import com.saurs.talktome.security.auth.AuthenticationResponse;
import com.saurs.talktome.security.jwt.JwtService;
import com.saurs.talktome.services.exceptions.ObjectNotFoundException;
import com.saurs.talktome.services.exceptions.UsernameAlreadyTakenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authManager;

    public AuthenticationResponse register (RegisterDTO data) {
        if (!repository.existsByEmail(data.getEmail()) && !repository.existsByUsername(data.getUsername())) {
            var user = User.builder()
                    .username(data.getUsername())
                    .email(data.getEmail())
                    .password(passwordEncoder.encode(data.getPassword()))
                    .roles(Set.of(roleRepository.findByName("ROLE_USER").get()))
                    .build();
            repository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }
        throw new UsernameAlreadyTakenException();
    }

    public AuthenticationResponse authenticate(LoginDTO login) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.getUsername(),
                        login.getPassword()
                )
        );
        var user = repository.findByUsername(login.getUsername()).orElseThrow(() -> new ObjectNotFoundException(login.getUsername()));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
