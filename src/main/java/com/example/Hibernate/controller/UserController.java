package com.example.Hibernate.controller;

import com.example.Hibernate.model.User;
import com.example.Hibernate.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("add")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @DeleteMapping("user/{id}")
    public void deleteUserById(@PathVariable(name = "id") Long id) throws Exception {
        userService.deleteUserById(id);
    }
}
