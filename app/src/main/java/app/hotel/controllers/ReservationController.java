package app.hotel.controllers;

import app.database.api.CurrencyService;
import app.database.entities.Guest;
import app.database.entities.Reservation;
import app.hotel.dbcontroller.ReservationService;
import com.sun.javafx.scene.control.DoubleField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@Getter
@Controller
public class ReservationController implements Initializable,ModifyController {

    @FXML
    private Label currencyValue;

    @FXML
    private TextField reservationId;

    @FXML
    private TextField reservationGuestId;

    @FXML
    private TextField reservationRoomId;

    @FXML
    private TextField reservationStartDate;

    @FXML
    private TextField reservationEndDate;

    @FXML
    private TextField reservationTotalPrice;

    @FXML
    private TextField reservationDateFrom;

    @FXML
    private TextField reservationDateTo;

    @FXML
    private TextField reservationIdPayed;

    @FXML
    private ComboBox<String> possibleCurrency;

    private final CurrencyService currencyService;
    private final ReservationService reservationService;

    private Reservation selectedReservation;

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
        Reservation reservation = new Reservation();
        reservation.setRoomId(getReservationRoomId().getText());
        reservation.setGuestId(getReservationGuestId().getText());
        reservation.setStartDate(Date.valueOf(getReservationStartDate().getText()));
        reservation.setEndDate(Date.valueOf(getReservationEndDate().getText()));
        reservation.setTotalPrice(Float.parseFloat(getReservationTotalPrice().getText()));
        reservation.setPayed(false);

        reservationService.insertReservation(reservation);
        switchMainWindow();
    }

    public void modifyReservation() {
        //DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-yy")
        selectedReservation.setId(reservationId.getText());
        selectedReservation.setRoomId(reservationRoomId.getText());
        selectedReservation.setGuestId(reservationGuestId.getText());
        selectedReservation.setStartDate(Date.valueOf(reservationStartDate.getText()));
        selectedReservation.setEndDate(Date.valueOf(reservationEndDate.getText()));
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

    private void InitPayForReservationWindow(){
        if(possibleCurrency == null)
            return;

        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(currencyService.getPossibleRates());

        this.possibleCurrency.setItems(observableList);
        this.possibleCurrency.getSelectionModel().selectFirst();
    }

    public void rateValue() {
        if (currencyService.getCurrencyRestModel() != null) {
            System.out.println(possibleCurrency.getValue());

            String convertedVal = String.valueOf(
                    currencyService
                    .getStrategyFactory()
                    .findStrategy(possibleCurrency.getValue())
                    .rateMoney(30, currencyService.getCurrencyRestModel().getRates()));     //TODO HARDCODED CASH

            currencyValue.setText(convertedVal);
        }
    }

    public void generateReport() {
        //TODO
        System.out.println("reservation controller generate report");
        String regex = "[0-3][0-9].[0-1][0-9].[0-9][0-9][0-9][0-9]";
        if (reservationDateFrom.getText().matches(regex) && reservationDateTo.getText().matches(regex))
            switchMainWindow();
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Błąd");
            alert.setContentText("Co najmniej jedna z podanych dat nie zgadza się z podanym wzorem!");
            alert.showAndWait();
        }
    }

    public void printTextFields() {
        System.out.println(reservationGuestId.getText());
        System.out.println(reservationRoomId.getText());
        System.out.println(reservationTotalPrice.getText());

    }

    public void switchMainWindow() {
        AuxiliaryController.switchMainWindow();
    }


    @Override
    public void initData(Object object) {
        Reservation reservation = (Reservation) object;
        selectedReservation = reservation;
        reservationId.setText(selectedReservation.getId());
        reservationGuestId.setText(selectedReservation.getGuestId());
        reservationRoomId.setText(selectedReservation.getRoomId());
        reservationStartDate.setText(selectedReservation.getStartDate().toString());
        reservationEndDate.setText(selectedReservation.getEndDate().toString());
        reservationTotalPrice.setText(String.valueOf(selectedReservation.getTotalPrice()));
        reservationIdPayed.setText(String.valueOf(selectedReservation.isPayed()));
    }
}
