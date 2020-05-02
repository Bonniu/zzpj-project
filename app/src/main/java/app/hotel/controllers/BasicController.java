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
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static app.hotel.controllers.AuxiliaryController.changeScene;

@Controller
public class BasicController {

    private static Stage primaryStage;
    private static BorderPane mainLayout;

    @FXML
    private Tab rooms, guests, reservations, users;
    @FXML
    private URL location;

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
    private TableColumn<Room, Boolean> roomState;

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
        changeScene(modifyRoomWindowLocation, 460, 360, getSelectedRoom());
    }

    public void deleteRoom() {
        roomService.deleteRoom(getSelectedRoom());
        refreshAll();
    }

    public void generateRoomRaport() {

        ObservableList<Room> items = roomsTable.getItems();
        ArrayList<Room> list = new ArrayList<>(items);
        System.out.println(list.toString());
        System.out.println("raport pokoi");

        Document document = new Document();
        try {
            int h = LocalDateTime.now().getHour();
            int m = LocalDateTime.now().getMinute();
            String s = "room_report_" + LocalDate.now().toString() + "_" + h + "-" + m;
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(s + ".pdf"));
            document.open();
            document.add(new Paragraph("A Hello World PDF document."));
            document.close();
            writer.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
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
        guestService.deleteGuest(getSelectedGuest());
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
        changeScene(modifyReservationWindowLocation, 460, 360, reservation);
    }

    public void deleteReservation() {
        reservationService.deleteReservation(getSelectedReservation());
        refreshAll();
    }

    public void generateReservationReport() {
        URL reservationReportWindowLocation = Main.class.getResource("/" + "reservationReportWindow.fxml");
        changeScene(reservationReportWindowLocation, 460, 360, reservationList);
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
        changeScene(modifyUserWindowLocation, 460, 360, getSelectedUser());
    }

    public void deleteUser() {
        userService.deleteUser(getSelectedUser());
        refreshAll();
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

        /////////////ROOM//////////////////////////
        roomList.clear();
        roomList.addAll(roomService.getAllRooms());

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
