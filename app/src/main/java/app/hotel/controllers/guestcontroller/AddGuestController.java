package app.hotel.controllers.guestcontroller;

import app.database.entities.Guest;
import app.database.exceptions.HotelException;
import app.database.exceptions.validations.Validator;
import app.hotel.controllers.AuxiliaryController;
import app.hotel.services.implementation.GuestService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.util.HashMap;

import static app.hotel.controllers.AuxiliaryController.generateAlert;

@Controller
@Getter
public class AddGuestController {

    @FXML
    public TextField guestPesel;

    @FXML
    private TextField guestName;

    @FXML
    private TextField guestSurname;

    @FXML
    private TextField guestPhonenumber;

    @FXML
    public TextField guestDiscount;

    private final GuestService guestService;
    private final Validator<HashMap<String, String>> validator;

    public AddGuestController(GuestService guestService,
                              @Qualifier("guest") Validator<HashMap<String, String>> validator) {
        this.guestService = guestService;
        this.validator = validator;
    }

    public void addGuest() {

    }

    public void switchMainWindow() {
        AuxiliaryController.switchMainWindow();
    }
}
