package app.hotel.controllers;

import app.database.api.CurrencyService;
import app.database.entities.Guest;
import app.database.entities.Reservation;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Getter
@Controller
public class ReservationController implements Initializable {

    @FXML
    private Label currencyValue;

    @FXML
    private TextField reservationId;

    @FXML
    private TextField reservationGuestId;

    @FXML
    private TextField reservationRoomId;

    @FXML
    private DateCell reservationStartDate;

    @FXML
    private DateCell reservationEndDate;

    @FXML
    private TextField reservationTotalPrice;

    @FXML
    private DateCell reservationDateFrom;

    @FXML
    private DateCell reservationDateTo;

    @FXML
    private ChoiceBox<String> possibleCurrency;

    private final CurrencyService currencyService;

    @Autowired
    public ReservationController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.possibleCurrency.setItems(InitCurrency());
        this.possibleCurrency.getSelectionModel().selectFirst();
        this.currencyService.getCurrency();
    }

    @Autowired
    private ReservationService reservationService;

    public void addReservation() {
        Reservation reservation = new Reservation();
        reservation.setRoomId(getReservationRoomId().getText());
        reservation.setGuestId(getReservationGuestId().getText());
        //reservation.setStartDate(getReservationStartDate());

        //guestService.insertGuest(guest);
        switchMainWindow();
    }

    public void modifyReservation() {
        //TODO
        System.out.println("reservation controller modify");
        printTextFields();
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
        System.out.println(reservationStartDate.getText());
        System.out.println(reservationEndDate.getText());
        System.out.println(reservationTotalPrice.getText());

    }

    public void switchMainWindow() {
        AuxiliaryController.switchMainWindow();
    }


}
