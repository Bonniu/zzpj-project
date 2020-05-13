package app.hotel.controllers.roomcontroller;

import app.database.entities.Room;
import app.hotel.controllers.AuxiliaryController;
import app.hotel.controllers.InitializeController;
import app.hotel.dbservices.implementation.RoomService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Getter
@Controller
public class AddRoomController  {

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
        room.setState("dostępny");

        roomService.insert(room);
        switchMainWindow();
    }

    public void switchMainWindow() {
        AuxiliaryController.switchMainWindow();
    }

}
