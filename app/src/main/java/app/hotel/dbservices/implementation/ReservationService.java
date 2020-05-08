package app.hotel.dbservices.implementation;

import app.database.entities.Reservation;
import app.database.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public List<Reservation> getOutdatedNoPaidReservation(){
        return this.reservationRepository.
                getReservationsByStartDateBeforeAndIsPayed(LocalDate.now().plusDays(2), false);
    }

    public Optional<Reservation> getReservationById(String id) {
        Optional<Reservation> reservation = this.reservationRepository.findById(id);
        return reservation;
    }


    public void insertReservation(Reservation reservation) {
        this.reservationRepository.insert(reservation);
    }

    public void updateReservation(Reservation reservation) {
        this.reservationRepository.save(reservation);
    }

    public void deleteReservation(Reservation reservation) {
        this.reservationRepository.delete(reservation);
    }
}
