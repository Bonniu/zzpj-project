package app.hotel.dbcontroller;

import app.database.entities.Reservation;
import app.database.repositories.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/reservations")
public class ReservationService {

    private ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/allReservations")
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = this.reservationRepository.findAll();
        return reservations;
    }

    @GetMapping("/{id}")
    public Optional<Reservation> getReservationById( @PathVariable("id") String id) {
        Optional<Reservation> reservation = this.reservationRepository.findById(id);
        return reservation;
    }

    @PostMapping
    public void insertReservation(@RequestBody Reservation reservation) {
        this.reservationRepository.insert(reservation);
    }

    @PutMapping
    public void updateReservation(@RequestBody Reservation reservation) {
        this.reservationRepository.save(reservation);
    }

    @DeleteMapping
    public void deleteReservation(@RequestBody Reservation reservation) {
        this.reservationRepository.delete(reservation);
    }
}
