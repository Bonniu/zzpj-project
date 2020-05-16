package app.hotel.controllers.reservationcontrollers;

import app.database.entities.Guest;
import app.database.entities.Reservation;
import app.database.entities.Room;
import app.database.exceptions.HotelException;
import app.database.exceptions.validations.Validator;
import app.hotel.controllers.AuxiliaryController;
import app.hotel.controllers.InitializeController;
import app.hotel.services.implementation.ReservationService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import static app.hotel.controllers.AuxiliaryController.generateAlert;
import static java.time.temporal.ChronoUnit.DAYS;

@Getter
@Controller
public class AddReservationController implements Initializable, InitializeController {

    @FXML
    private ChoiceBox choiceBoxGuestId;

    @FXML
    private ChoiceBox choiceBoxRoomId;


    @FXML
    private DatePicker reservationStartDate;


    @FXML
    private DatePicker reservationEndDate;

    @FXML
    private TextField reservationTotalPrice;

    private final ReservationService reservationService;
    private final Validator<HashMap<String, String>> validator;


    @Autowired
    public AddReservationController(ReservationService reservationService,
                                    @Qualifier("reservation") Validator<HashMap<String, String>> validator) {
        this.reservationService = reservationService;
        this.validator = validator;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            reservationStartDate.valueProperty().addListener((observable) -> totalPriceOfReservation());
            reservationEndDate.valueProperty().addListener((observable) -> totalPriceOfReservation());
    }

    private void totalPriceOfReservation() {
        //dziura między wyborem daty a wpisaniem jej do edytora
        if (reservationStartDate.getEditor().getText().length() > 0 || reservationEndDate.getEditor().getText().length() > 0) {
            Room room = (Room) getChoiceBoxRoomId().getSelectionModel().getSelectedItem();
            LocalDate startDate = LocalDate.parse(reservationStartDate.getValue().toString());
            LocalDate endDate = LocalDate.parse(getReservationEndDate().getValue().toString());


            Long daysBetween = DAYS.between(startDate, endDate);
            Float roomPrice = room.getPrice();
            double totalPrice = daysBetween * roomPrice;
            totalPrice = Math.round(totalPrice * 100.0) / 100.0;
            reservationTotalPrice.setText(String.valueOf(totalPrice));
        }
    }

    public void addReservation() {

        try {
            validator.validateInsert(new HashMap<>() {{
                put("setStartDate", getReservationStartDate().getValue().toString());
                put("setEndDate", getReservationEndDate().getValue().toString());
            }});
        }
        catch (HotelException hotelException){
            generateAlert("Rezerwacja nie została dodana!",
                    hotelException.displayErrors(),
                    Alert.AlertType.ERROR);
            return;
        }

        Reservation reservation = new Reservation();
        Guest g = (Guest) getChoiceBoxGuestId().getSelectionModel().getSelectedItem();
        Room r = (Room) getChoiceBoxRoomId().getSelectionModel().getSelectedItem();
        reservation.setGuestId(g.getPesel());
        reservation.setRoomId(r.getNumber());
        reservation.setStartDate(LocalDate.parse(getReservationStartDate().getValue().toString()));
        reservation.setEndDate(LocalDate.parse(getReservationEndDate().getValue().toString()));
        System.out.println(getReservationTotalPrice().getText());
        reservation.setTotalPrice(getReservationTotalPrice().getText() + " PLN");
        reservation.setPayed(false);

        reservationService.insert(reservation);
        switchMainWindow();
    }

    public void switchMainWindow() {
        AuxiliaryController.switchMainWindow();
    }

    @Override
    public void initData(Object object) {

            ArrayList<Object> objectList = (ArrayList<Object>) object;
            ObservableList<Guest> guestList = (ObservableList<Guest>) objectList.get(0);
            ObservableList<Room> roomList = ((ObservableList<Room>) objectList.get(1))
                    .filtered(x -> !x.getState().equals("niedostępny")); //filtrowanie niedostępnych pokojów
            choiceBoxSetData(guestList, roomList);
    }

    private void choiceBoxSetData(ObservableList<Guest> guestList, ObservableList<Room> roomList) {
        choiceBoxGuestId.setItems(guestList);
        choiceBoxRoomId.setItems(roomList);
    }
}
