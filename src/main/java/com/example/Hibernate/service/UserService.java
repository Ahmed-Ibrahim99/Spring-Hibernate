package com.example.Hibernate.service;

import com.example.Hibernate.model.User;
import com.example.Hibernate.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll(User.class);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(User.class, id);
    }

    public User addUser(User user)  {
        return userRepository.save(user);
    }
}
