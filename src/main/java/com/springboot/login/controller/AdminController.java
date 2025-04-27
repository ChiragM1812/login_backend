package com.springboot.login.controller;

import com.springboot.login.entity.Admin;
import org.springframework.web.bind.annotation.RequestBody;
import com.springboot.login.entity.Manager;
import com.springboot.login.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/signup")
    public String signup(@RequestBody Admin admin) {
        return adminService.registerAdmin(admin);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        // login logic
    }

    @PostMapping("/create-manager")
    public String createManager(@RequestBody Manager manager) {
        return adminService.createManager(manager);
    }
}
