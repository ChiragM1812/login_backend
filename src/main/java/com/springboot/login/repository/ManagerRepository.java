package com.springboot.login.repository;

import com.springboot.login.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Optional<Manager> findByEmail(String email);
}
