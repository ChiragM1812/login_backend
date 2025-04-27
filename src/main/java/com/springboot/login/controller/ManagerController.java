package com.springboot.login.controller;

import com.springboot.login.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        return managerService.loginManager(email, password);
    }
}
