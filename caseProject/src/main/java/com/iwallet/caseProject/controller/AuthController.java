package com.iwallet.caseProject.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iwallet.caseProject.dto.RegisterDto;
import com.iwallet.caseProject.model.IwalletUser;
import com.iwallet.caseProject.model.Role;
import com.iwallet.caseProject.repository.IwalletUserRepository;
import com.iwallet.caseProject.repository.RoleRepository;
import com.iwallet.caseProject.security.JwtAuthFilter;
import com.iwallet.caseProject.security.JwtGenerator;
import com.iwallet.caseProject.service.TokenBlacklist;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/iwalletapi/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private IwalletUserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtGenerator jwtGenerator;
    private JwtAuthFilter jwtAuthFilter;
    private TokenBlacklist tokenBlacklist;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, IwalletUserRepository userRepository,
    						RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtGenerator jwtGenerator, TokenBlacklist tokenBlacklist, JwtAuthFilter jwtAuthFilter) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.tokenBlacklist = tokenBlacklist;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody IwalletUser loginUser){
    	Authentication authentication = authenticationManager.authenticate(
    			new UsernamePasswordAuthenticationToken(
    					loginUser.getUsername(),
    					loginUser.getPassword()));
    	SecurityContextHolder.getContext().setAuthentication(authentication);
    	String token = jwtGenerator.generateToken(authentication);
    	return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = jwtAuthFilter.getJWTFromRequest(request);
        tokenBlacklist.addToBlacklist(token);

        return ResponseEntity.ok("Logged out successfully");    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }

        IwalletUser user = new IwalletUser();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode((registerDto.getPassword())));

        Role roles = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singletonList(roles));

        userRepository.save(user);

        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
    }
}