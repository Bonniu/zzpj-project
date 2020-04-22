package app.hotel.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class UserController {

    @FXML
    private TextField userName;
    @FXML
    private TextField userSurname;
    @FXML
    private TextField userType;


    public void addUser() {
        System.out.println("add user");
        printTextFields();
        switchMainWindow();
    }

    public void modifyUser() {
        System.out.println("modify user");
        printTextFields();
        switchMainWindow();
    }

    public void printTextFields() {
        System.out.println(userName.getText());
        System.out.println(userSurname.getText());
        System.out.println(userType.getText());
    }

    public void switchMainWindow() {
        AuxiliaryController.switchMainWindow();
    }
}
