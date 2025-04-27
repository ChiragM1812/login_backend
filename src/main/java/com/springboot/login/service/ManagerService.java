package com.springboot.login.service;

import com.springboot.login.entity.Manager;
import com.springboot.login.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    public String loginManager(String email, String password) {
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isPresent() && manager.get().getPassword().equals(password)) {
            return "Manager login successful.";
        } else {
            return "Invalid credentials.";
        }
    }
}
