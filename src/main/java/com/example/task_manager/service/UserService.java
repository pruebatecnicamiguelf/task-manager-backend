package com.example.task_manager.service;

import java.util.Set;

import com.example.task_manager.dto.UserDTO;
import com.example.task_manager.dto.UserResponseDTO;
import com.example.task_manager.model.Role;
import com.example.task_manager.model.User;

public interface UserService {
    UserResponseDTO registerUser(UserDTO userDTO);
    User loginUser(String username, String password);
    User findByUsername(String username);
    Set<Role> getRolesByUserName(String username);
}
