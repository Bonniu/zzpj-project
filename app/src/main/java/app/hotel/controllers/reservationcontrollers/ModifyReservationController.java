package app.hotel.controllers.reservationcontrollers;

import app.database.entities.Guest;
import app.database.entities.Reservation;
import app.database.entities.Room;
import app.hotel.controllers.AuxiliaryController;
import app.hotel.controllers.InitializeController;
import app.hotel.dbservices.implementation.ReservationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

import static app.hotel.controllers.AuxiliaryController.generateAlert;
import static java.time.temporal.ChronoUnit.DAYS;

@Getter
@Controller
public class ModifyReservationController implements Initializable, InitializeController {

    @FXML
    private TextField reservationId;


    @FXML
    private ChoiceBox choiceBoxGuestId;

    @FXML
    private ChoiceBox choiceBoxRoomId;


    @FXML
    private DatePicker reservationStartDate;


    @FXML
    private DatePicker reservationEndDate;

    @FXML
    private TextField reservationTotalPrice;

    @FXML
    private ChoiceBox reservationIdPayed;

    private final ReservationService reservationService;

    private Reservation selectedReservation;

    @Autowired
    public ModifyReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    @Override
    public void initData(Object object) {
        ArrayList<Object> objectList = (ArrayList<Object>) object;
        selectedReservation = (Reservation) objectList.get(0);
        ObservableList<Guest> guestList = (ObservableList<Guest>) objectList.get(1);
        ObservableList<Room> roomList = (ObservableList<Room>) objectList.get(2);

        int i = 0;
        int guestIndex = 0;
        for (Guest guest : guestList) {
            if (guest.getPesel().equals(selectedReservation.getGuestId())) {
                guestIndex = i;
            }
            i++;
        }
        i = 0;

        int roomIndex = 0;
        for (Room room : roomList) {
            if (room.getNumber().equals(selectedReservation.getRoomId())) {
                roomIndex = i;
            }
            i++;
        }


        choiceBoxSetData(guestList, roomList);
        modifyReservationSetData(guestIndex, roomIndex);
    }

    public void modifyReservation() throws ParseException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        DateFormat originalFormat = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

        selectedReservation.setId(reservationId.getText());
        Room room = (Room) choiceBoxRoomId.getSelectionModel().getSelectedItem();
        selectedReservation.setRoomId(room.getNumber());
        Guest guest = (Guest) choiceBoxGuestId.getSelectionModel().getSelectedItem();
        selectedReservation.setGuestId(guest.getPesel());
        if (reservationStartDate.getValue() == null) {
            Date date = originalFormat.parse(reservationStartDate.getEditor().getText());
            String formattedDate = targetFormat.format(date);
            selectedReservation.setStartDate(LocalDate.parse(formattedDate));
        } else {
            selectedReservation.setStartDate(LocalDate.parse(formatter.format(reservationStartDate.getValue())));
        }

        if (reservationEndDate.getValue() == null) {
            Date date = originalFormat.parse(reservationEndDate.getEditor().getText());
            String formattedDate = targetFormat.format(date);
            selectedReservation.setEndDate(LocalDate.parse(formattedDate));
        } else {
            selectedReservation.setEndDate(LocalDate.parse(formatter.format(reservationEndDate.getValue())));
        }
        selectedReservation.setTotalPrice(Float.parseFloat(reservationTotalPrice.getText()));
        selectedReservation.setPayed(stateStringToBoolean((String)reservationIdPayed.getSelectionModel().getSelectedItem()));

        reservationService.update(selectedReservation);
        switchMainWindow();
    }


    private void modifyReservationSetData(int guestIndex, int roomIndex) {

        reservationId.setText(selectedReservation.getId());
        choiceBoxGuestId.getSelectionModel().select(guestIndex);
        choiceBoxRoomId.getSelectionModel().select(roomIndex);
        reservationStartDate.setValue(selectedReservation.getStartDate());
        reservationEndDate.setValue(selectedReservation.getEndDate());
        reservationTotalPrice.setText(String.valueOf(selectedReservation.getTotalPrice()));
        reservationIdPayed.setItems(FXCollections.observableArrayList("opłacona", "nieopłacona"));
        System.out.println(stateBooleanToString(selectedReservation.isPayed()));
        reservationIdPayed.getSelectionModel().select(stateBooleanToString(selectedReservation.isPayed()));
    }

    private void choiceBoxSetData(ObservableList<Guest> guestList, ObservableList<Room> roomList) {
        choiceBoxGuestId.setItems(guestList);
        choiceBoxRoomId.setItems(roomList);
    }

    private void totalPriceOfReservation() {
        //dziura między wyborem daty a wpisaniem jej do edytora
        if (reservationStartDate.getEditor().getText().length() > 0 || reservationEndDate.getEditor().getText().length() > 0) {
            Room room = (Room) getChoiceBoxRoomId().getSelectionModel().getSelectedItem();
            LocalDate startDate = LocalDate.parse(reservationStartDate.getValue().toString());
            LocalDate endDate = LocalDate.parse(getReservationEndDate().getValue().toString());


            Long daysBetween = DAYS.between(startDate, endDate);
            Float roomPrice = room.getPrice();
            double totalPrice = daysBetween * roomPrice;
            totalPrice = Math.round(totalPrice * 100.0) / 100.0;


            if (!Objects.isNull(selectedReservation)) {
                if (selectedReservation.getTotalPrice() > 0 && selectedReservation.isPayed() && reservationTotalPrice.getText().length() > 0) {
                    System.out.println(reservationTotalPrice.getText());
                    double prevPrice = selectedReservation.getTotalPrice();
                    double difference = prevPrice - totalPrice;
                    if (difference > 0) {
                        generateAlert("", "Należy oddać klientowi " + Math.abs(Math.round(difference * 100.0) / 100.0) + ".", Alert.AlertType.INFORMATION);
                    } else if (difference < 0) {
                        generateAlert("", "Klient musi dopłacić " + Math.abs(Math.round(difference * 100.0) / 100.0) + ".", Alert.AlertType.INFORMATION);
                    }
                }
            }

            reservationTotalPrice.setText(String.valueOf(totalPrice));
        }


    }

    private boolean stateStringToBoolean(String state){
        if(state.equals("opłacona")) {
            return true;
        }else{
            return false;
        }
    }

    private int stateBooleanToString(Boolean state){
        if(state){
            return 0;
        }else {
            return 1;
        }
    }


    public void switchMainWindow() {
        AuxiliaryController.switchMainWindow();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // jak nie wciskamy generuj raport to ...
        if (!url.toString().contains("reservationReportWindow") && !url.toString().contains("addPayWindow")) {
            reservationStartDate.valueProperty().addListener((observable) -> totalPriceOfReservation());
            reservationEndDate.valueProperty().addListener((observable) -> totalPriceOfReservation());
        }
    }
}
