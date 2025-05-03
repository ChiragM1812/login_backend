package com.springboot.login.repository;

import com.springboot.login.entity.Hotel;
import com.springboot.login.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByManager(Manager manager);
    Optional<Hotel> findByName(String name);
}
