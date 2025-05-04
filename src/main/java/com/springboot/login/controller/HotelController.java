package com.springboot.login.controller;

import com.springboot.login.entity.Hotel;
import com.springboot.login.entity.Manager;
import com.springboot.login.repository.HotelRepository;
import com.springboot.login.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/manager/hotel")
public class HotelController {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ManagerRepository managerRepository;

    // Create Hotel
    @PostMapping("/add")
    public String addHotel(@RequestBody Hotel hotel, @RequestParam String email) {
        Optional<Manager> managerOpt = managerRepository.findByEmail(email);
        if (managerOpt.isEmpty()) return "Manager not found!";

        // Check if a hotel with the same name already exists
        if (hotelRepository.findByName(hotel.getName()).isPresent()) {
            return "Hotel name already exists!";
        }

        hotel.setManager(managerOpt.get());
        hotelRepository.save(hotel);
        return "Hotel added successfully!";
    }

    // View Hotels by Manager
    @GetMapping("/view")
    public List<Hotel> viewHotels(@RequestParam String email) {
        Optional<Manager> managerOpt = managerRepository.findByEmail(email);
        return managerOpt.map(hotelRepository::findByManager).orElse(List.of());
    }

    @PutMapping("/update/by-id/{id}")
    public String updateHotelById(@PathVariable Long id, @RequestParam String email, @RequestBody Hotel updatedHotel) {
        Optional<Manager> managerOpt = managerRepository.findByEmail(email);
        if (managerOpt.isEmpty()) return "Manager not found!";

        Optional<Hotel> hotelOpt = hotelRepository.findById(id);
        if (hotelOpt.isEmpty()) return "Hotel not found!";

        Hotel hotel = hotelOpt.get();
        hotel.setName(updatedHotel.getName());
        hotel.setAddress(updatedHotel.getAddress());
        hotel.setContact(updatedHotel.getContact());
        hotel.setDescription(updatedHotel.getDescription());
        hotel.setAmenities(updatedHotel.getAmenities());
        hotelRepository.save(hotel);

        return "Hotel updated successfully!";
    }

    @PutMapping("/update/by-name")
    public String updateHotelByName(@RequestParam String name, @RequestParam String email, @RequestBody Hotel updatedHotel) {
        Optional<Manager> managerOpt = managerRepository.findByEmail(email);
        if (managerOpt.isEmpty()) return "Manager not found!";

        Optional<Hotel> hotelOpt = hotelRepository.findByName(name);
        if (hotelOpt.isEmpty()) return "Hotel not found!";

        Hotel hotel = hotelOpt.get();
        hotel.setName(updatedHotel.getName());
        hotel.setAddress(updatedHotel.getAddress());
        hotel.setContact(updatedHotel.getContact());
        hotel.setDescription(updatedHotel.getDescription());
        hotel.setAmenities(updatedHotel.getAmenities());
        hotelRepository.save(hotel);

        return "Hotel updated successfully!";
    }

    @DeleteMapping("/delete/by-id/{id}")
    public String deleteHotelById(@PathVariable Long id, @RequestParam String email) {
        Optional<Manager> managerOpt = managerRepository.findByEmail(email);
        if (managerOpt.isEmpty()) return "Manager not found!";

        if (!hotelRepository.existsById(id)) return "Hotel not found!";

        hotelRepository.deleteById(id);
        return "Hotel deleted successfully!";
    }

    @DeleteMapping("/delete/by-name")
    public String deleteHotelByName(@RequestParam String name, @RequestParam String email) {
        Optional<Manager> managerOpt = managerRepository.findByEmail(email);
        if (managerOpt.isEmpty()) return "Manager not found!";

        Optional<Hotel> hotelOpt = hotelRepository.findByName(name);
        if (hotelOpt.isEmpty()) return "Hotel not found!";

        hotelRepository.delete(hotelOpt.get());
        return "Hotel deleted successfully!";
    }
    
    @GetMapping("/all")
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

}
