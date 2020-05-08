package app.hotel.dbservices.implementation;

import app.database.entities.Room;
import app.database.repositories.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    private RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        List<Room> rooms = this.roomRepository.findAll();
        return rooms;
    }

    public Optional<Room> getRoomById(String id) {
        Optional<Room> room = this.roomRepository.findById(id);
        return room;
    }

    public void insertRoom(@RequestBody Room room) {
        this.roomRepository.insert(room);
    }

    public void updateRoom(@RequestBody Room room) {
        this.roomRepository.save(room);
    }

    public void deleteRoom(@RequestBody Room room) {
        this.roomRepository.delete(room);
    }

}
