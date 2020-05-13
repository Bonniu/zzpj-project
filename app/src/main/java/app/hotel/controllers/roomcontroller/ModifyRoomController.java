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
public class ModifyRoomController implements InitializeController {

    @FXML
    private TextField roomNumber;
    @FXML
    private TextField roomCapacity;
    @FXML
    private TextField roomPrice;
    @FXML
    private ChoiceBox roomStateChoiceBox;

    private Room selectedRoom;

    @Autowired
    private RoomService roomService;

    public void modifyRoom() {
        selectedRoom.setNumber(roomNumber.getText());
        selectedRoom.setCapacity(Integer.parseInt(roomCapacity.getText()));
        selectedRoom.setPrice(Float.parseFloat(roomPrice.getText()));
        selectedRoom.setState((String) roomStateChoiceBox.getSelectionModel().getSelectedItem());

        roomService.update(selectedRoom);
        switchMainWindow();

    }

    public void switchMainWindow() {
        AuxiliaryController.switchMainWindow();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initData(Object object) {
        selectedRoom = (Room) object;
        roomNumber.setText(selectedRoom.getNumber());
        roomCapacity.setText(String.valueOf(selectedRoom.getCapacity()));
        roomPrice.setText(String.valueOf(selectedRoom.getPrice()));
        roomStateChoiceBox.setItems(FXCollections.observableArrayList("dostępny", "niedostępny", "zajęty"));
        roomStateChoiceBox.getSelectionModel().select(selectedRoom.getState());

    }
}
