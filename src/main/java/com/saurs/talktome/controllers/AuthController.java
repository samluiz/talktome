package com.saurs.talktome.controllers;

import com.saurs.talktome.dtos.LoginDTO;
import com.saurs.talktome.dtos.RegisterDTO;
import com.saurs.talktome.security.auth.AuthenticationResponse;
import com.saurs.talktome.services.AuthService;
import com.saurs.talktome.services.SecurityUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterDTO data) {
        return ResponseEntity.ok(service.register(data));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginDTO login) {
        return ResponseEntity.ok(service.authenticate(login));
    }

    @GetMapping("/user")
    public ResponseEntity<String> getAuthenticatedUser(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok("Active user: " + user.getUsername());
    }
}
