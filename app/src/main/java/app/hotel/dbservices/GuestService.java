package app.hotel.dbservices;

import app.database.entities.Guest;
import app.database.repositories.GuestRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Service
public class GuestService {

    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    public Optional<Guest> getGuestById(long id) {
        return this.guestRepository.findById(id);
    }

    public void insertGuest(@RequestBody Guest guest) {
        this.guestRepository.insert(guest);
    }

    public void updateGuest(@RequestBody Guest guest) {
        this.guestRepository.save(guest);
    }

    public void deleteGuest(@RequestBody Guest guest) {
        this.guestRepository.delete(guest);
    }

}
