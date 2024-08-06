package com.example.task_manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.task_manager.dto.ApiResponse;
import com.example.task_manager.dto.AuthenticationRequest;
import com.example.task_manager.dto.AuthenticationResponse;
import com.example.task_manager.dto.UserDTO;
import com.example.task_manager.dto.UserResponseDTO;
import com.example.task_manager.exception.UserAlreadyExistsException;
import com.example.task_manager.model.Role;
import com.example.task_manager.model.User;
import com.example.task_manager.security.JwtUtil;
import com.example.task_manager.service.TokenBlacklistService;
import com.example.task_manager.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, TokenBlacklistService tokenBlacklistService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> registerUser(@RequestBody UserDTO userDTO) {
        try {
            UserResponseDTO response = userService.registerUser(userDTO);

            ApiResponse<UserResponseDTO> apiResponse = new ApiResponse<>(HttpStatus.CREATED.value(),
                    "User registered successfully", null);

            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            ApiResponse<UserResponseDTO> apiResponse = new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(),
                    null);
            return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
        } catch (Exception e) {
            ApiResponse<UserResponseDTO> apiResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An error occurred", null);
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(
            @RequestBody AuthenticationRequest authenticationRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Set<Role> roles = userService.getRolesByUserName(userDetails.getUsername());

            // Convertir roles a String
            Set<String> roleNames = roles.stream().map(Role::name).collect(Collectors.toSet());

            String jwt = jwtUtil.generateToken(userDetails.getUsername(), roleNames);

            AuthenticationResponse authResponse = new AuthenticationResponse(jwt);
            ApiResponse<AuthenticationResponse> response = new ApiResponse<>(HttpStatus.OK.value(), "Login successful",
                    authResponse);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            ApiResponse<AuthenticationResponse> response = new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(),
                    "Invalid credentials", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            long expirationTime = jwtUtil.extractExpiration(token).getTime();
            tokenBlacklistService.addToBlacklist(token, expirationTime);
            SecurityContextHolder.clearContext();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/current-user")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());

        UserDTO userDTO = new UserDTO(user.getUsername(), null, user.getEmail());
        ApiResponse<UserDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "Current user retrieved successfully",
                userDTO);

        return ResponseEntity.ok(response);
    }
}
