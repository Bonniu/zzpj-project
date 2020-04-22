package app.hotel.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class GuestController {

    @FXML
    private TextField guestName;
    @FXML
    private TextField guestSurname;
    @FXML
    private TextField guestPhonenumber;


    public void addGuest() {
        System.out.println("add guest");
        printTextFields();
        switchMainWindow();
    }

    public void modifyGuest() {
        System.out.println("modify guest");
        printTextFields();
        switchMainWindow();
    }

    public void printTextFields() {
        System.out.println(guestName.getText());
        System.out.println(guestSurname.getText());
        System.out.println(guestPhonenumber.getText());
    }

    public void switchMainWindow() {
        AuxiliaryController.switchMainWindow();
    }


}
