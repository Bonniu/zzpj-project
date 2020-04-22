package app.hotel.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RoomController {
    @FXML
    private TextField roomNumber;
    @FXML
    private TextField roomCapacity;
    @FXML
    private TextField roomPrice;


    public void addRoom() {
        System.out.println("add room");
        printTextFields();
        switchMainWindow();
    }

    public void modifyRoom() {
        System.out.println("modify room");
        printTextFields();
        switchMainWindow();
    }

    public void printTextFields() {
        System.out.println(roomNumber.getText());
        System.out.println(roomCapacity.getText());
        System.out.println(roomPrice.getText());
    }

    public void switchMainWindow() {
        AuxiliaryController.switchMainWindow();
    }

}
