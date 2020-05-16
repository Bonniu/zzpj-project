package app.hotel.controllers.reservationcontrollers;

import app.database.entities.Guest;
import app.database.entities.Reservation;
import app.database.entities.Room;
import app.database.exceptions.HotelException;
import app.database.exceptions.validations.Validator;
import app.hotel.controllers.AuxiliaryController;
import app.hotel.controllers.InitializeController;
import app.hotel.services.implementation.GuestService;
import app.hotel.services.implementation.ReservationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static app.hotel.controllers.AuxiliaryController.generateAlert;
import static java.time.temporal.ChronoUnit.DAYS;

@Getter
@Controller
public class ModifyReservationController implements Initializable, InitializeController {

    @FXML
    private TextField reservationId;


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

    @FXML
    private ChoiceBox reservationIdPayed;

    private final ReservationService reservationService;
    private GuestService guestService;
    private double previousPrice;
    private Reservation selectedReservation;
    private final Validator<HashMap<String, String>> validator;

    @Autowired
    public ModifyReservationController(ReservationService reservationService,
                                       @Qualifier("reservation") Validator<HashMap<String, String>> validator) {
        this.reservationService = reservationService;
        this.validator = validator;
    }

    @Override
    public void initData(Object object) {
        ArrayList<Object> objectList = (ArrayList<Object>) object;
        selectedReservation = (Reservation) objectList.get(0);
        ObservableList<Guest> guestList = (ObservableList<Guest>) objectList.get(1);
        ObservableList<Room> roomList = (ObservableList<Room>) objectList.get(2);
        guestService = (GuestService) objectList.get(3);
        modifyReservationSetData(guestList, roomList);
        this.previousPrice = countTotalPricePLN();
        reservationTotalPrice.setText(previousPrice + " PLN");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reservationStartDate.valueProperty().addListener((observable) -> totalPriceOfReservation());
        reservationEndDate.valueProperty().addListener((observable) -> totalPriceOfReservation());
        choiceBoxGuestId.valueProperty().addListener((observable) -> totalPriceOfReservation());
        choiceBoxRoomId.valueProperty().addListener((observable) -> totalPriceOfReservation());
    }

    public void switchMainWindow() {
        AuxiliaryController.switchMainWindow();
    }

    public void modifyReservation() throws ParseException {
        try {
            validator.validateUpdate(new HashMap<>() {{
                put("setStartDate", getReservationStartDate().getValue().toString());
                put("setEndDate", getReservationEndDate().getValue().toString());
            }});
        } catch (HotelException hotelException) {
            generateAlert("Rezerwacja nie została zaktualizowana!",
                    hotelException.displayErrors(),
                    Alert.AlertType.ERROR);
            return;
        }
        selectedReservation.setId(reservationId.getText());
        Room room = (Room) choiceBoxRoomId.getSelectionModel().getSelectedItem();
        selectedReservation.setRoomId(room.getNumber());
        Guest guest = (Guest) choiceBoxGuestId.getSelectionModel().getSelectedItem();
        selectedReservation.setGuestId(guest.getPesel());
        checkIfOtherPrice();
        selectedReservation.setTotalPrice(reservationTotalPrice.getText());
        //selectedReservation.setPayed(stateStringToBoolean((String) reservationIdPayed.getSelectionModel().getSelectedItem()));
        selectedReservation.setPayed(false);

        //data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateFormat originalFormat = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (reservationStartDate.getValue() == null) {
            Date date = originalFormat.parse(reservationStartDate.getEditor().getText());
            String formattedDate = targetFormat.format(date);
            selectedReservation.setStartDate(LocalDate.parse(formattedDate));
        } else {
            selectedReservation.setStartDate(LocalDate.parse(formatter.format(reservationStartDate.getValue())));
        }

        if (reservationEndDate.getValue() == null) {
            Date date = originalFormat.parse(reservationEndDate.getEditor().getText());
            String formattedDate = targetFormat.format(date);
            selectedReservation.setEndDate(LocalDate.parse(formattedDate));
        } else {
            selectedReservation.setEndDate(LocalDate.parse(formatter.format(reservationEndDate.getValue())));
        }

        reservationService.update(selectedReservation);
        switchMainWindow();
    }

    private void checkIfOtherPrice() {
        double totalPrice = Double.parseDouble(reservationTotalPrice.getText().split(" ")[0]);
        if (previousPrice > 0 && selectedReservation.isPayed() && reservationTotalPrice.getText().length() > 0) {
            subtractDiscountFromGuest();
            double difference = previousPrice - totalPrice;
            double differenceABS = Math.abs(Math.round(difference * 100.0) / 100.0);
            if (difference > 0) {
                generateAlert("", "Należy oddać klientowi " + differenceABS + ".", Alert.AlertType.INFORMATION);
            } else if (difference < 0) {
                generateAlert("", "Klient musi dopłacić " + differenceABS + ".", Alert.AlertType.INFORMATION);
            }
        }
    }

    private void subtractDiscountFromGuest() {
        Optional<Guest> guestOpt = guestService.find(selectedReservation.getGuestId());
        if (guestOpt.isEmpty())
            return;
        Guest guest = guestOpt.get();
        int discount = guest.getDiscount();
        if (discount > 0) {
            guest.setDiscount(discount - 1);
            guestService.update(guest);
        }
    }

    private int getChoiceBoxGuestIndex(ObservableList<Guest> guestList) {
        for (int i = 0; i < guestList.size(); i++) {
            if (guestList.get(i).getPesel().equals(selectedReservation.getGuestId())) {
                return i;
            }
        }
        return -1;
    }

    private int getChoiceBoxRoomIndex(ObservableList<Room> roomList) {
        for (int i = 0; i < roomList.size(); i++) {
            if (roomList.get(i).getNumber().equals(selectedReservation.getRoomId())) {
                return i;
            }
        }
        return -1;
    }

    private void modifyReservationSetData(ObservableList<Guest> guestList, ObservableList<Room> roomList) {
        int guestIndex = getChoiceBoxGuestIndex(guestList);
        int roomIndex = getChoiceBoxRoomIndex(roomList);
        choiceBoxSetData(guestList, roomList);
        reservationId.setText(selectedReservation.getId());
        choiceBoxGuestId.getSelectionModel().select(guestIndex);
        choiceBoxRoomId.getSelectionModel().select(roomIndex);
        reservationStartDate.setValue(selectedReservation.getStartDate());
        reservationEndDate.setValue(selectedReservation.getEndDate());
        reservationTotalPrice.setText(String.valueOf(selectedReservation.getTotalPrice()));
        reservationIdPayed.setItems(FXCollections.observableArrayList("opłacona", "nieopłacona"));
        reservationIdPayed.getSelectionModel().select(stateBooleanToString(selectedReservation.isPayed()));
    }

    private void choiceBoxSetData(ObservableList<Guest> guestList, ObservableList<Room> roomList) {
        choiceBoxGuestId.setItems(guestList);
        choiceBoxRoomId.setItems(roomList);
    }

    private void totalPriceOfReservation() {
        // sprawdzenie czy wszystkie pola wymagane do obliczenia ceny sa wypelnione
        if (reservationStartDate.getValue() != null && reservationEndDate.getValue() != null
                && choiceBoxRoomId.getSelectionModel().getSelectedIndex() > -1
                && choiceBoxGuestId.getSelectionModel().getSelectedIndex() > -1) {
            double totalPrice = countTotalPricePLN();
            reservationTotalPrice.setText(totalPrice + " PLN");
        }
    }

    private double countTotalPricePLN() {
        Room room = (Room) getChoiceBoxRoomId().getSelectionModel().getSelectedItem();
        LocalDate startDate = LocalDate.parse(reservationStartDate.getValue().toString());
        LocalDate endDate = LocalDate.parse(getReservationEndDate().getValue().toString());

        Long daysBetween = DAYS.between(startDate, endDate);
        Float roomPrice = room.getPrice();
        double totalPrice = daysBetween * roomPrice;
        double discount = totalPrice * 0.01 * ((Guest) getChoiceBoxGuestId().getSelectionModel().getSelectedItem()).getDiscount();
        totalPrice -= discount;
        return totalPrice;
    }

    private boolean stateStringToBoolean(String state) {
        return state.equals("opłacona");
    }

    private int stateBooleanToString(Boolean state) {
        if (state) {
            return 0;
        } else {
            return 1;
        }
    }


}
