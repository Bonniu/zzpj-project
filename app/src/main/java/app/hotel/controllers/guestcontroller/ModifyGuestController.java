package app.hotel.controllers.guestcontroller;

import app.database.entities.Guest;
import app.hotel.controllers.AuxiliaryController;
import app.hotel.controllers.InitializeController;
import app.hotel.dbservices.implementation.GuestService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Getter
public class ModifyGuestController implements InitializeController {

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


    private Guest selectedGuest;
    //DB
    @Autowired
    private GuestService guestService;


    public void initData(Object object) {
        selectedGuest = (Guest) object;
        guestName.setText(selectedGuest.getName());
        guestSurname.setText(selectedGuest.getSurname());
        guestPhonenumber.setText(String.valueOf(selectedGuest.getPhoneNumber()));
        guestDiscount.setText(String.valueOf(selectedGuest.getDiscount()));
    }

    public void modifyGuest() {

        selectedGuest.setName(guestName.getText());
        selectedGuest.setSurname(guestSurname.getText());
        selectedGuest.setPhoneNumber(Integer.parseInt(guestPhonenumber.getText()));
        selectedGuest.setDiscount(0);
        //TODO
        // selectedGuest.setDiscount(Integer.parseInt(getGuestDiscount().getText()));
        guestService.update(selectedGuest);
        switchMainWindow();
    }

    public void switchMainWindow() {
        AuxiliaryController.switchMainWindow();
    }
}
