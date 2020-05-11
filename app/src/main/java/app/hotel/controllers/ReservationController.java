package app.hotel.controllers;

import app.database.api.CurrencyService;
import app.database.entities.Guest;
import app.database.entities.Reservation;
import app.database.entities.Room;
import app.hotel.dbservices.implementation.GuestService;
import app.hotel.dbservices.implementation.ReservationService;
import app.hotel.dbservices.implementation.RoomService;
import app.hotel.reportmakers.ReservationReport;
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


@SuppressWarnings(value = "unchecked")

@Getter
@Controller
public class ReservationController implements Initializable, ModifyController {

    @FXML
    private Label currencyValue;

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
    private TextField reservationIdPayed;

    @FXML
    private ComboBox<String> possibleCurrency;

    private final CurrencyService currencyService;
    private final ReservationService reservationService;

    private Reservation selectedReservation;

    private ReservationService reservations;
    private RoomService rooms;
    private GuestService guests;

    @Autowired
    public ReservationController(CurrencyService currencyService, ReservationService reservationService) {
        this.currencyService = currencyService;
        this.reservationService = reservationService;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        InitPayForReservationWindow();
        // jak nie wciskamy generuj raport to ...
        if (!url.toString().contains("reservationReportWindow") && !url.toString().contains("addPayWindow")) {
            reservationStartDate.valueProperty().addListener((observable) -> totalPriceOfReservation());
            reservationEndDate.valueProperty().addListener((observable) -> totalPriceOfReservation());
        }
    }


    public void addReservation() {

        if (getReservationStartDate().getValue().isAfter(getReservationEndDate().getValue())
                || getReservationStartDate().getValue().isEqual(getReservationEndDate().getValue())) {
            generateAlert("", "Data rozpoczęcia musi być przed datą zakończenia rezerwacji!", Alert.AlertType.ERROR);
            return;
        }
        Reservation reservation = new Reservation();
        Guest g = (Guest) getChoiceBoxGuestId().getSelectionModel().getSelectedItem();
        Room r = (Room) getChoiceBoxRoomId().getSelectionModel().getSelectedItem();
        reservation.setGuestId(g.getPesel());
        reservation.setRoomId(r.getNumber());
        reservation.setStartDate(LocalDate.parse(getReservationStartDate().getValue().toString()));
        reservation.setEndDate(LocalDate.parse(getReservationEndDate().getValue().toString()));

        reservation.setTotalPrice(Float.parseFloat(getReservationTotalPrice().getText()));
        reservation.setPayed(false);

        reservationService.insert(reservation);
        switchMainWindow();
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
        selectedReservation.setPayed(Boolean.parseBoolean(reservationIdPayed.getText()));

        reservationService.update(selectedReservation);
        switchMainWindow();
    }

    private ObservableList<String> InitCurrency() {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(currencyService.getPossibleRates());
        return observableList;
    }

    public void payForReservation() {
        System.out.println("paid from user");
        // printTextFields();
        switchMainWindow();
    }

    private void InitPayForReservationWindow() {
        if (possibleCurrency == null)
            return;

        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(currencyService.getPossibleRates());

        this.possibleCurrency.setItems(observableList);
        this.possibleCurrency.getSelectionModel().selectFirst();
    }

    public void rateValue() {
        if (currencyService.getCurrencyRestModel() != null) {
            String convertedVal = String.valueOf(
                    currencyService
                            .getStrategyContext()
                            .findStrategy(possibleCurrency.getValue())
                            .rateMoney(30, currencyService.getCurrencyRestModel().getRates()));     //TODO HARDCODED CASH

            currencyValue.setText(convertedVal);
        }
    }

    public void generateReportButtonFunction() {

        LocalDate dateFrom = getReservationStartDate().getValue();
        LocalDate dateTo = getReservationEndDate().getValue();

        if (dateFrom == null && dateTo == null) {
            generateAlert("", "Jedna z podanych dat nie może być pusta", Alert.AlertType.INFORMATION);
            return;
        }
        if (dateFrom == null)
            dateFrom = LocalDate.of(1900, 1, 1);
        if (dateTo == null)
            dateTo = LocalDate.of(2100, 1, 1);

        ReservationReport rr = new ReservationReport(dateFrom, dateTo, reservations, rooms, guests);

        rr.generateReport();
        switchMainWindow();

    }


    public void switchMainWindow() {
        AuxiliaryController.switchMainWindow();
    }

    // tak wiem troche XD
    @Override
    public void initData(Object object) {

        //modify reservation
        try {
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
        } catch (ClassCastException ignored) {

        }

        // add reservation
        try {
            ArrayList<Object> objectList = (ArrayList<Object>) object;
            ObservableList<Guest> guestList = (ObservableList<Guest>) objectList.get(0);
            ObservableList<Room> roomList = ((ObservableList<Room>) objectList.get(1))
                    .filtered(x -> !x.getState().equals("niedostępny")); //filtrowanie niedostępnych pokojów
            choiceBoxSetData(guestList, roomList);
        } catch (ClassCastException ignored) {

        }

        // generate raport
        try {
            ArrayList<Object> objectList = (ArrayList<Object>) object;
            guests = (GuestService) objectList.get(0);
            rooms = (RoomService) objectList.get(1);
            reservations = (ReservationService) objectList.get(2);
        } catch (ClassCastException ignored) {

        }
    }

    private void choiceBoxSetData(ObservableList<Guest> guestList, ObservableList<Room> roomList) {
        choiceBoxGuestId.setItems(guestList);
        choiceBoxRoomId.setItems(roomList);
    }


    private void modifyReservationSetData(int guestIndex, int roomIndex) {

        reservationId.setText(selectedReservation.getId());
        choiceBoxGuestId.getSelectionModel().select(guestIndex);
        choiceBoxRoomId.getSelectionModel().select(roomIndex);
        reservationStartDate.setValue(selectedReservation.getStartDate());
        reservationEndDate.setValue(selectedReservation.getEndDate());
        reservationTotalPrice.setText(String.valueOf(selectedReservation.getTotalPrice()));
        reservationIdPayed.setText(String.valueOf(selectedReservation.isPayed()));
    }
}
