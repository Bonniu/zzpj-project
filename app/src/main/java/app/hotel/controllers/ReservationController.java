package app.hotel.controllers;

import app.database.api.CurrencyService;
import app.database.entities.Guest;
import app.database.entities.Reservation;
import app.database.entities.Room;
import app.hotel.reportmakers.ReservationReport;
import app.hotel.dbcontroller.ReservationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import static app.hotel.controllers.AuxiliaryController.generateError;

@SuppressWarnings(value = "unchecked")

@Getter
@Controller
public class ReservationController implements Initializable, ModifyController {

    @FXML
    private Label currencyValue;

    @FXML
    private TextField reservationId;

    @FXML
    private TextField reservationGuestId;

    @FXML
    private ChoiceBox choiceBoxGuestId;

    @FXML
    private ChoiceBox choiceBoxRoomId;

    @FXML
    private TextField reservationRoomId;

    @FXML
    private DatePicker reservationStartDate;

    @FXML
    private DatePicker reservationDateFrom;

    @FXML
    private DatePicker reservationDateTo;

    @FXML
    private DatePicker reservationEndDate;

    @FXML
    private TextField reservationTotalPrice;

    @FXML
    private TextField reservationIdPayed;

    @FXML
    private ComboBox<String> possibleCurrency;

    private final CurrencyService currencyService;
    private final ReservationService reservationService;

    private Reservation selectedReservation;

    private ObservableList<Reservation> reservations;

    @Autowired
    public ReservationController(CurrencyService currencyService, ReservationService reservationService) {
        this.currencyService = currencyService;
        this.reservationService = reservationService;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        InitPayForReservationWindow();
    }


    public void addReservation() {

        if (getReservationStartDate().getValue().isAfter(getReservationEndDate().getValue())
                || getReservationStartDate().getValue().isEqual(getReservationEndDate().getValue())) {
            generateError("Data rozpoczęcia musi być przed datą zakończenia rezerwacji!");
            return;
        }
        Reservation reservation = new Reservation();
        Guest g = (Guest) getChoiceBoxGuestId().getSelectionModel().getSelectedItem();
        Room r = (Room) getChoiceBoxRoomId().getSelectionModel().getSelectedItem();
        reservation.setGuestId(g.getPidn());
        reservation.setRoomId(r.getNumber());
        reservation.setStartDate(LocalDate.parse(getReservationStartDate().getValue().toString()));
        reservation.setEndDate(LocalDate.parse(getReservationEndDate().getValue().toString()));

        // liczenie ? mamy room i dni
        reservation.setTotalPrice(Float.parseFloat(getReservationTotalPrice().getText()));
        reservation.setPayed(false);

        reservationService.insertReservation(reservation);
        switchMainWindow();
    }

    public void modifyReservation() throws ParseException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        DateFormat originalFormat = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

        selectedReservation.setId(reservationId.getText());
        selectedReservation.setRoomId(reservationRoomId.getText());
        selectedReservation.setGuestId(reservationGuestId.getText());
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
        selectedReservation.setTotalPrice(Float.parseFloat(reservationTotalPrice.getText()));
        selectedReservation.setPayed(Boolean.parseBoolean(reservationIdPayed.getText()));

        reservationService.updateReservation(selectedReservation);
        switchMainWindow();
    }

    private ObservableList<String> InitCurrency() {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(currencyService.getPossibleRates());
        return observableList;
    }

    public void payForReservation() {
        System.out.println("paid from user");
        // printTextFields();
        switchMainWindow();
    }

    private void InitPayForReservationWindow() {
        if (possibleCurrency == null)
            return;

        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(currencyService.getPossibleRates());

        this.possibleCurrency.setItems(observableList);
        this.possibleCurrency.getSelectionModel().selectFirst();
    }

    public void rateValue() {
        if (currencyService.getCurrencyRestModel() != null) {
            String convertedVal = String.valueOf(
                    currencyService
                            .getStrategyFactory()
                            .findStrategy(possibleCurrency.getValue())
                            .rateMoney(30, currencyService.getCurrencyRestModel().getRates()));     //TODO HARDCODED CASH

            currencyValue.setText(convertedVal);
        }
    }

    public void generateReportButtonFunction() {
        ReservationReport rr = new ReservationReport(
                getReservationDateFrom().getValue(),
                getReservationDateTo().getValue(),
                reservations);

        rr.generateReport();
        switchMainWindow();

    }

    public void printTextFields() {
        System.out.println(reservationGuestId.getText());
        System.out.println(reservationRoomId.getText());
        System.out.println(reservationTotalPrice.getText());

    }

    public void switchMainWindow() {
        AuxiliaryController.switchMainWindow();
    }

    // tak wiem troche XD
    @Override
    public void initData(Object object) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        //modify reservation
        try {
            selectedReservation = (Reservation) object;
            modifyReservationSetData(formatter);
        } catch (ClassCastException ignored) {

        }

        // add reservation
        try {
            ArrayList<Object> objectList = (ArrayList<Object>) object;
            ObservableList<Guest> guestList = (ObservableList<Guest>) objectList.get(0);
            ObservableList<Room> roomList = (ObservableList<Room>) objectList.get(1);
            choiceBoxSetData(guestList, roomList);
        } catch (ClassCastException ignored) {

        }

        // generate raport
        try {
            reservations = (ObservableList<Reservation>) object;
        } catch (ClassCastException ignored) {

        }
    }

    private void choiceBoxSetData(ObservableList<Guest> guestList, ObservableList<Room> roomList) {
        choiceBoxGuestId.setItems(guestList);
        choiceBoxRoomId.setItems(roomList);
    }


    private void modifyReservationSetData(DateTimeFormatter formatter) {
        reservationId.setText(selectedReservation.getId());
        reservationGuestId.setText(selectedReservation.getGuestId());
        reservationRoomId.setText(selectedReservation.getRoomId());
        reservationStartDate.getEditor().setText(formatter.format(selectedReservation.getStartDate()));
        reservationEndDate.getEditor().setText(formatter.format(selectedReservation.getEndDate()));
        reservationTotalPrice.setText(String.valueOf(selectedReservation.getTotalPrice()));
        reservationIdPayed.setText(String.valueOf(selectedReservation.isPayed()));
    }
}
