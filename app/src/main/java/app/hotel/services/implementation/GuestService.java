package app.hotel.services.implementation;

import app.database.entities.Guest;
import app.database.repositories.GuestRepository;
import app.hotel.services.GuestServiceInterface;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Service
public class GuestService implements GuestServiceInterface {

    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    @Override
    public Optional<Guest> find(String id) {
        return this.guestRepository.findById(id);
    }

    @Override
    public List<Guest> findAll() {
        return guestRepository.findAll();
    }

    @Override
    public Guest insert(@RequestBody Guest entity) {
        this.guestRepository.insert(entity);
        return entity;
    }

    @Override
    public void update(@RequestBody Guest entity) {
        this.guestRepository.save(entity);
    }

    @Override
    public void delete(@RequestBody Guest entity) {
        this.guestRepository.delete(entity);
    }
}
