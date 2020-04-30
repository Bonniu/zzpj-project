package app.hotel.controllers;

import app.database.entities.Guest;
import app.database.entities.Room;
import app.hotel.dbcontroller.RoomService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.stereotype.Controller;

@Getter
@Controller
public class RoomController implements ModifyController {
    @FXML
    private TextField roomNumber;
    @FXML
    private TextField roomCapacity;
    @FXML
    private TextField roomPrice;
    @FXML
    private TextField roomState;

    private Room selectedRoom;

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
        selectedRoom.setNumber(roomNumber.getText());
        selectedRoom.setCapacity(Integer.parseInt(roomCapacity.getText()));
        selectedRoom.setPrice(Float.parseFloat(roomPrice.getText()));
        selectedRoom.setState(Boolean.parseBoolean(roomState.getText()));

        roomService.updateRoom(selectedRoom);
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

    @Override
    public void initData(Object object) {
        Room room = (Room) object;
        selectedRoom = room;
        roomNumber.setText(selectedRoom.getNumber());
        roomCapacity.setText(String.valueOf(selectedRoom.getCapacity()));
        roomPrice.setText(String.valueOf(selectedRoom.getPrice()));
        roomState.setText(String.valueOf(selectedRoom.getState()));

    }
}
