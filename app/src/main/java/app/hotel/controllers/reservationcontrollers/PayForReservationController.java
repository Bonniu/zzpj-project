package app.hotel.controllers.reservationcontrollers;

import app.database.api.CurrencyService;
import app.database.entities.Reservation;
import app.hotel.controllers.AuxiliaryController;
import app.hotel.controllers.InitializeController;
import app.hotel.services.implementation.ReservationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;


@SuppressWarnings(value = "unchecked")

@Getter
@Controller
public class PayForReservationController implements Initializable, InitializeController {

    @FXML
    private TextField reservationId;

    @FXML
    private TextField reservationTotalPrice;

    @FXML
    private ComboBox<String> possibleCurrency;

    @FXML
    private Label currencyValue;

    private final CurrencyService currencyService;
    private final ReservationService reservationService;

    private Reservation selectedReservation;

    private boolean firstTime;


    @Autowired
    public PayForReservationController(CurrencyService currencyService, ReservationService reservationService) {
        this.currencyService = currencyService;
        this.reservationService = reservationService;
        firstTime = true;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        possibleCurrency.valueProperty().addListener((observable) -> rateValue());
        InitPayForReservationWindow();
    }

    public void payForReservation() {
        if (!selectedReservation.isPayed()) {
            String rounded = new DecimalFormat("#.##")
                    .format(Float.parseFloat(currencyValue.getText()))
                    .replace(",", ".");
            selectedReservation.setPayed(true);
            selectedReservation.setTotalPrice(rounded + " " +
                    possibleCurrency.getSelectionModel().getSelectedItem());
            reservationService.update(selectedReservation);
        }
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
        if (firstTime) {
            firstTime = false;
        } else {
            if (currencyService.getCurrencyRestModel() != null) {
                float hajs = Float.parseFloat(reservationTotalPrice.getText().split(" ")[0]);
                String convertedVal = String.valueOf(
                        currencyService
                                .getStrategyContext()
                                .findStrategy(possibleCurrency.getValue())
                                .rateMoney(hajs, currencyService.getCurrencyRestModel().getRates()));

                currencyValue.setText(convertedVal);
            }
        }
    }

    public void switchMainWindow() {
        firstTime = true;
        AuxiliaryController.switchMainWindow();
    }

    private void PayForReservationSetData() {
        reservationId.setText(selectedReservation.getId());
        reservationTotalPrice.setText(String.valueOf(selectedReservation.getTotalPrice()));
    }

    @Override
    public void initData(Object object) {
        selectedReservation = (Reservation) object;
        PayForReservationSetData();
        rateValue();
    }
}
