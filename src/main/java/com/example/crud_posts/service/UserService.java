package com.example.crud_posts.service;

import com.example.crud_posts.dto.UserDTO;
import com.example.crud_posts.entity.User;
import com.example.crud_posts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("المستخدم غير موجود: " + id));
    }

    @Transactional
    public User createUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("اسم المستخدم موجود مسبقاً");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("البريد الإلكتروني موجود مسبقاً");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, UserDTO userDTO) {
        User user = getUserById(id);
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(userDTO.getPassword());
        }
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}