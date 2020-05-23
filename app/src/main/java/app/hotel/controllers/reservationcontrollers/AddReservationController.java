package app.hotel.controllers.reservationcontrollers;

import app.database.entities.Guest;
import app.database.entities.Reservation;
import app.database.entities.Room;
import app.database.exceptions.HotelException;
import app.database.exceptions.validations.Validator;
import app.hotel.controllers.AuxiliaryController;
import app.hotel.controllers.InitializeController;
import app.hotel.services.implementation.ReservationService;
import javafx.collections.FXCollections;
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
import java.util.List;
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

    private ObservableList allRooms;
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
        reservationStartDate.valueProperty().addListener((observable) -> checkRooms());
        reservationEndDate.valueProperty().addListener((observable) -> checkRooms());
        choiceBoxGuestId.valueProperty().addListener((observable) -> totalPriceOfReservation());
        choiceBoxRoomId.valueProperty().addListener((observable) -> totalPriceOfReservation());
    }

    private void totalPriceOfReservation() {

        //dziura miÄ™dzy wyborem daty a wpisaniem jej do edytora
        if (reservationStartDate.getValue() != null && reservationEndDate.getValue() != null
                && choiceBoxRoomId.getSelectionModel().getSelectedIndex() > -1
                && choiceBoxGuestId.getSelectionModel().getSelectedIndex() > -1) {
            Room room = (Room) getChoiceBoxRoomId().getSelectionModel().getSelectedItem();
            LocalDate startDate = LocalDate.parse(reservationStartDate.getValue().toString());
            LocalDate endDate = LocalDate.parse(getReservationEndDate().getValue().toString());


            Long daysBetween = DAYS.between(startDate, endDate);
            Float roomPrice = room.getPrice();
            double totalPrice = daysBetween * roomPrice;
            double discount = totalPrice * 0.01 * ((Guest) getChoiceBoxGuestId().getSelectionModel().getSelectedItem()).getDiscount();
            totalPrice -= discount;
            totalPrice = Math.round(totalPrice * 100.0) / 100.0;
            reservationTotalPrice.setText(String.valueOf(totalPrice));
        }
    }

    private void checkRooms() {
        if (reservationStartDate.getValue() != null && reservationEndDate.getValue() != null) {
            List<Reservation> reservations = reservationService.findAll();
            ArrayList<Room> filteredRooms = filterOccupiedRooms(reservations);
            choiceBoxRoomId.setItems(FXCollections.observableArrayList(filteredRooms));
        }

    }

    private ArrayList<Room> filterOccupiedRooms(List<Reservation> reservations) {
        ArrayList list = new ArrayList<>(allRooms);
        for (int i = 0; i < allRooms.size(); i++) {
            Room room = (Room) allRooms.get(i);
            for (int j = 0; j < reservations.size(); j++) {
                if (reservations.get(j).getRoomId().equals(room.getNumber())) {
                    LocalDate rStartDate = reservations.get(j).getStartDate();
                    LocalDate rEndDate = reservations.get(j).getEndDate();
                    if ((!rStartDate.isBefore(reservationEndDate.getValue())
                            && !rEndDate.isBefore(reservationEndDate.getValue()))
                            || (!rStartDate.isAfter(reservationStartDate.getValue())
                            && !rEndDate.isAfter(reservationStartDate.getValue()))) {
                        System.out.println("GIT");
                    } else {
                        System.out.println("KOLIDUJE");
                        System.out.println(reservations.get(j));
                        list.remove(room);
                    }
                }
            }
        }
        return list;
    }


    public void addReservation() {

        try {
            validator.validateInsert(new HashMap<>() {{
                put("setStartDate", getReservationStartDate().getValue().toString());
                put("setEndDate", getReservationEndDate().getValue().toString());
            }});
        } catch (HotelException hotelException) {
            generateAlert("Rezerwacja nie zostaĹ‚a dodana!",
                    hotelException.displayErrors(),
                    Alert.AlertType.ERROR);
            return;
        }

        Reservation reservation = new Reservation();
        Guest g = (Guest) getChoiceBoxGuestId().getSelectionModel().getSelectedItem();
        Room r = (Room) getChoiceBoxRoomId().getSelectionModel().getSelectedItem();
        reservation.setGuestId(g.getIDcard());
        reservation.setRoomId(r.getNumber());
        reservation.setStartDate(LocalDate.parse(getReservationStartDate().getValue().toString()));
        reservation.setEndDate(LocalDate.parse(getReservationEndDate().getValue().toString()));
        double cost = Double.parseDouble(getReservationTotalPrice().getText());
        reservation.setTotalPrice(cost + " PLN");
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
                .filtered(x -> !x.getState().equals("niedostÄ™pny")); //filtrowanie niedostÄ™pnych pokojĂłw
        choiceBoxSetData(guestList, roomList);

        // uniemoĹĽliwienie rezerwacji przed dniem dzisiejszym
        reservationStartDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                LocalDate today = LocalDate.now();
                if (empty || item.compareTo(today) < 0) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ff9ebe;");
                }

            }
        });
        // uniemoĹĽliwienie rezerwacji przed dniem startu rezerwacji
        reservationEndDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (reservationStartDate.getValue() != null)
                    if (item.isBefore(reservationStartDate.getValue().plusDays(1))) {
                        setDisable(true);
                        setStyle("-fx-background-color: #ff9ebe;");

                    }
            }
        });

    }

    private void choiceBoxSetData(ObservableList<Guest> guestList, ObservableList<Room> roomList) {
        choiceBoxGuestId.setItems(guestList);
        choiceBoxRoomId.setItems(roomList);
        allRooms = roomList;
    }
}
