
package com.springboot.login.service;

import com.springboot.login.entity.Admin;
import com.springboot.login.entity.Manager;
import com.springboot.login.repository.AdminRepository;
import com.springboot.login.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ManagerRepository managerRepository;

    public String registerAdmin(Admin admin) {
        adminRepository.save(admin);
        return "Admin registered successfully.";
    }

    public String loginAdmin(String email, String password) {
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isPresent() && admin.get().getPassword().equals(password)) {
            return "Admin login successful.";
        } else {
            return "Invalid credentials.";
        }
    }

    public String createManager(Manager manager) {
        managerRepository.save(manager);
        return "Manager created successfully by Admin.";
    }
}
