package com.session16_tmdt.service;

import com.session16_tmdt.model.User;
import com.session16_tmdt.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User findByEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        return userRepository.findByEmail(email.trim()).orElse(null);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
