package app.hotel.controllers;

import app.database.entities.Guest;
import app.database.entities.Reservation;
import app.database.entities.Room;
import app.database.entities.User;
import app.hotel.Main;
import app.hotel.dbcontroller.GuestService;
import app.hotel.dbcontroller.ReservationService;
import app.hotel.dbcontroller.RoomService;
import app.hotel.dbcontroller.UserService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;

import static app.hotel.controllers.AuxiliaryController.changeScene;

@Controller
public class BasicController {


    @FXML
    private Tab rooms, guests, reservations, users;
    @FXML
    private URL location;

    @FXML
    private TableView<Room> roomsTable;

    @FXML
    private TableColumn<Room, String>
            roomId,
            roomNumber;

    @FXML
    private TableColumn<Room,Number> roomCapacity;
    @FXML
    private TableColumn<Room,Number> roomPurchasePrice;
    @FXML
    private TableColumn<Room,Boolean> roomState;

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
    private TableColumn<Reservation,Number> totalPrice;
    @FXML
    private TableColumn<Reservation,Boolean> isPayed;

    @FXML
    public Reservation getSelectedReservation() {
        return reservationsTable.getSelectionModel().getSelectedItem();
    }


    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String>
            userId,
            userFirstName,
            userLastName,
            userType;

    @FXML
    public User getSelectedUser() {
        return usersTable.getSelectionModel().getSelectedItem();
    }

    //DB
    @Autowired
    private GuestService guestService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private UserService userService;

    private ObservableList<Guest> guestList = FXCollections.observableArrayList();
    private ObservableList<Reservation> reservationList = FXCollections.observableArrayList();
    private ObservableList<Room> roomList = FXCollections.observableArrayList();
    private ObservableList<User> userList = FXCollections.observableArrayList();


    // ---- methods ------------------------------------------------------------------------------------------------
    // ----rooms----
    public void switchAddRoomWindow() {
        URL addRoomWindowLocation = Main.class.getResource("/" + "addRoomWindow.fxml");
        changeScene(addRoomWindowLocation, 460, 360);
    }

    public void switchModifyRoomWindow() {
        URL modifyRoomWindowLocation = Main.class.getResource("/" + "modifyRoomWindow.fxml");
        changeScene(modifyRoomWindowLocation, 460, 360);
    }

    public void deleteRoom() {
        System.out.println("Usuniety pokoj");
    }

    public void generateRoomRaport() {
        System.out.println("raport pokoi");
    }

    // ----guests----
    public void switchAddGuestWindow() {
        URL addGuestWindowLocation = Main.class.getResource("/" + "addGuestWindow.fxml");
        changeScene(addGuestWindowLocation, 460, 360);
    }

    public void switchModifyGuestWindow() {
        URL modifyGuestWindowLocation = Main.class.getResource("/" + "modifyGuestWindow.fxml");
        changeScene(modifyGuestWindowLocation, 460, 360);
    }

    public void deleteGuest() {
        System.out.println("Usuniety chlop");
    }

    // ---- reservations ----
    public void switchAddReservationWindow() {
        URL addReservationWindowLocation = Main.class.getResource("/" + "addReservationWindow.fxml");
        changeScene(addReservationWindowLocation, 460, 360);
    }

    public void switchModifyReservationWindow() {
        URL modifyReservationWindowLocation = Main.class.getResource("/" + "modifyReservationWindow.fxml");
        changeScene(modifyReservationWindowLocation, 460, 360);
    }

    public void deleteReservation() {
        System.out.println("delete reservation");
    }

    public void generateReservationReport() {
        URL reservationReportWindowLocation = Main.class.getResource("/" + "reservationReportWindow.fxml");
        changeScene(reservationReportWindowLocation, 460, 360);
    }

    public void paidWindow() {
        URL addPayWindowLocation = Main.class.getResource("/" + "addPayWindow.fxml");
        changeScene(addPayWindowLocation, 460, 360);
    }

    // ---- users ----
    public void switchAddUserWindow() {
        URL addUserWindowLocation = Main.class.getResource("/" + "addUserWindow.fxml");
        changeScene(addUserWindowLocation, 460, 360);
    }

    public void switchModifyUserWindow() {
        URL modifyUserWindowLocation = Main.class.getResource("/" + "modifyUserWindow.fxml");
        changeScene(modifyUserWindowLocation, 460, 360);
    }

    public void deleteUser() {
        System.out.println("Delete user");
    }

    // ---- other methods ----
    public void refreshAll() {

        //////////////GUEST/////////////////////////
        guestList.clear();
        guestList.addAll(guestService.getAllGuests());

        guestId.setCellValueFactory(guestStringCellDataFeatures ->
                new SimpleStringProperty(guestStringCellDataFeatures.getValue().getPidn())
        );

        guestName.setCellValueFactory(guestStringCellDataFeatures ->
                new SimpleStringProperty(guestStringCellDataFeatures.getValue().getName())
        );

        guestSurname.setCellValueFactory(guestStringCellDataFeatures ->
                new SimpleStringProperty(guestStringCellDataFeatures.getValue().getSurname())
        );

        guestPhonenumber.setCellValueFactory(guestStringCellDataFeatures ->
                new SimpleStringProperty(String.valueOf(guestStringCellDataFeatures.getValue().getPhoneNumber())));
        //TODO discount
        guestDiscount.setCellValueFactory(guestStringCellDataFeatures ->
                new SimpleStringProperty(String.valueOf(guestStringCellDataFeatures.getValue().getPhoneNumber())));
        guestsTable.setItems(guestList);

        /////////////RESERVATION//////////////////////////
        reservationList.clear();
        reservationList.addAll(reservationService.getAllReservations());

        reservationId.setCellValueFactory(reservationStringCellDataFeatures ->
                new SimpleStringProperty(reservationStringCellDataFeatures.getValue().getId())
        );
        guestID.setCellValueFactory(reservationStringCellDataFeatures ->
                new SimpleStringProperty(reservationStringCellDataFeatures.getValue().getGuest().getSurname())
        );
        roomID.setCellValueFactory(reservationStringCellDataFeatures ->
                new SimpleStringProperty(reservationStringCellDataFeatures.getValue().getRoom().getNumber())
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

        /////////////ROOM//////////////////////////
        roomList.clear();
        roomList.addAll(roomService.getAllRooms());

        roomID.setCellValueFactory(roomStringCellDataFeatures ->
                new SimpleStringProperty(roomStringCellDataFeatures.getValue().getId())
        );
        roomNumber.setCellValueFactory(roomStringCellDataFeatures ->
                new SimpleStringProperty(roomStringCellDataFeatures.getValue().getNumber())
        );
        roomCapacity.setCellValueFactory(roomIntegerCellDataFeatures ->
                new SimpleIntegerProperty(roomIntegerCellDataFeatures.getValue().getCapacity())
        );

        roomPurchasePrice.setCellValueFactory(roomFloatCellDataFeatures ->
                new SimpleFloatProperty(roomFloatCellDataFeatures.getValue().getPrice())
        );
        roomState.setCellValueFactory(roomBooleanCellDataFeatures ->
                new SimpleBooleanProperty(roomBooleanCellDataFeatures.getValue().getState())
        );
        roomsTable.setItems(roomList);

        /////////////USER//////////////////////////
        userList.clear();
        userList.addAll(userService.getAllUsers());

        userId.setCellValueFactory(roomStringCellDataFeatures ->
                new SimpleStringProperty(roomStringCellDataFeatures.getValue().getId())
        );
        userFirstName.setCellValueFactory(roomStringCellDataFeatures ->
                new SimpleStringProperty(roomStringCellDataFeatures.getValue().getName())
        );
        userLastName.setCellValueFactory(roomStringCellDataFeatures ->
                new SimpleStringProperty(roomStringCellDataFeatures.getValue().getSurname())
        );
        userType.setCellValueFactory(roomStringCellDataFeatures ->
                new SimpleStringProperty(roomStringCellDataFeatures.getValue().getUserType())
        );

        usersTable.setItems(userList);
    }


}
