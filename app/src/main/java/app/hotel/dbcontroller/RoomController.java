package app.hotel.dbcontroller;

import app.database.entities.Room;
import app.database.repositories.RoomRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/rooms")
public class RoomController {
    private RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @GetMapping("/allRooms")
    public List<Room> getAllRooms() {
        List<Room> rooms = this.roomRepository.findAll();
        return rooms;
    }

    @GetMapping("/room/{id}")
    public Optional<Room> getRoomById(@PathVariable("id") String id) {
        Optional<Room> room = this.roomRepository.findById(id);
        return room;
    }

    @PostMapping
    public void insertRoom(@RequestBody Room room) {
        this.roomRepository.insert(room);
    }

    @PutMapping
    public void updateRoom(@RequestBody Room room) {
        this.roomRepository.save(room);
    }

    @DeleteMapping
    public void deleteRoom(@RequestBody Room room) {
        this.roomRepository.delete(room);
    }

}
