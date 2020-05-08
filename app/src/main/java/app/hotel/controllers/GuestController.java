package app.hotel.controllers;

import app.database.entities.Guest;
import app.hotel.dbservices.implementation.GuestService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Getter
@Controller
public class GuestController implements ModifyController{

    @FXML
    public TextField guestDiscount;
    @FXML
    private TextField guestName;
    @FXML
    private TextField guestSurname;
    @FXML
    private TextField guestPhonenumber;

    private Guest selectedGuest;
    //DB
    @Autowired
    private GuestService guestService;

    public void addGuest() {
        Guest guest = new Guest();
        guest.setName(getGuestName().getText());
        guest.setSurname(getGuestSurname().getText());
        guest.setPhoneNumber(Integer.parseInt(getGuestPhonenumber().getText()));
        guest.setDiscount(Integer.parseInt(getGuestDiscount().getText()));
        System.out.println(Objects.isNull(guestService));

        guestService.insertGuest(guest);
        switchMainWindow();
    }

    public void initData(Object object)
    {
        Guest guest = (Guest)object;
        selectedGuest = guest;
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
        guestService.updateGuest(selectedGuest);

        switchMainWindow();
    }

    public void printTextFields() {
        System.out.println(guestName.getText());
        System.out.println(guestSurname.getText());
        System.out.println(guestPhonenumber);
    }

    public void switchMainWindow() {
        AuxiliaryController.switchMainWindow();
    }

}
