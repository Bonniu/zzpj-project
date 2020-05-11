package app.hotel.controllers;

import app.database.entities.Guest;
import app.database.entities.Reservation;
import app.database.entities.Room;
import app.hotel.Main;
import app.hotel.dbservices.implementation.GuestService;
import app.hotel.dbservices.implementation.ReservationService;
import app.hotel.dbservices.implementation.RoomService;
import app.hotel.reportmakers.RoomReport;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static app.hotel.controllers.AuxiliaryController.changeScene;
import static app.hotel.controllers.AuxiliaryController.generateAlert;

@Controller
public class BasicController implements Initializable {

    @FXML
    private TableView<Room> roomsTable;

    @FXML
    private TableColumn<Room, String>
            roomNumber;

    @FXML
    private TableColumn<Room, Number> roomCapacity;
    @FXML
    private TableColumn<Room, Number> roomPurchasePrice;
    @FXML
    private TableColumn<Room, String> roomState;

    public BasicController(GuestService guestService, ReservationService reservationService, RoomService roomService) {
        this.guestService = guestService;
        this.reservationService = reservationService;
        this.roomService = roomService;
    }

    @FXML
    public Room getSelectedRoom() {
        return roomsTable.getSelectionModel().getSelectedItem();
    }

    @FXML
    private TableView<Guest> guestsTable;
    @FXML
    private TableColumn<Guest, String>
            guestId,
            guestName,
            guestSurname,
            guestPhonenumber,
            guestDiscount;

    @FXML
    public Guest getSelectedGuest() {
        return guestsTable.getSelectionModel().getSelectedItem();
    }

    @FXML
    private TableView<Reservation> reservationsTable;
    @FXML
    private TableColumn<Reservation, String>
            reservationId,
            guestID,
            roomID,
            startDate,
            endDate;

    @FXML
    private TableColumn<Reservation, Number> totalPrice;
    @FXML
    private TableColumn<Reservation, Boolean> isPayed;

    @FXML
    public Reservation getSelectedReservation() {
        return reservationsTable.getSelectionModel().getSelectedItem();
    }

    //DB
    private final GuestService guestService;
    private final ReservationService reservationService;
    private final RoomService roomService;

    private ObservableList<Guest> guestList = FXCollections.observableArrayList();
    private ObservableList<Reservation> reservationList = FXCollections.observableArrayList();
    private ObservableList<Room> roomList = FXCollections.observableArrayList();


    // ---- methods ------------------------------------------------------------------------------------------------
    // ----rooms----
    public void switchAddRoomWindow() {
        URL addRoomWindowLocation = Main.class.getResource("/" + "addRoomWindow.fxml");
        changeScene(addRoomWindowLocation, 460, 360);
    }

    public void switchModifyRoomWindow() {
        URL modifyRoomWindowLocation = Main.class.getResource("/" + "modifyRoomWindow.fxml");
        changeScene(modifyRoomWindowLocation, 460, 360, getSelectedRoom());
    }

    public void deleteRoom() {
        roomService.delete(getSelectedRoom());
        refreshAll();
    }

    public void generateRoomRaport() {
        RoomReport rr = new RoomReport(roomsTable.getItems());
        rr.generateReport();
    }

    // ----guests----
    public void switchAddGuestWindow() {
        URL addGuestWindowLocation = Main.class.getResource("/" + "addGuestWindow.fxml");
        changeScene(addGuestWindowLocation, 460, 360);
    }

    public void switchModifyGuestWindow(ActionEvent event) throws IOException {

        URL modifyGuestWindowLocation = Main.class.getResource("/" + "modifyGuestWindow.fxml");
        changeScene(modifyGuestWindowLocation, 460, 360, getSelectedGuest());

    }

    public void deleteGuest() {
        System.out.println(getSelectedGuest());
        guestService.delete(getSelectedGuest());

        refreshAll();
    }

    // ---- reservations ----
    public void switchAddReservationWindow() throws IOException {
        URL addReservationWindowLocation = Main.class.getResource("/" + "addReservationWindow.fxml");
        ArrayList<Object> list = new ArrayList<>();
        list.add(guestsTable.getItems());
        list.add(roomsTable.getItems());
        changeScene(addReservationWindowLocation, 460, 360, list);
    }

    public void switchModifyReservationWindow() {
        Reservation reservation = getSelectedReservation();
        URL modifyReservationWindowLocation = Main.class.getResource("/" + "modifyReservationWindow.fxml");
        ArrayList<Object> list = new ArrayList<>();
        list.add(reservation);
        list.add(guestsTable.getItems());
        list.add(roomsTable.getItems());

        changeScene(modifyReservationWindowLocation, 460, 360, list);
    }

    public void deleteReservation() {
        reservationService.delete(getSelectedReservation());
        refreshAll();
    }

    public void generateReservationReport() {
        URL reservationReportWindowLocation = Main.class.getResource("/" + "reservationReportWindow.fxml");
        ArrayList<Object> list = new ArrayList<>();
        list.add(guestService);
        list.add(roomService);
        list.add(reservationService);
        changeScene(reservationReportWindowLocation, 460, 360, list);
    }

    public void paidWindow() {
        Reservation r = getSelectedReservation();
        URL addPayWindowLocation = Main.class.getResource("/" + "addPayWindow.fxml");
        changeScene(addPayWindowLocation, 460, 360);
    }

    public void warningRefreshReservations() {
        List<Reservation> outdatedReservations = reservationService.getOutdatedNoPaidReservation();
        if (outdatedReservations.size() != 0) {
            String ids = outdatedReservations.stream().
                    map(reservation -> reservation.getId() + ",\n")
                    .collect(Collectors.joining(""));
            generateAlert("Some reservations are outdated",
                    "consider remove reservations: " + ids,
                    Alert.AlertType.WARNING);

        }
        refreshReservations();
    }

    public void refreshReservations() {
        /////////////RESERVATION//////////////////////////
        reservationList.clear();
        reservationList.addAll(reservationService.findAll());


        reservationId.setCellValueFactory(reservationStringCellDataFeatures ->
                new SimpleStringProperty(reservationStringCellDataFeatures.getValue().getId())
        );
        guestID.setCellValueFactory(reservationStringCellDataFeatures ->
                new SimpleStringProperty(reservationStringCellDataFeatures.getValue().getGuestId())
        );
        roomID.setCellValueFactory(reservationStringCellDataFeatures ->
                new SimpleStringProperty(reservationStringCellDataFeatures.getValue().getRoomId())
        );
        startDate.setCellValueFactory(reservationStringCellDataFeatures ->
                new SimpleStringProperty(reservationStringCellDataFeatures.getValue().getStartDate().toString())
        );
        endDate.setCellValueFactory(reservationStringCellDataFeatures ->
                new SimpleStringProperty(reservationStringCellDataFeatures.getValue().getEndDate().toString())
        );
        totalPrice.setCellValueFactory(reservationFloatCellDataFeatures ->
                new SimpleFloatProperty(reservationFloatCellDataFeatures.getValue().getTotalPrice())
        );
        isPayed.setCellValueFactory(reservationBooleanProperty ->
                new SimpleBooleanProperty(reservationBooleanProperty.getValue().isPayed())
        );
        reservationsTable.setItems(reservationList);
    }

    // ---- other methods ----
    public void refreshAll() {

        //////////////GUEST/////////////////////////
        guestList.clear();
        guestList.addAll(guestService.findAll());

        guestId.setCellValueFactory(guestStringCellDataFeatures ->
                new SimpleStringProperty(guestStringCellDataFeatures.getValue().getPesel())
        );

        guestName.setCellValueFactory(guestStringCellDataFeatures ->
                new SimpleStringProperty(guestStringCellDataFeatures.getValue().getName())
        );

        guestSurname.setCellValueFactory(guestStringCellDataFeatures ->
                new SimpleStringProperty(guestStringCellDataFeatures.getValue().getSurname())
        );

        guestPhonenumber.setCellValueFactory(guestStringCellDataFeatures ->
                new SimpleStringProperty(String.valueOf(guestStringCellDataFeatures.getValue().getPhoneNumber())));

        guestDiscount.setCellValueFactory(guestStringCellDataFeatures ->
                new SimpleStringProperty(String.valueOf(guestStringCellDataFeatures.getValue().getDiscount())));

        guestsTable.setItems(guestList);


        /////////////ROOM//////////////////////////
        roomList.clear();
        roomList.addAll(roomService.findAll());

        roomNumber.setCellValueFactory(roomStringCellDataFeatures ->
                new SimpleStringProperty(roomStringCellDataFeatures.getValue().getNumber())
        );
        roomCapacity.setCellValueFactory(roomIntegerCellDataFeatures ->
                new SimpleIntegerProperty(roomIntegerCellDataFeatures.getValue().getCapacity())
        );

        roomPurchasePrice.setCellValueFactory(roomFloatCellDataFeatures ->
                new SimpleFloatProperty(roomFloatCellDataFeatures.getValue().getPrice())
        );
        roomState.setCellValueFactory(roomStringCellDataFeatures ->
                new SimpleStringProperty(roomStringCellDataFeatures.getValue().getState())
        );

        roomsTable.setItems(roomList);

        refreshReservations();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshAll();
    }
}
