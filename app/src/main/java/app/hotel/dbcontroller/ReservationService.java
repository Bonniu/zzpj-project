package app.hotel.dbcontroller;

import app.database.entities.Reservation;
import app.database.repositories.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = this.reservationRepository.findAll();
        return reservations;
    }


    public Optional<Reservation> getReservationById(String id) {
        Optional<Reservation> reservation = this.reservationRepository.findById(id);
        return reservation;
    }


    public void insertReservation(@RequestBody Reservation reservation) {
        this.reservationRepository.insert(reservation);
    }


    public void updateReservation(@RequestBody Reservation reservation) {
        this.reservationRepository.save(reservation);
    }


    public void deleteReservation(@RequestBody Reservation reservation) {
        this.reservationRepository.delete(reservation);
    }
}
