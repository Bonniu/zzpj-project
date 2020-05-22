package app.hotel.services.implementation;

import app.database.entities.Guest;
import app.database.exceptions.validations.GuestValidator;
import app.database.exceptions.validations.Validator;
import app.database.repositories.GuestRepository;
import app.hotel.services.GuestServiceInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public void insert( Guest entity) {
        this.guestRepository.insert(entity);
    }

    @Override
    public void update( Guest entity) {
        this.guestRepository.save(entity);
    }

    @Override
    public void delete( Guest entity) {
        this.guestRepository.delete(entity);
    }
}
