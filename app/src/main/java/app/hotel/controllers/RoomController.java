package app.hotel.controllers;

import app.database.entities.Guest;
import app.database.entities.Room;
import app.hotel.dbcontroller.RoomService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Getter
@Controller
public class RoomController {
    @FXML
    private TextField roomNumber;
    @FXML
    private TextField roomCapacity;
    @FXML
    private TextField roomPrice;

    @Autowired
    private RoomService roomService;

    public void addRoom() {
        Room room = new Room();
        room.setNumber(getRoomNumber().getText());
        room.setCapacity(Integer.parseInt(getRoomCapacity().getText()));
        room.setPrice(Float.parseFloat(getRoomPrice().getText()));
        room.setState(true);

        roomService.insertRoom(room);
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
