package app.hotel.controllers.guestcontroller;

import app.database.entities.Guest;
import app.hotel.controllers.AuxiliaryController;
import app.hotel.dbservices.implementation.GuestService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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

    //DB
    @Autowired
    private GuestService guestService;

    public void addGuest() {
        Guest guest = new Guest();
        if (getGuestPesel().getText().length() != 11) {
            generateAlert("Gość nie został dodany do bazy danych.",
                    "PESEL nie ma 11 znaków.",
                    Alert.AlertType.ERROR);
            return;
        }
        try {
            Long.parseLong(getGuestPesel().getText());
        } catch (NumberFormatException e) {
            generateAlert("Gość nie został dodany do bazy danych.",
                    "PESEL jest nieprawidłowy.",
                    Alert.AlertType.ERROR);
            return;
        }
        guest.setPesel(getGuestPesel().getText());
        guest.setName(getGuestName().getText());
        guest.setSurname(getGuestSurname().getText());
        guest.setPhoneNumber(Integer.parseInt(getGuestPhonenumber().getText()));
        guest.setDiscount(Integer.parseInt(getGuestDiscount().getText()));

        guestService.insert(guest);

        switchMainWindow();
    }

    public void switchMainWindow() {
        AuxiliaryController.switchMainWindow();
    }
}
