package app.hotel.controllers.reservationcontrollers;

import app.database.api.CurrencyService;
import app.database.entities.Guest;
import app.database.entities.Reservation;
import app.database.entities.Room;
import app.hotel.controllers.AuxiliaryController;
import app.hotel.controllers.InitializeController;
import app.hotel.dbservices.implementation.GuestService;
import app.hotel.dbservices.implementation.ReservationService;
import app.hotel.dbservices.implementation.RoomService;
import app.hotel.reportmakers.ReservationReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static app.hotel.controllers.AuxiliaryController.generateAlert;
import static java.time.temporal.ChronoUnit.DAYS;


@SuppressWarnings(value = "unchecked")

@Getter
@Controller
public class PayForReservationController implements Initializable, InitializeController {

    @FXML
    private Label currencyValue;

    @FXML
    private TextField reservationId;

    @FXML
    private TextField reservationTotalPrice;

    @FXML
    private ComboBox<String> possibleCurrency;

    private final CurrencyService currencyService;
    private final ReservationService reservationService;

    private Reservation selectedReservation;


    @Autowired
    public PayForReservationController(CurrencyService currencyService, ReservationService reservationService) {
        this.currencyService = currencyService;
        this.reservationService = reservationService;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        InitPayForReservationWindow();
    }

    private ObservableList<String> InitCurrency() {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(currencyService.getPossibleRates());
        return observableList;
    }

    public void payForReservation() {
        if(selectedReservation.isPayed()){
            generateAlert("", "Rezerwacja jest już opłacona.", Alert.AlertType.INFORMATION);
            return;
        }else if(!selectedReservation.isPayed()){
            System.out.println("paid from user");
            selectedReservation.setPayed(true);
        }
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
                            .getStrategyContext()
                            .findStrategy(possibleCurrency.getValue())
                            .rateMoney(30, currencyService.getCurrencyRestModel().getRates()));     //TODO HARDCODED CASH

            currencyValue.setText(convertedVal);
        }
    }

    public void switchMainWindow() {
        AuxiliaryController.switchMainWindow();
    }

    private void PayForReservationSetData() {

        reservationId.setText(selectedReservation.getId());
        reservationTotalPrice.setText(String.valueOf(selectedReservation.getTotalPrice()));
    }

    @Override
    public void initData(Object object) {

        selectedReservation = (Reservation)object;

        PayForReservationSetData();
    }
}
