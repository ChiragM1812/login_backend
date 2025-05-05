package com.springboot.login.controller;

import com.springboot.login.entity.Hotel;
import com.springboot.login.entity.Manager;
import com.springboot.login.entity.Room;
import com.springboot.login.repository.HotelRepository;
import com.springboot.login.repository.ManagerRepository;
import com.springboot.login.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/manager/room")
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ManagerRepository managerRepository;

    // Add Room
    @PostMapping("/add")
    public String addRoom(@RequestBody Room room, @RequestParam Long hotelId, @RequestParam String email) {
        Optional<Manager> managerOpt = managerRepository.findByEmail(email);
        Optional<Hotel> hotelOpt = hotelRepository.findById(hotelId);

        if (managerOpt.isEmpty()) return "Manager not found!";
        if (hotelOpt.isEmpty()) return "Hotel not found!";

        Hotel hotel = hotelOpt.get();
//        if (!hotel.getManager().getEmail().equals(email)) return "Unauthorized access!";

        room.setHotel(hotel);
        roomRepository.save(room);
        return "Room added successfully!";
    }

    // View Rooms in a Hotel
    @GetMapping("/view")
    public List<Room> viewRooms(@RequestParam Long hotelId, @RequestParam String email) {
        Optional<Manager> managerOpt = managerRepository.findByEmail(email);
        Optional<Hotel> hotelOpt = hotelRepository.findById(hotelId);

        if (managerOpt.isEmpty() || hotelOpt.isEmpty()) return List.of();
        Hotel hotel = hotelOpt.get();
        if (!hotel.getManager().getEmail().equals(email)) return List.of();

        return roomRepository.findByHotel(hotel);
    }

    // Update Room
    @PutMapping("/update/{roomNumber}")
    public String updateRoomByNumber(@PathVariable String roomNumber, @RequestBody Room updatedRoom, @RequestParam String email) {
    	Optional<Manager> managerOpt = managerRepository.findByEmail(email);
        if (managerOpt.isEmpty()) return "Manager not found!";
    	
    	Optional<Room> roomOpt = roomRepository.findByRoomNumber(roomNumber);
        if (roomOpt.isEmpty()) return "Room not found!";

        Room room = roomOpt.get();
        room.setType(updatedRoom.getType());
        room.setPrice(updatedRoom.getPrice());
        room.setAvailable(updatedRoom.getAvailable());

        roomRepository.save(room);
        return "Room updated successfully!";
    }

    // Delete Room
    @DeleteMapping("/delete/{roomNumber}")
    public String deleteRoom(@PathVariable String roomNumber, @RequestParam String email) {
    	Optional<Manager> managerOpt = managerRepository.findByEmail(email);
        if (managerOpt.isEmpty()) return "Manager not found!";
    	
        Optional<Room> roomOpt = roomRepository.findByRoomNumber(roomNumber);
        if (roomOpt.isEmpty()) return "Room not found!";

        roomRepository.delete(roomOpt.get());
        return "Room deleted successfully!";
    }

}

