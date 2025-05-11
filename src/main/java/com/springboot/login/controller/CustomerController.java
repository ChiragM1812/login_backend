package com.springboot.login.controller;

import com.springboot.login.dto.CustomerSignupDTO;
import com.springboot.login.dto.CustomerLoginDTO;
import com.springboot.login.entity.Customer;
import com.springboot.login.entity.Hotel;
import com.springboot.login.entity.Room;
import com.springboot.login.repository.CustomerRepository;
import com.springboot.login.repository.HotelRepository;
import com.springboot.login.repository.RoomRepository;
import com.springboot.login.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @PostMapping("/signup")
    public String signup(@RequestBody CustomerSignupDTO signupDTO) {
        return customerService.registerCustomer(signupDTO);
    }

    @GetMapping("/all")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }
    
    @PostMapping("/login")
    public String login(@RequestBody CustomerLoginDTO loginDTO) {
        return customerService.loginCustomer(loginDTO);
    }
    private boolean isCustomerValid(String email) {
        return customerRepository.findByEmail(email).isPresent();
    }

    // View All Hotels
    @GetMapping("/hotels")
    public ResponseEntity<?> viewHotels(@RequestParam String email) {
        Optional<Customer> customerOpt = customerRepository.findByEmail(email);
        if (customerOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Customer email not found!");
        }

        List<Hotel> hotels = hotelRepository.findAll();

        // Convert hotels to DTOs without manager
        List<Map<String, Object>> response = hotels.stream().map(hotel -> {
            Map<String, Object> data = new HashMap<>();
            data.put("name", hotel.getName());
            data.put("address", hotel.getAddress());
            data.put("contact", hotel.getContact());
            data.put("description", hotel.getDescription());
            data.put("amenities", hotel.getAmenities());
            data.put("rooms", hotel.getRooms());
            return data;
        }).toList();

        return ResponseEntity.ok(response);
    }


    // View Rooms in a Hotel
//    @GetMapping("/hotels/{hotelId}/rooms")
//    public ResponseEntity<?> getRoomsByHotel(@PathVariable Long hotelId, @RequestParam String email) {
//        if (!isCustomerValid(email)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Customer email not found!");
//        }
//
//        Optional<Hotel> hotelOpt = hotelRepository.findById(hotelId);
//        if (hotelOpt.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hotel not found!");
//        }
//
//        List<Room> rooms = roomRepository.findByHotel(hotelOpt.get());
//        return ResponseEntity.ok(rooms);
//    }
    
    // find rooms by hotel name
    @GetMapping("/hotels/rooms")
    public ResponseEntity<?> getRoomsByHotelName(
            @RequestParam String name,
            @RequestParam String email) {

        if (!isCustomerValid(email)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Customer email not found!");
        }

        Optional<Hotel> hotelOpt = hotelRepository.findFirstByNameContainingIgnoreCase(name);
        if (hotelOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hotel not found!");
        }

        List<Room> rooms = roomRepository.findByHotel(hotelOpt.get());
        return ResponseEntity.ok(rooms);
    }


    // Search Hotels by keyword
    @GetMapping("/hotels/search")
    public ResponseEntity<?> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam String email) {

        if (!isCustomerValid(email)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Customer email not found!");
        }

        List<Hotel> hotels;

        if (name != null && address != null) {
            hotels = hotelRepository.findByNameContainingIgnoreCaseAndAddressContainingIgnoreCase(name, address);
        } else if (name != null) {
            hotels = hotelRepository.findByNameContainingIgnoreCase(name);
        } else if (address != null) {
            hotels = hotelRepository.findByAddressContainingIgnoreCase(address);
        } else {
            hotels = hotelRepository.findAll(); // or return bad request
        }

        List<Map<String, Object>> response = hotels.stream().map(hotel -> {
            Map<String, Object> data = new HashMap<>();
            data.put("id", hotel.getId());
            data.put("name", hotel.getName());
            data.put("address", hotel.getAddress());
            data.put("contact", hotel.getContact());
            data.put("description", hotel.getDescription());
            data.put("amenities", hotel.getAmenities());
            data.put("rooms", hotel.getRooms());
            return data;
        }).toList();

        return ResponseEntity.ok(response);
    }

    // Filter Rooms by type and availability
//    @GetMapping("/hotels/{hotelId}/rooms/filter")
//    public List<Room> filterRooms(
//            @PathVariable Long hotelId,
//            @RequestParam(required = false) String type,
//            @RequestParam(required = false) Boolean available,
//            @RequestParam String email
//    ) {
//        if (!isCustomerValid(email)) return List.of();
//
//        Optional<Hotel> hotelOpt = hotelRepository.findById(hotelId);
//        if (hotelOpt.isEmpty()) return List.of();
//
//        List<Room> rooms = roomRepository.findByHotel(hotelOpt.get());
//
//        if (type != null) rooms = rooms.stream().filter(r -> r.getType().equalsIgnoreCase(type)).toList();
//        if (available != null) rooms = rooms.stream().filter(r -> r.getAvailable().equals(available)).toList();
//
//        return rooms;
//    }
    
    @GetMapping("/hotels/rooms/filter")
    public ResponseEntity<?> filterRoomsByHotelName(
            @RequestParam String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam String email
    ) {
        if (!isCustomerValid(email)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Customer email not found!");
        }

        Optional<Hotel> hotelOpt = hotelRepository.findFirstByNameContainingIgnoreCase(name);
        if (hotelOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No matching hotel found!");
        }

        List<Room> rooms = roomRepository.findByHotel(hotelOpt.get());

        if (type != null) {
            rooms = rooms.stream()
                    .filter(r -> r.getType().equalsIgnoreCase(type))
                    .toList();
        }

        if (available != null) {
            rooms = rooms.stream()
                    .filter(r -> r.getAvailable().equals(available))
                    .toList();
        }

        if (minPrice != null) {
            rooms = rooms.stream()
                    .filter(r -> r.getPrice() >= minPrice)
                    .toList();
        }

        if (maxPrice != null) {
            rooms = rooms.stream()
                    .filter(r -> r.getPrice() <= maxPrice)
                    .toList();
        }

        return ResponseEntity.ok(rooms);
    }
}

